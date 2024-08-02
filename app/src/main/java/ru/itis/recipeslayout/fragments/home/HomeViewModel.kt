package ru.itis.recipeslayout.fragments.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.recipeslayout.RecipeApplication
import ru.itis.recipeslayout.data.DefaultAppContainer
import ru.itis.recipeslayout.data.RecipeUiState
import ru.itis.recipeslayout.database.RecipeDatabase
import ru.itis.recipeslayout.model.entity.Recipe
import ru.itis.recipeslayout.repository.NetworkRecipeRepository
import ru.itis.recipeslayout.repository.OfflineRecipeRepository
import java.io.IOException

class HomeViewModel(
    private val application: Application,
    private val networkRecipeRepository: NetworkRecipeRepository,
    private val offlineRecipeRepository: OfflineRecipeRepository
): ViewModel() {

    private val _recipeUiState = MutableLiveData<RecipeUiState>()
    val recipeUiState: LiveData<RecipeUiState> = _recipeUiState

    init {
        getRecipes()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun getRecipes() {
        viewModelScope.launch {
            _recipeUiState.value = RecipeUiState.Loading
            Log.d("DATA", "Загрузка")
            val recipesFromDb = offlineRecipeRepository.getRecipes()
            Log.d("DATA", "Данные из БД: $recipesFromDb")
            try {
                if (isNetworkAvailable(application)) {
                    val recipesFromApi = networkRecipeRepository.getRecipes()
                    Log.d("DATA", "Данные API: $recipesFromApi")
                    if (recipesFromDb.isEmpty()) {
                        Log.d("DATA", "В БД ничего нет")
                        for (recipe in recipesFromApi) {
                            offlineRecipeRepository.insertRecipe(recipe)
                        }
                        _recipeUiState.value = RecipeUiState.Success(recipesFromApi)
                    }
                    else if (recipesFromDb.size < recipesFromApi.size) {
                        for (recipeApi in recipesFromApi) {
                            for (recipeDB in recipesFromDb) {
                                if (recipeApi.id != recipeDB.id) {
                                    offlineRecipeRepository.insertRecipe(recipeApi)
                                }
                            }
                        }
                    }
                    else {
                        _recipeUiState.value = RecipeUiState.Success(recipesFromDb)
                    }
                } else {
                    _recipeUiState.value = RecipeUiState.Success(recipesFromDb)
                }
            } catch (e: IOException) {
                Log.d("DATA", "IOException")
                _recipeUiState.value = RecipeUiState.Error
            } catch (e: HttpException) {
                Log.d("DATA", "HttpException")
                _recipeUiState.value = RecipeUiState.Error
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
                HomeViewModel(
                    application,
                    appContainer.networkRecipeRepository,
                    appContainer.offlineRecipeRepository
                )
            }
        }
    }
}