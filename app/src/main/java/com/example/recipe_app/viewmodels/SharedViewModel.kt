package com.example.recipe_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipe_app.model.Recipe

class SharedRecipeViewModel : ViewModel() {
    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe> get() = _selectedRecipe

    fun selectRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
    }
}
