package com.example.recipe_app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.ui.services.RecipeSaveService

class SavedRecipesViewModel : ViewModel() {
    // Instance of my service that handles Firebase operations
    private val recipeSaveService = RecipeSaveService()

    // MutableLiveData for internal modifications of save status (success/failure)
    private val _saveStatus = MutableLiveData<Boolean>()

    // Public LiveData that fragments observe to know if save was successful
    val saveStatus: LiveData<Boolean> = _saveStatus

    // MutableLiveData for internal list of saved recipes
    private val _savedRecipes = MutableLiveData<List<Recipe>>()

    // Public LiveData that fragments observe to display saved recipes
    val savedRecipes: LiveData<List<Recipe>> = _savedRecipes
    // Function to save a recipe

    private val _selectedRecipe = MutableLiveData<Recipe?>()
    val selectedRecipe: LiveData<Recipe?> get() = _selectedRecipe

    fun saveRecipe(recipe: Recipe) {
        recipeSaveService.saveRecipe(
            recipe,
            onSuccess = { _saveStatus.value = true },   // On successful save, set _saveStatus to true
            onFailure = { _saveStatus.value = false }        // On failure, set _saveStatus to false
        )
    }

    // Function to load all saved recipes
    fun loadSavedRecipes() {
        recipeSaveService.getSavedRecipes(
            onSuccess = { recipes ->
                Log.d("SavedRecipesViewModel", "Fetched recipes: ${recipes.size} recipes")
                _savedRecipes.value = recipes // Update LiveData
                println("SAVED RECIPLES");
                println(recipes.size);
            },
            onFailure = {
                Log.e("SavedRecipesViewModel", "Failed to fetch saved recipes")
                _savedRecipes.value = emptyList() // Handle failure gracefully
            }
        )
    }


    fun getRecipeById(recipeId: String) {
        recipeSaveService.getRecipeById(
            recipeId = recipeId,
            onSuccess = { recipe ->
                _selectedRecipe.value = recipe // Update LiveData with the fetched recipe
            },
            onFailure = { exception ->
                Log.e("SavedRecipesViewModel", "Failed to fetch recipe by ID: ${exception.message}")
                _selectedRecipe.value = null // Handle failure
            }
        )
    }








}