package com.example.recipe_app.model


data class EdamamRecipeResponse(
    val label: String?,
    val image: String?,
    val totalTime: Int?,
    val summary: String?,
    val cuisineType: List<String>?,
    val mealType: List<String>?,
    val ingredients: List<ExtendedIngredient>?,
    val instructions: List<AnalyzedInstruction>?,
    val nutrients: List<Nutrient>?,
    val dietLabels: List<String>?,
    val healthLabels: List<String>?
)