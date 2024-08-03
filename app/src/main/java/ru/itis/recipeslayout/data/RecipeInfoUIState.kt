package ru.itis.recipeslayout.data

import ru.itis.recipeslayout.model.response.DetailRecipeApiResponse

sealed interface RecipeInfoUIState {
    data class Success(val data: DetailRecipeApiResponse): RecipeInfoUIState
    object Error: RecipeInfoUIState
    object Loading: RecipeInfoUIState
}