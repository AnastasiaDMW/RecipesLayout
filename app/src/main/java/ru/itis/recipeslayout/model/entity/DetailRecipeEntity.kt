package ru.itis.recipeslayout.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.itis.recipeslayout.Constant.DETAIL_RECIPE

@Entity(tableName = DETAIL_RECIPE)
data class DetailRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vegan: Boolean = false,
    val glutenFree: Boolean = false,
    val cookingMinutes: Int? = 0,
    val healthScore: Int = 0,
    val instructions: String = "",
    val title: String = "",
    val image: String = "",
    val servings: Int = 0
)
