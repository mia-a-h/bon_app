package com.example.recipe_app.model

import com.google.firebase.firestore.PropertyName

data class AnalyzedInstruction(
    val steps: List<Step>  // The list of steps (each step contains number and step description)
)

data class Step(
    val number: Int,  // The step number (e.g., 1, 2, 3)
    val step: String  // The step description (e.g., "Mix flour and melted butter with a beater.")
)

data class ExtendedIngredient(      //for the get random recipes
    @PropertyName("name") val nameClean: String? = "", //ingredient name
    @PropertyName("quantity") val quantity: Double? = 0.0
) {
    // Firestore requires a no-argument constructor to deserialize
    constructor() : this("", 0.0)
}

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
    @PropertyName("name") val name: String = "",
    @PropertyName("quantity") val quantity: Double = 0.0
) {
    // Firestore requires a no-argument constructor to deserialize
    constructor() : this("", 0.0)
}
