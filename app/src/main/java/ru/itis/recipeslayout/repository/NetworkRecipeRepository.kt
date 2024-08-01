package ru.itis.recipeslayout.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.recipeslayout.model.entity.Recipe
import ru.itis.recipeslayout.model.response.DetailRecipeApiResponse
import ru.itis.recipeslayout.model.response.RecipeApiResponse
import ru.itis.recipeslayout.network.RecipeApiService

class NetworkRecipeRepository(
    private val recipeApiService: RecipeApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RecipeRepository {
    override suspend fun getRecipes(): List<Recipe> {
        return withContext(ioDispatcher) {
            val recipeApiResponses = recipeApiService.getRecipes().recipes
            recipeApiResponses.map { it.toRecipe() }
        }

    }
    override suspend fun getRecipeInfo(recipeId: Int): DetailRecipeApiResponse {
        return withContext(ioDispatcher) {
            recipeApiService.getRecipeInfo(recipeId)
        }
    }

    private fun RecipeApiResponse.toRecipe(): Recipe =
        Recipe(
            id = id,
            title = title,
            image = image
        )
}