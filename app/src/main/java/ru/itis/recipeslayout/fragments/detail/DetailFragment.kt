package ru.itis.recipeslayout.fragments.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.itis.recipeslayout.Constant.RECIPE_ID
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var binding: FragmentDetailBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        val price = arguments?.getInt(RECIPE_ID) ?: 0
//        mapViewModel?.setPrice(price)
        binding?.run {
            tvRecipeId.text = price.toString()
        }

    }

    companion object {
        fun bundle(recipeId: Int): Bundle = Bundle().apply {
            putInt(RECIPE_ID, recipeId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}