package ru.itis.recipeslayout.fragments.detail

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.recipeslayout.RecipeApplication
import ru.itis.recipeslayout.data.DefaultAppContainer
import ru.itis.recipeslayout.data.RecipeInfoUIState
import ru.itis.recipeslayout.database.RecipeDatabase
import ru.itis.recipeslayout.repository.NetworkRecipeRepository
import ru.itis.recipeslayout.repository.OfflineRecipeRepository
import java.io.IOException

class DetailViewModel(
    private val application: Application,
    private val networkRecipeRepository: NetworkRecipeRepository,
    private val offlineRecipeRepository: OfflineRecipeRepository
): ViewModel() {

    var recipeId: Int = 0

    private var _detailRecipeUiState = MutableLiveData<RecipeInfoUIState>()
    val detailRecipeUiState: LiveData<RecipeInfoUIState> = _detailRecipeUiState

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun getRecipeInfo(recipeId: Int) {
        viewModelScope.launch {
            _detailRecipeUiState.value = RecipeInfoUIState.Loading
            val recipeFromDb = offlineRecipeRepository.getRecipeInfo(recipeId)
            try {
                if (isNetworkAvailable(application)) {
                    val recipesFromApi = networkRecipeRepository.getRecipeInfo(recipeId)
                    if (recipeFromDb.id != recipeId) {
                        offlineRecipeRepository.insertDetailRecipe(recipesFromApi)
                        offlineRecipeRepository.insertExtendedInstructions(recipesFromApi)

                        _detailRecipeUiState.value = RecipeInfoUIState.Success(recipesFromApi)
                    }
                    else {
                        _detailRecipeUiState.value = RecipeInfoUIState.Success(recipeFromDb)
                    }
                } else {
                    _detailRecipeUiState.value = RecipeInfoUIState.Success(recipeFromDb)
                }
            } catch (e: IOException) {
                RecipeInfoUIState.Error
            } catch (e: HttpException) {
                RecipeInfoUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
                val recipeDao = RecipeDatabase.getRecipeDao(application.applicationContext)
                val detailRecipeDao = RecipeDatabase.getDetailRecipeDao(application.applicationContext)
                val extendedIngredientDao = RecipeDatabase.getExtendedIngredientDao(application.applicationContext)
                val appContainer = DefaultAppContainer(recipeDao, detailRecipeDao, extendedIngredientDao)
                DetailViewModel(
                    application,
                    appContainer.networkRecipeRepository,
                    appContainer.offlineRecipeRepository
                )
            }
        }
    }
}