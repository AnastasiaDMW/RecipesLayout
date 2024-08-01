package ru.itis.recipeslayout.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.itis.recipeslayout.dao.DetailRecipeDao
import ru.itis.recipeslayout.dao.ExtendedIngredientDao
import ru.itis.recipeslayout.dao.RecipeDao
import ru.itis.recipeslayout.model.entity.DetailRecipeEntity
import ru.itis.recipeslayout.model.entity.ExtendedIngredientEntity
import ru.itis.recipeslayout.model.entity.Recipe
import ru.itis.recipeslayout.model.response.DetailRecipeApiResponse
import ru.itis.recipeslayout.model.response.ExtendedIngredientApiResponse

class OfflineRecipeRepository(
    private val recipeDao: RecipeDao,
    private val detailRecipeDao: DetailRecipeDao,
    private val extendedIngredientDao: ExtendedIngredientDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RecipeRepository {

    override suspend fun getRecipes(): List<Recipe> {
        return withContext(ioDispatcher) {
            recipeDao.getRecipes().first()
        }
    }

    suspend fun insertRecipe(recipe: Recipe) {
        withContext(ioDispatcher) {
            recipeDao.insertRecipe(recipe)
        }
    }

    suspend fun insertDetailRecipe(detailRecipeApiResponse: DetailRecipeApiResponse) {
        withContext(ioDispatcher) {
            detailRecipeDao.insertDetailRecipe(detailRecipeApiResponse.toDetailRecipeEntity())
        }
    }

    suspend fun insertExtendedInstructions(detailRecipeApiResponse: DetailRecipeApiResponse) {
        withContext(ioDispatcher) {
            for (expandedInstruction in detailRecipeApiResponse.extendedIngredientApiResponses) {
                extendedIngredientDao.insertExtendedIngredient(
                    ExtendedIngredientEntity(
                        id = expandedInstruction.id,
                        original = expandedInstruction.original,
                        image = expandedInstruction.image,
                        unit = expandedInstruction.unit,
                        amount = expandedInstruction.amount,
                        detailRecipeId = detailRecipeApiResponse.id
                    )
                )
            }
        }
    }

    override suspend fun getRecipeInfo(recipeId: Int): DetailRecipeApiResponse {
        val recipeInfo = detailRecipeDao.getDetailRecipes().first().filter { it.id == recipeId }
        if (recipeInfo.isEmpty()) {
            return DetailRecipeApiResponse(id = 0)
        }
        val extendedIngredient = extendedIngredientDao.getExtendedIngredient().first().filter { it.detailRecipeId == recipeInfo[0].id }

        return DetailRecipeApiResponse(
            id = recipeInfo[0].id,
            vegan = recipeInfo[0].vegan,
            glutenFree = recipeInfo[0].glutenFree,
            cookingMinutes = recipeInfo[0].cookingMinutes,
            healthScore = recipeInfo[0].healthScore,
            instructions = recipeInfo[0].instructions,
            title = recipeInfo[0].title,
            image = recipeInfo[0].image,
            servings = recipeInfo[0].servings,
            extendedIngredientApiResponses = extendedIngredient.map { it.toExtendedIngredientApiResponse() }
        )
    }

    private fun ExtendedIngredientEntity.toExtendedIngredientApiResponse(): ExtendedIngredientApiResponse =
        ExtendedIngredientApiResponse(
            id = id,
            image = image,
            original = original,
            amount = amount,
            unit = unit
        )

    private fun DetailRecipeApiResponse.toDetailRecipeEntity(): DetailRecipeEntity =
        DetailRecipeEntity(
            id = id,
            vegan = vegan,
            glutenFree = glutenFree,
            cookingMinutes = cookingMinutes,
            healthScore = healthScore,
            instructions = instructions,
            title = title,
            image = image,
            servings = servings
        )
}