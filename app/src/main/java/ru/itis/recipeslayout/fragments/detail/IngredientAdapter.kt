package ru.itis.recipeslayout.fragments.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.itis.recipeslayout.Constant.API_KEY
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.ItemIngredientBinding
import ru.itis.recipeslayout.model.response.ExtendedIngredientApiResponse
import java.util.Locale

class IngredientAdapter(
    private var list: List<ExtendedIngredientApiResponse>,
    private val glide: RequestManager
): RecyclerView.Adapter<IngredientAdapter.IngredientHolder>() {

    class IngredientHolder(
        private val binding: ItemIngredientBinding,
        private val glide: RequestManager,
    ): ViewHolder(binding.root) {

        private val requestOptions = RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.ALL)

        fun onBind(extendedIngredientApiResponse: ExtendedIngredientApiResponse) {
            binding.run {
                tvName.text = extendedIngredientApiResponse.original.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                tvServingUnit.text = "${extendedIngredientApiResponse.amount} ${extendedIngredientApiResponse.unit}"
                glide
                    .load("https://spoonacular.com/cdn/ingredients_100x100/"+extendedIngredientApiResponse.image+"?apiKey=$API_KEY")
                    .error(R.drawable.not_found)
                    .apply(requestOptions)
                    .into(ivIngredient)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientHolder = IngredientHolder(
        binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        glide = glide
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) = holder.onBind(list[position])

}