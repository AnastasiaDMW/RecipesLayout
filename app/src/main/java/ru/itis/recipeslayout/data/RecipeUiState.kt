package ru.itis.recipeslayout.data

import ru.itis.recipeslayout.model.entity.Recipe

sealed interface RecipeUiState {
    data class Success(val data: List<Recipe>): RecipeUiState
    object Error: RecipeUiState
    object Loading: RecipeUiState
}