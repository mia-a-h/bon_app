package com.example.recipe_app.model

import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
//        @Query("cuisine") cuisine: String?,
//        @Query("type") type: String?,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): SpoonacularResponse

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): RandomRecipeResponse
}

data class SpoonacularResponse(
    val results: List<SpoonacularRecipeResponse> // Match the response structure
)

data class RandomRecipeResponse(
    val recipes: List<SpoonacularRecipeResponse> // Match the structure for random recipes
)
