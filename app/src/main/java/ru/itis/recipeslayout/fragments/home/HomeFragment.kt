package ru.itis.recipeslayout.fragments.home

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.data.RecipeUiState
import ru.itis.recipeslayout.databinding.FragmentHomeBinding
import ru.itis.recipeslayout.fragments.detail.DetailFragment
import ru.itis.recipeslayout.model.entity.Recipe

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding: FragmentHomeBinding? = null
    private var adapter: RecipeAdapter? = null
    private var progressLoadDialog: CustomProgressLoadDialog? = null
    private var progressErrorDialog: CustomProgressErrorDialog? = null
    private var allRecipeList: List<Recipe> = listOf()

    private val homeViewModel: HomeViewModel by viewModels(
        factoryProducer = { HomeViewModel.Factory }
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        progressLoadDialog = CustomProgressLoadDialog(requireContext())
        progressErrorDialog = CustomProgressErrorDialog(requireContext())

        binding?.etSearch?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.let {
                    it?.updateDataset(
                        if (s.toString().isBlank()) {
                            allRecipeList
                        } else {
                            homeViewModel.filteredRecipeList = allRecipeList
                                .filter { recipe ->
                                    recipe.title.lowercase().contains(s.toString().lowercase())
                                }
                            homeViewModel.filteredRecipeList
                        }
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        homeViewModel.recipeUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RecipeUiState.Loading -> {
                    progressLoadDialog?.start("Загрузка...")
                }
                is RecipeUiState.Success -> {
                    progressLoadDialog?.stop()
                    progressErrorDialog?.stop()
                    initAdapter(
                        homeViewModel.filteredRecipeList.ifEmpty {
                            state.data
                        }
                    )
                    if (homeViewModel.filteredRecipeList.isEmpty()) {
                        homeViewModel.filteredRecipeList = state.data
                    }
                    allRecipeList = state.data
                }
                RecipeUiState.Error -> {
                    progressLoadDialog?.stop()
                    progressErrorDialog?.start(homeViewModel::getRecipes)
                }
            }
        }
    }

    private fun initAdapter(recipes: List<Recipe>) {
        adapter = RecipeAdapter(
            glide = Glide.with(this@HomeFragment),
            list = recipes,
            onClick = {
                findNavController().navigate(
                    resId = R.id.action_homeFragment_to_detailFragment,
                    args = DetailFragment.bundle(recipeId = it.id)
                )
            }
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