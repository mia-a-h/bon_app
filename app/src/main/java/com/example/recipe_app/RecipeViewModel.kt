package com.example.recipe_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.repository.IRecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: IRecipeRepository) : ViewModel() {

    private val _recommendedRecipes = MutableLiveData<List<Recipe>>()
    val recommendedRecipes: LiveData<List<Recipe>> get() = _recommendedRecipes

    private val _popularRecipes = MutableLiveData<List<Recipe>>()
    val popularRecipes: LiveData<List<Recipe>> get() = _popularRecipes

    private val _filteredRecipes = MutableLiveData<List<Recipe>>()
    val filteredRecipes: LiveData<List<Recipe>> get() = _filteredRecipes

    private val _searchedRecipes = MutableLiveData<List<Recipe>>()
    val searchedRecipes: LiveData<List<Recipe>> get() = _searchedRecipes

    fun fetchRecommendedRecipes() {
        viewModelScope.launch {
            // Fetch recommended recipes from your API (use different query or API endpoint)
            val recipes = repository.fetchRecipesFromApi()
            _recommendedRecipes.postValue(recipes)
            recipes.forEach { saveRecipe(it) }
        }
    }

    fun fetchPopularRecipes() {
        viewModelScope.launch {
            // Fetch popular recipes from your API (use different query or API endpoint)
            val recipes = repository.fetchRecipesFromApi()
            _popularRecipes.postValue(recipes)
            recipes.forEach { saveRecipe(it) }
        }
    }

    fun fetchFilteredRecipes(cuisineType: String, mealType: String) {
        viewModelScope.launch {
            val filteredRecipes = repository.getFilteredRecipes(cuisineType, mealType)
            _filteredRecipes.postValue(filteredRecipes.value ?: emptyList())
        }
    }

    fun searchRecipes(query: String){
        viewModelScope.launch {
            val recipes = repository.searchRecipesFromApi(query)
            _searchedRecipes.postValue(recipes)
        }
    }

    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.saveRecipe(recipe)
        }
    }

    fun getSavedRecipes(): LiveData<List<Recipe>> {
        return repository.getSavedRecipes()
    }
}
