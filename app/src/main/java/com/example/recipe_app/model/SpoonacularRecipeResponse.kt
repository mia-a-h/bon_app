package com.example.recipe_app.model

data class SpoonacularRecipeResponse(
    val id: Int = 0,
    val title: String = "",
    val image: String = "",
    val readyInMinutes: Int = 0,
    val cuisines: List<String> = emptyList(),
    val dishTypes: List<String> = emptyList(),
    val summary: String = "",
    val analyzedInstructions: List<AnalyzedInstruction> = emptyList(),
    val extendedIngredients: List<ExtendedIngredient> = emptyList(),
    val nutrition: Nutrition = Nutrition(),
    val diets: List<String> = emptyList(),
    val restrictions: List<String> = emptyList()
)

data class AnalyzedInstruction(
    val steps: List<Step> = emptyList()
)

data class Step(
    val number: Int = 0,
    val step: String = ""
)

data class ExtendedIngredient(
    val nameClean: String? = null,
    val amount: Double? = null
)

data class Nutrition(
    val nutrients: List<Nutrient> = emptyList()
)

data class Nutrient(
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = ""
)
