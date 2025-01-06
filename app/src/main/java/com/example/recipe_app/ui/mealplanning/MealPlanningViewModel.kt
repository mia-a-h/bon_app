//package com.example.recipe_app.ui.mealplanning
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.recipe_app.network.EdamamApiService
//import com.example.recipe_app.model.MealPlanRequest
//import com.example.recipe_app.model.MealPlanResponse
//import com.example.recipe_app.model.Plan
//import com.example.recipe_app.model.Recipe
//import com.example.recipe_app.network.EdamamRetrofitClient
//import kotlinx.coroutines.launch
//
//class MealPlanningViewModel : ViewModel() {
//    private val edamamApiService: EdamamApiService = EdamamRetrofitClient.api
//        private val appId = "app_id"
//    private val appKey = "app_key"
//
//    private val _meals = MutableLiveData<List<Recipe>>()
//    val meals: LiveData<List<Recipe>> = _meals
//    fun loadMealsForDay(dayIndex: Int) {
//        viewModelScope.launch {
//            try {
//                // For now, just filter the meals for the selected day
//                // Later, you might want to load from a database or make an API call
//                val currentMeals = _meals.value ?: emptyList()
//                // Example filtering logic - adjust based on your needs
//                val filteredMeals = currentMeals.filter { recipe ->
//                    // Add your day-specific filtering logic here
//                    true // Currently showing all meals
//                }
//                _meals.value = filteredMeals
//            } catch (e: Exception) {
//                _error.value = "Error loading meals: ${e.message}"
//            }
//        }
//    }
//
//
//
//    private val _planningStatus = MutableLiveData<Boolean>()
//    val planningStatus: LiveData<Boolean> = _planningStatus
//
//    private val _error = MutableLiveData<String>()
//    val error: LiveData<String> = _error
//
//    fun createMealPlan(request: MealPlanRequest) {
//        viewModelScope.launch {  // Launches coroutine in ViewModel scope
//            try {
//                _planningStatus.value = true  // Show loading indicator
//
//                // Make API call to get meal plan
//                val response = edamamApiService.getMealPlan(appId, appKey, request)
//
//                if (response.isSuccessful) {
//                    response.body()?.let { mealPlanResponse ->
//                        // Convert response to basic meal list
//                        val basicMeals = processMealPlan(mealPlanResponse)
//
//                        // For each basic meal, fetch full details
//                        val detailedMeals = basicMeals.mapNotNull { meal ->
//                            fetchRecipeDetails(meal.id.toString())
//                        }
//
//                        // Update UI with detailed meals
//                        _meals.value = detailedMeals
//                    }
//                } else {
//                    // Handle error if API call unsuccessful
//                    _error.value = "Failed to create meal plan: ${response.message()}"
//                }
//            } catch (e: Exception) {
//                // Handle any other errors
//                _error.value = "Error: ${e.message}"
//            } finally {
//                // Hide loading indicator
//                _planningStatus.value = false
//            }
//        }
//    }
//    private val _selectedRecipe = MutableLiveData<Recipe>()
//    val selectedRecipe: LiveData<Recipe> = _selectedRecipe
//
//    fun setSelectedRecipe(recipe: Recipe) {
//        _selectedRecipe.value = recipe
//    }
//    private suspend fun fetchRecipeDetails(id: String): Recipe? {
//        return try {
//            val response = edamamApiService.getRecipeDetails(id, appId, appKey)
//            if (response.isSuccessful) {
//                response.body()?.let { details ->
//                    Recipe(
//
//                        name = details.label ?: "",
//                        cuisine = details.cuisineType?.firstOrNull(),
//                        mealType = details.mealType?.firstOrNull(),
//                        summary = details.summary,
//                        instructions = details.instructions ?: emptyList(),
//                        ingredients = details.ingredients ?: emptyList(),
//                        nutrients = details.nutrients ?: emptyList(),
//                        image = details.image,
//                        time = details.totalTime,
//                        dietaryPreferences = details.dietLabels?.joinToString(),
//                        dietaryRestrictions = details.healthLabels
//                    )
//                }
//            } else null
//        } catch (e: Exception) {
//            _error.value = "Error fetching recipe details: ${e.message}"
//            null
//        }
//    }
//
//    private fun processMealPlan(response: MealPlanResponse): List<Recipe> {
//        val recipes = mutableListOf<Recipe>()
//
//        response.selection.forEach { selection ->
//            selection.sections.forEach { (mealTime, section) ->
//                // Process main sections (like breakfast)
//                section.assigned?.let { recipeUri ->
//                    section._links?.self?.let { link ->
//                        recipes.add(
//                            Recipe(
//                                name = link.title,
//                                mealType = mealTime,
//                                summary = "", // Will be filled when fetching full details
//                                instructions = emptyList(),
//                                ingredients = emptyList(),
//                                nutrients = emptyList(),
//                                image = null,
//                                time = null,
//                                cuisine = null,
//                                dietaryPreferences = null,
//                                dietaryRestrictions = null
//                            )
//                        )
//                    }
//                }
//
//                // Process subsections (like lunch/dinner courses)
//                section.sections?.forEach { (course, subSection) ->
//                    subSection._links?.self?.let { link ->
//                        recipes.add(
//                            Recipe(
//                                name = link.title,
//                                mealType = "$mealTime - $course",
//                                summary = "",
//                                instructions = emptyList(),
//                                ingredients = emptyList(),
//                                nutrients = emptyList(),
//                                image = null,
//                                time = null,
//                                cuisine = null,
//                                dietaryPreferences = null,
//                                dietaryRestrictions = null
//                            )
//                        )
//                    }
//                }
//            }
//        }
//
//        return recipes
//    }
//}