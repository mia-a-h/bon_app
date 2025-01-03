package com.example.recipe_app

import com.example.recipe_app.model.MealPlanRequest
import com.example.recipe_app.model.MealPlanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface EdamamApiService {
    @POST("api/meal-planner/v1/plan")
    @Headers("Content-Type: application/json")
    suspend fun getMealPlan(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Body request: MealPlanRequest
    ): Response<MealPlanResponse>
}