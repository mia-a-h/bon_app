package com.example.recipe_app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.recipe_app.Constants
import com.example.recipe_app.model.RetrofitClient
import com.example.recipe_app.mapToLocalRecipe
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.RecipeDao

class RecipeRepository(private val recipeDao: RecipeDao) : IRecipeRepository {

    // Fetch recipes from the API
    override suspend fun fetchRecipesFromApi(): List<Recipe> {
        try {
            val apiResponse = RetrofitClient.api.getRandomRecipes(
                apiKey = Constants.API_KEY
            )
            return apiResponse.recipes.map { mapToLocalRecipe(it) } // Map API data to local Recipe class
        } catch (e: Exception){
            Log.e("RecipeRepository", "Error fetching recipes: ${e.message}")
            return emptyList()
        }
    }

    // Search for recipes from API
    override suspend fun searchRecipesFromApi(query: String): List<Recipe>{
        try {
            val apiResponse = RetrofitClient.api.searchRecipes(
                query = query,
//                cuisine = cuisineType ?:"",
//                type = mealType ?:"",
                apiKey = Constants.API_KEY
            )
            return apiResponse.results.map { mapToLocalRecipe(it) }
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error fetching recipes: ${e.message}")
            return emptyList()
        }
    }

    override fun getFilteredRecipes(cuisineType: String, mealType: String): LiveData<List<Recipe>>{
        return if (cuisineType == "All" && mealType == "All") {
            recipeDao.getSavedRecipes()
        } else {
            recipeDao.getRecipesByFilter(cuisineType, mealType)
        }
    }

    // Save recipe locally
    override suspend fun saveRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    // Get saved recipes from the database
    override fun getSavedRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getSavedRecipes()
    }
}
