package com.example.recipe_app.repository

import androidx.lifecycle.LiveData
import com.example.recipe_app.model.IngredientSub
import com.example.recipe_app.model.Recipe
import retrofit2.http.Query

interface IRecipeRepository {
    suspend fun fetchRecipesFromApi(tags: String?): List<Recipe>
    suspend fun searchRecipesFromApi(query: String): List<Recipe>
    suspend fun fetchSubstitutes(ingredient: String): List<IngredientSub>
    suspend fun saveRecipe(recipe: Recipe)
    suspend fun saveRecipesToFirebase(recipes: List<Recipe>)
    fun getSavedRecipes(): LiveData<List<Recipe>>
    fun getFilteredRecipes(cuisineType: String, mealType: String): LiveData<List<Recipe>>
    fun getRecipesFromFirebase(): LiveData<List<Recipe>>
    suspend fun fetchRecipesFromFirebase(cuisine: String?, mealType: String?): List<Recipe>
}