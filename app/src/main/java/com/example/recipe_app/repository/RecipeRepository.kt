package com.example.recipe_app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipe_app.Constants
import com.example.recipe_app.network.RetrofitClient
import com.example.recipe_app.utils.mapToLocalRecipe
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.dao.RecipeDao
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class RecipeRepository(private val recipeDao: RecipeDao) : IRecipeRepository {

    // Fetch recipes from the API
    override suspend fun fetchRecipesFromApi(tags: String?): List<Recipe> {
        try {
            //val tagsString = tags?.joinToString(",")
            val apiResponse = RetrofitClient.api.getRandomRecipes(
                apiKey = Constants.API_KEY,
                tags = tags
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
            Log.d("RecipeRepository", "Filter params - cuisineType: $cuisineType, mealType: $mealType")
            recipeDao.getSavedRecipes()
        } else {
            Log.d("RecipeRepository", "Filter params - cuisineType: $cuisineType, mealType: $mealType")
            recipeDao.getRecipesByFilter(cuisineType, mealType)
        }
    }

    override suspend fun fetchRecipesFromFirebase(cuisine: String?, mealType: String?): List<Recipe> {
        val firestore = FirebaseFirestore.getInstance()
        val recipesList = mutableListOf<Recipe>()

        // Start the query for the "recipes" collection
        var query = firestore.collection("recipes")

        // Apply filters only if cuisine or mealType is not "All"
        if (cuisine != null && cuisine != "All") {
            query = query.whereEqualTo("cuisine", cuisine) as CollectionReference
        }

        if (mealType != null && mealType != "All") {
            query = query.whereEqualTo("mealType", mealType) as CollectionReference
        }

        // Get the query results
        val querySnapshot = query.get().await()

        // Add fetched recipes to the list
        for (document in querySnapshot.documents) {
            val recipe = document.toObject(Recipe::class.java)
            recipe?.let { recipesList.add(it) }
        }

        return recipesList
    }



    // Save recipe locally
    override suspend fun saveRecipe(recipe: Recipe) {
        recipe.cuisine?.let { Log.d("saving recipe...", it) }
        recipeDao.insertRecipe(recipe)
    }

     override suspend fun saveRecipesToFirebase(recipes: List<Recipe>) {
        val firestore = FirebaseFirestore.getInstance()

        for (recipe in recipes) {
            val recipeRef = firestore.collection("recipes").document(recipe.id.toString())  // Use a unique document ID

            recipeRef.set(recipe)
                .addOnSuccessListener {
                    Log.d("Firebase", "Recipe added successfully!")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Error adding recipe", e)
                }
        }
    }

    // Get saved recipes from the database
    override fun getSavedRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getSavedRecipes()
    }

    override fun getRecipesFromFirebase(): LiveData<List<Recipe>> {
        val recipesLiveData = MutableLiveData<List<Recipe>>()

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("recipes")
            .get()
            .addOnSuccessListener { documents ->
                val recipesList = mutableListOf<Recipe>()
                for (document in documents) {
                    val recipe = document.toObject(Recipe::class.java)
                    recipe?.let {
                        recipesList.add(it)
                    }
                }
                // Post the list of recipes to LiveData
                recipesLiveData.postValue(recipesList)
                Log.d("Firebase", "Recipes fetched: $recipesList")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error fetching recipes", e)
                // Optionally, post an empty list or handle the failure
                recipesLiveData.postValue(emptyList())
            }

        return recipesLiveData
    }

}
