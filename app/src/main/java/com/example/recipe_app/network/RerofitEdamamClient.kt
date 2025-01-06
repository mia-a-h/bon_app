package com.example.recipe_app.network

import com.example.recipe_app.Constants
import com.example.recipe_app.model.MealPlanRequest
import com.example.recipe_app.model.MealPlanResponse
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.utils.mapToLocalRecipe
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

class RerofitEdamamClient {


    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs request and response body
    }

    val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add the interceptor to the OkHttpClient
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.edamam.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    val edamamApiService = retrofit.create(EdamamApiService::class.java)

    // Assuming you have a Retrofit service set up
    suspend fun fetchMealPlan(request: MealPlanRequest): MealPlanResponse? {
        val response = edamamApiService.getMealPlan(
            appId = Constants.EDAMAM_APP_KEY,
            appKey = Constants.EDAMAM_API_KEY,
            type= listOf("public"),
            request = request
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null;
        }
    }



}