package ru.itis.recipeslayout.repository

import ru.itis.recipeslayout.model.entity.Recipe
import ru.itis.recipeslayout.model.response.DetailRecipeApiResponse

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe>

    suspend fun getRecipeInfo(recipeId: Int): DetailRecipeApiResponse
}