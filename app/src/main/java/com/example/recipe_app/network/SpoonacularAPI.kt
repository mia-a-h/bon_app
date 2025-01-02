package com.example.recipe_app.network

import com.example.recipe_app.model.SpoonacularRecipeResponse
import com.example.recipe_app.model.SubstituteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        //@Query("addRecipeInformation") recipeInfo: Boolean = true,
        //@Query("addRecipeInstructions") instructions: Boolean = true,
        //@Query("addRecipeNutrition") nutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): SpoonacularResponse

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("include-tags") tags: String?,
        @Query("number") number: Int = 10,
        @Query("includeNutrition") nutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RandomRecipeResponse

    @GET("food/ingredients/substitutes")
    suspend fun getIngredientSubs(
        @Query("ingredientName") ingredient: String,
        @Query("apiKey") apiKey: String
    ): IngredientSubstituteResponse
}

data class SpoonacularResponse(
    val results: List<SpoonacularRecipeResponse> // Match the response structure
)

data class RandomRecipeResponse(
    val recipes: List<SpoonacularRecipeResponse> // Match the structure for random recipes
)

data class IngredientSubstituteResponse(
    val substitutes: List<SubstituteResponse>
)
