package ru.itis.recipeslayout

import android.app.Application
import ru.itis.recipeslayout.data.AppContainer
import ru.itis.recipeslayout.data.DefaultAppContainer
import ru.itis.recipeslayout.database.RecipeDatabase

class RecipeApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        val recipeDao = RecipeDatabase.getRecipeDao(this)
        val detailRecipeDao = RecipeDatabase.getDetailRecipeDao(this)
        val extendedIngredientDao = RecipeDatabase.getExtendedIngredientDao(this)
        container = DefaultAppContainer(recipeDao, detailRecipeDao, extendedIngredientDao)
    }
}