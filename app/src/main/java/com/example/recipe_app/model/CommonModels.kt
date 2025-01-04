package com.example.recipe_app.model

data class AnalyzedInstruction(
    val steps: List<Step>  // The list of steps (each step contains number and step description)
)

data class Step(
    val number: Int,  // The step number (e.g., 1, 2, 3)
    val step: String  // The step description (e.g., "Mix flour and melted butter with a beater.")
)

data class ExtendedIngredient(      //for the get random recipes
    val nameClean: String?, //ingredient name
    val amount: Double?
)

data class Nutrition(
    val nutrients: List<Nutrient>, //nutrient details
    val ingredients: List<Ingredient>
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String
)

data class Ingredient(      //for the search for recipes
    val name: String,
    val amount: Double
)
