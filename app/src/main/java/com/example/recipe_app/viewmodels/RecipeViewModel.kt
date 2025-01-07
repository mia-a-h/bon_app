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

var staticTagR: String? = "all, all"
var staticTagP: String? = "all, all"

class RecipeViewModel(private val repository: IRecipeRepository) : ViewModel() {

    private val _recommendedRecipes = MutableLiveData<List<Recipe>>()
    val recommendedRecipes: LiveData<List<Recipe>> get() = _recommendedRecipes

    private val _popularRecipes = MutableLiveData<List<Recipe>>()
    val popularRecipes: LiveData<List<Recipe>> get() = _popularRecipes

//    private val _filteredRecipes = MutableLiveData<List<Recipe>>()
//    val filteredRecipes: LiveData<List<Recipe>> get() = _filteredRecipes

    private val _searchedRecipes = MutableLiveData<List<Recipe>>()
    val searchedRecipes: LiveData<List<Recipe>> get() = _searchedRecipes

    fun fetchRecommendedRecipes(tags: String?) {
        //normalize "All" filter to ensure consistent handling
        val normalizedTags = if (tags == "all, all") null else tags

        if (_recommendedRecipes.value.isNullOrEmpty() || staticTagR != normalizedTags) {
            viewModelScope.launch {
                try {
                    // Fetch recipes based on tags (null for "All")
                    val recipes = repository.fetchRecipesFromApi(normalizedTags)
                    _recommendedRecipes.postValue(recipes)
                    // Log to monitor changes and fetch status
                    Log.d("Recipes", "Fetched recommended recipes: $recipes")
                    staticTagR = normalizedTags
                    Log.d("Repo", "Static tag updated to: $staticTagR")
                    //recipes.forEach { saveRecipe(it) }
                    //saveRecipesFirebase(recipes)
                } catch (e: Exception) {
                    Log.e("FetchError", "Error fetching recommended recipes: ${e.message}")
                }
            }
        } else {
            Log.d("Recipes", "Recommended recipes already loaded. Static tag: $staticTagR")
        }
    }


    fun fetchPopularRecipes(tags: String?) {

        val normalizedTags = if (tags == "all, all") null else tags

        if (_popularRecipes.value.isNullOrEmpty() || staticTagP != normalizedTags) {
            viewModelScope.launch {
                try {
                    val recipes = repository.fetchRecipesFromApi(tags)
                    _popularRecipes.postValue(recipes)
                    Log.d("Recipes", "Fetched popular recipes: $recipes")
                    staticTagP = normalizedTags
                    Log.d("Repo", "Static tag updated to: $staticTagP")
                    //recipes.forEach { saveRecipe(it) }
                    //saveRecipesFirebase(recipes)
                } catch (e: Exception) {
                    Log.e("FetchError", "Error fetching recommended recipes: ${e.message}")
                }
            }
        } else {
            Log.d("Recipes", "Popular recipes already loaded. Static tag: $staticTagP")
        }
    }

//    fun fetchFilteredRecipes(cuisineType: String, mealType: String) {
//        viewModelScope.launch {
//            val filteredRecipes = repository.getFilteredRecipes(cuisineType, mealType)
//            Log.d("view model function", filteredRecipes.toString())
//            Log.d("view model function", "$cuisineType $mealType")
//            _filteredRecipes.postValue(filteredRecipes.value ?: emptyList())
//        }
//    }
//
//    fun fetchFilteredRecipesFireBase(cuisine: String?, mealType: String?) {
//        viewModelScope.launch {
//            val recipes = repository.fetchRecipesFromFirebase(cuisine, mealType)
//            _filteredRecipes.postValue(recipes)
//        }
//    }

    fun searchRecipes(query: String){
        viewModelScope.launch {
            val recipes = repository.searchRecipesFromApi(query)
            _searchedRecipes.postValue(recipes)
        }
    }

//    private fun saveRecipe(recipe: Recipe) {
//        viewModelScope.launch {
//            repository.saveRecipe(recipe)
//        }
//    }

    fun getSavedRecipes(): LiveData<List<Recipe>> {
        return repository.getSavedRecipes()
    }

    fun saveRecipesFirebase(recipes: List<Recipe>){
        viewModelScope.launch {
            repository.saveRecipesToFirebase(recipes)
        }
    }

    private val _substitutes = MutableLiveData<Map<String, List<String>>>()
    val substitutes: LiveData<Map<String, List<String>>> get() = _substitutes

    private val substitutesCache = mutableMapOf<String, List<String>>()

    fun fetchSubstitutes(ingredient: String)  {
        viewModelScope.launch {
            if (!substitutesCache.containsKey(ingredient)) {
                try {
                    // Fetch the list of IngredientSub objects from the repository
                    val ingredientSub = repository.fetchSubstitutes(ingredient)

                    // Process the substitute and update the cache
                    ingredientSub?.substitutes?.forEach { _ ->
                        Log.d("RecipeViewModel", "Before Update: $substitutesCache")
                        substitutesCache[ingredient] = ingredientSub.substitutes
                        Log.d("RecipeViewModel", "After Update: $substitutesCache")
                    }

                    // Post the updated cache to the LiveData
                    _substitutes.postValue(substitutesCache)
                } catch (e: Exception) {
                    Log.e("RecipeViewModel", "Failed to fetch substitutes: ${e.message}")
                }
            }
            else{
                _substitutes.postValue(substitutesCache)
            }
        }
    }

    private val _joke = MutableLiveData<String?>()
    val joke: LiveData<String?> get() = _joke

    fun fetchJoke(){
        viewModelScope.launch {
            try{
                val joke = repository.fetchJoke()
                _joke.postValue(joke)
            } catch (e: Exception){
                Log.e("RecipeViewModel", "Failed to fetch joke: ${e.message}")
            }
        }
    }

    private val _trivia = MutableLiveData<String?>()
    val trivia: LiveData<String?> get() = _trivia

    fun fetchTrivia(){
        viewModelScope.launch {
            try{
                val trivia = repository.fetchTrivia()
                _trivia.postValue(trivia)
            } catch (e: Exception){
                Log.e("RecipeViewModel", "Failed to fetch trivia: ${e.message}")
            }
        }
    }
}
