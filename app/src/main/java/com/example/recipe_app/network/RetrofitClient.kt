package com.example.recipe_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.recipe_app.EdamamApiService

object RetrofitClient {
    private const val BASE_URL = "https://api.spoonacular.com/"

    val api: SpoonacularApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularApi::class.java)
    }
}
object EdamamRetrofitClient {
    private const val BASE_URL = "https://api.edamam.com/"

    val api: EdamamApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EdamamApiService::class.java)
    }
}