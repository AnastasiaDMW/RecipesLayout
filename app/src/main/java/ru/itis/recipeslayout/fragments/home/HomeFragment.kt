package ru.itis.recipeslayout.fragments.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.data.RecipeUiState
import ru.itis.recipeslayout.databinding.FragmentHomeBinding
import ru.itis.recipeslayout.model.entity.Recipe

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding: FragmentHomeBinding? = null
    private var adapter: RecipeAdapter? = null
    private var progressLoadDialog: CustomProgressLoadDialog? = null
    private var progressErrorDialog: CustomProgressErrorDialog? = null

    private val homeViewModel: HomeViewModel by viewModels(
        factoryProducer = { HomeViewModel.Factory }
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        progressLoadDialog = CustomProgressLoadDialog(requireContext())
        progressErrorDialog = CustomProgressErrorDialog(requireContext())

        homeViewModel.recipeUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RecipeUiState.Loading -> {
                    progressLoadDialog?.start("Загрузка...")
                    Log.d("DATA", "Loading")
                }
                is RecipeUiState.Success -> {
                    progressLoadDialog?.stop()
                    progressErrorDialog?.stop()
                    initAdapter(state.data)
                    Log.d("DATA", "Success")
                }
                RecipeUiState.Error -> {
                    progressLoadDialog?.stop()
                    progressErrorDialog?.start(homeViewModel::getRecipes)
                    Log.d("DATA", "Error")
                }
            }
        }
    }

    private fun initAdapter(recipes: List<Recipe>) {
        adapter = RecipeAdapter(
            glide = Glide.with(this@HomeFragment),
            list = recipes,
            onClick = {}
        )
        binding?.run {
            rvRecipes.apply {
                this.adapter = this@HomeFragment.adapter
                layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}