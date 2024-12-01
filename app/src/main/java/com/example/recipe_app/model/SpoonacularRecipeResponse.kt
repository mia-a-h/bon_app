package com.example.recipe_app.model

data class SpoonacularRecipeResponse(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val cuisines: List<String>, // Spoonacular may provide cuisines as a list
    val dishTypes: List<String>, // Could represent meal types
    val summary: String, // Description or overview of the recipe
    val diets: List<String>,
    val restrictions: List<String>
)

