package ru.itis.recipeslayout.fragments.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ru.itis.recipeslayout.Constant.RECIPE_ID
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.data.RecipeInfoUIState
import ru.itis.recipeslayout.databinding.FragmentDetailBinding
import ru.itis.recipeslayout.fragments.home.CustomProgressErrorDialog
import ru.itis.recipeslayout.fragments.home.CustomProgressLoadDialog
import ru.itis.recipeslayout.model.response.DetailRecipeApiResponse

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var adapter: IngredientAdapter? = null
    private var binding: FragmentDetailBinding? = null
    private var progressLoadDialog: CustomProgressLoadDialog? = null
    private var progressErrorDialog: CustomProgressErrorDialog? = null

    private val detailViewModel: DetailViewModel by viewModels(
        factoryProducer = { DetailViewModel.Factory }
    )
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        val recipeId = arguments?.getInt(RECIPE_ID) ?: 0
        detailViewModel.recipeId = recipeId

        detailViewModel.getRecipeInfo(detailViewModel.recipeId)

        progressLoadDialog = CustomProgressLoadDialog(requireContext())
        progressErrorDialog = CustomProgressErrorDialog(requireContext())

        detailViewModel.detailRecipeUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RecipeInfoUIState.Loading -> {
                    progressLoadDialog?.start("Загрузка...")
                }
                is RecipeInfoUIState.Success -> {
                    progressLoadDialog?.stop()
                    progressErrorDialog?.stop()
                    initAdapter(state.data)
                }
                RecipeInfoUIState.Error -> {
                    progressLoadDialog?.stop()
                    progressErrorDialog?.start {
                        detailViewModel.getRecipeInfo(detailViewModel.recipeId)
                    }
                }
            }
        }
    }

    private fun removeHtmlTags(text: String): String {
        return text.replace(Regex("</?\\w+[^>]*>"), "")
    }

    @SuppressLint("SetTextI18n")
    private fun initAdapter(recipeInfo: DetailRecipeApiResponse) {
        adapter = IngredientAdapter(
            glide = Glide.with(this@DetailFragment),
            list = recipeInfo.extendedIngredientApiResponses
        )
        binding?.run {
            Glide
                .with(root.context)
                .load(recipeInfo.image)
                .into(ivRecipeImage)

            tvTitle.text = recipeInfo.title
            tvGlutenFree.text = "${ContextCompat.getString(root.context, R.string.gluten_free_tv)} "+if (recipeInfo.glutenFree) ContextCompat.getString(root.context,
                R.string.yes_tv) else ContextCompat.getString(root.context, R.string.no_tv)
            tvVegan.text = "${ContextCompat.getString(root.context, R.string.vegan_tv)} "+if (recipeInfo.vegan) ContextCompat.getString(root.context,
                R.string.yes_tv) else ContextCompat.getString(root.context, R.string.no_tv)
            tvTime.text = "${ContextCompat.getString(root.context, R.string.cooking_minutes_tv)} ${recipeInfo.cookingMinutes}"
            tvHealth.text = "${ContextCompat.getString(root.context, R.string.health_score_tv)} ${recipeInfo.healthScore}"
            tvServing.text = "${ContextCompat.getString(root.context, R.string.servings_tv)} ${recipeInfo.servings}"
            tvInstruction.text = "${ContextCompat.getString(root.context, R.string.instruction_tv)} ${removeHtmlTags(recipeInfo.instructions)}"
            rvIngredients.apply {
                this.adapter = this@DetailFragment.adapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
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