package com.example.recipe_app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.repository.IRecipeRepository
import com.google.firebase.firestore.FirebaseFirestore
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

    fun fetchRecommendedRecipes(tags: String?) {
        viewModelScope.launch {
            // Fetch recommended recipes from the API
            val recipes = repository.fetchRecipesFromApi(tags)
            _recommendedRecipes.postValue(recipes)
            Log.d("Recipes", "Fetched recommended recipes: $recipes")
            recipes.forEach { saveRecipe(it) }
            saveRecipesFirebase(recipes)
        }
    }

    fun fetchPopularRecipes(tags: String?) {
        viewModelScope.launch {
            // Fetch popular recipes from your API (use different query or API endpoint)
            val recipes = repository.fetchRecipesFromApi(tags)
            _popularRecipes.postValue(recipes)
            Log.d("Recipes", "Fetched popular recipes: $recipes")
            recipes.forEach { saveRecipe(it) }
            saveRecipesFirebase(recipes)
        }
    }

    fun fetchFilteredRecipes(cuisineType: String, mealType: String) {
        viewModelScope.launch {
            val filteredRecipes = repository.getFilteredRecipes(cuisineType, mealType)
            Log.d("view model function", filteredRecipes.toString())
            Log.d("view model function", "$cuisineType $mealType")
            _filteredRecipes.postValue(filteredRecipes.value ?: emptyList())
        }
    }

    fun fetchFilteredRecipesFireBase(cuisine: String?, mealType: String?) {
        viewModelScope.launch {
            val recipes = repository.fetchRecipesFromFirebase(cuisine, mealType)
            _filteredRecipes.postValue(recipes)
        }
    }

    fun searchRecipes(query: String){
        viewModelScope.launch {
            val recipes = repository.searchRecipesFromApi(query)
            _searchedRecipes.postValue(recipes)
        }
    }

    private fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.saveRecipe(recipe)
        }
    }

    fun getSavedRecipes(): LiveData<List<Recipe>> {
        return repository.getSavedRecipes()
    }

    fun saveRecipesFirebase(recipes: List<Recipe>){
        viewModelScope.launch {
            repository.saveRecipesToFirebase(recipes)
        }
    }


}
