package ru.itis.recipeslayout.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import ru.itis.recipeslayout.Constant.BASE_URL
import okhttp3.MediaType.Companion.toMediaType
import ru.itis.recipeslayout.dao.DetailRecipeDao
import ru.itis.recipeslayout.dao.ExtendedIngredientDao
import ru.itis.recipeslayout.dao.RecipeDao
import ru.itis.recipeslayout.network.RecipeApiService
import ru.itis.recipeslayout.repository.NetworkRecipeRepository
import ru.itis.recipeslayout.repository.OfflineRecipeRepository
import ru.itis.recipeslayout.repository.RecipeRepository

interface AppContainer {
    val networkRecipeRepository: RecipeRepository
    val offlineRecipeRepository: RecipeRepository
}

class DefaultAppContainer(
    private val recipeDao: RecipeDao,
    private val detailRecipeDao: DetailRecipeDao,
    private val extendedIngredientDao: ExtendedIngredientDao
): AppContainer {

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(
            Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType())
        )
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }

    override val networkRecipeRepository: NetworkRecipeRepository by lazy {
        NetworkRecipeRepository(retrofitService)
    }

    override val offlineRecipeRepository: OfflineRecipeRepository by lazy {
        OfflineRecipeRepository(recipeDao, detailRecipeDao, extendedIngredientDao)
    }
}