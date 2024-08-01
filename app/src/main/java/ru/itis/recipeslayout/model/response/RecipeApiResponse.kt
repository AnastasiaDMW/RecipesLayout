package ru.itis.recipeslayout.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    @SerialName("results")
    val recipes: List<RecipeApiResponse>
)

@Serializable
data class RecipeApiResponse(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("image") val image: String,
)