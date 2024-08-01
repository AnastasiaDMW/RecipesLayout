package ru.itis.recipeslayout.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.itis.recipeslayout.Constant.RECIPE

@Entity(tableName = RECIPE)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val image: String
)
