package com.example.recipe_app.viewmodels

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
            onSuccess = { _savedRecipes.value = it },   // On success, update _savedRecipes with the loaded recipes
            onFailure = { _saveStatus.value = false }        // On failure, set _saveStatus to false

        )
    }
}