package com.example.recipe_app.repository

import androidx.lifecycle.LiveData
import com.example.recipe_app.model.Recipe
import retrofit2.http.Query

interface IRecipeRepository {
    suspend fun fetchRecipesFromApi(): List<Recipe>
    suspend fun searchRecipesFromApi(query: String): List<Recipe>
    suspend fun saveRecipe(recipe: Recipe)
    fun getSavedRecipes(): LiveData<List<Recipe>>
    fun getFilteredRecipes(cuisineType: String, mealType: String): LiveData<List<Recipe>>
}