package com.example.recipe_app.model

data class SpoonacularSearchResponse(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val cuisines: List<String>,
    val dishTypes: List<String>,
    val summary: String,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val nutrition: Nutrition,
    val diets: List<String>,
    val restrictions: List<String>
)
