package ru.itis.recipeslayout.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.ItemRecipeBinding
import ru.itis.recipeslayout.model.entity.Recipe

class RecipeAdapter(
    private var list: List<Recipe>,
    private val glide: RequestManager,
    private val onClick: (Recipe) -> Unit,
): RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {

    class RecipeHolder(
        private val binding: ItemRecipeBinding,
        private val glide: RequestManager,
        private val onClick: (Recipe) -> Unit,
    ): ViewHolder(binding.root) {

        private val requestOptions = RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.ALL)

        private val context: Context
            get() = itemView.context

        fun onBind(recipe: Recipe) {
            binding.run {
                tvTitle.text = recipe.title
                glide
                    .load(recipe.image)
                    .error(R.drawable.not_found)
                    .apply(requestOptions)
                    .into(ivRecipe)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeHolder = RecipeHolder(
        binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        glide = glide,
        onClick = onClick
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.onBind(list[position])
    }

    fun updateDataset(newList: List<Recipe>) {
        list = newList
        notifyDataSetChanged()
    }
}