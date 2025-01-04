package com.example.recipe_app.network

import com.example.recipe_app.model.SpoonacularRecipeResponse
import com.example.recipe_app.model.SpoonacularSearchResponse
import com.example.recipe_app.model.SubstituteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 1,
        @Query("addRecipeInformation") recipeInfo: Boolean = true,
        @Query("addRecipeInstructions") instructions: Boolean = true,
        @Query("addRecipeNutrition") nutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): SpoonacularResponse

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("include-tags") tags: String?,
        @Query("number") number: Int = 1,
        @Query("includeNutrition") nutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RandomRecipeResponse

    @GET("food/ingredients/substitutes")
    suspend fun getIngredientSubs(
        @Query("ingredientName") ingredient: String,
        @Query("apiKey") apiKey: String
    ): IngSubstituteResponse

    @GET("food/trivia/random")
    suspend fun getRandomTrivia(
        @Query("apiKey") apiKey: String
    ): TriviaResponse

    @GET("food/jokes/random")
    suspend fun getRandomJoke(
        @Query("apiKey") apiKey: String
    ): JokeResponse
}

data class SpoonacularResponse(
    val results: List<SpoonacularSearchResponse> //Match the response structure
)

data class RandomRecipeResponse(
    val recipes: List<SpoonacularRecipeResponse> //Match the structure for random recipes
)

data class IngSubstituteResponse(
    val ingredient: String,
    val substitutes: List<String>,
    val message: String
)

data class TriviaResponse(
    val text: String
)

data class JokeResponse(
    val text: String
)