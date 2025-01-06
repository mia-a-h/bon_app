package com.example.recipe_app.network

import EdamamRecipe
import com.example.recipe_app.model.MealPlanRequest
import com.example.recipe_app.model.MealPlanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EdamamApiService {
    @POST("api/meal-planner/v1/{app_id}/select")
    @Headers("Content-Type: application/json")
    suspend fun getMealPlan(
        @Path("app_id") appId: String,                      // Path parameter for app ID
        @Query("app_key") appKey: String,                   // Query parameter for app key
        @Query("type") type: List<String>,                  // Required query parameter for recipe types
        @Body request: MealPlanRequest                      // Request body
    ): Response<MealPlanResponse>

    @GET("api/recipes/v2/{id}")
    suspend fun getRecipeDetails(
        @Path("id") id: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Response<EdamamRecipe>
}
