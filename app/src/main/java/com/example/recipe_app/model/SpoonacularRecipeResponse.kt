package com.example.recipe_app.model

data class SpoonacularRecipeResponse(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val cuisines: List<String>,
    val dishTypes: List<String>,
    val summary: String, //Description or overview of the recipe
    val analyzedInstructions: List<AnalyzedInstruction>,
    val extendedIngredients: List<ExtendedIngredient>,
    val nutrition: Nutrition,
    val diets: List<String>,
    val restrictions: List<String>
)

data class AnalyzedInstruction(
    val steps: List<Step>  // The list of steps (each step contains number and step description)
)

data class Step(
    val number: Int,  // The step number (e.g., 1, 2, 3)
    val step: String  // The step description (e.g., "Mix flour and melted butter with a beater.")
)

data class ExtendedIngredient(
    val nameClean: String?, //ingredient name
    val amount: Double?
)

data class Nutrition(
    val nutrients: List<Nutrient> //nutrient details
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String
)