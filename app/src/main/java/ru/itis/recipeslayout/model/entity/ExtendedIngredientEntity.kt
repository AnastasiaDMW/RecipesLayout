package ru.itis.recipeslayout.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.itis.recipeslayout.Constant.INGREDIENTS

@Entity(tableName = INGREDIENTS)
data class ExtendedIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val detailRecipeId: Int,
    val image: String,
    val original: String,
    val amount: Double,
    val unit: String
)


