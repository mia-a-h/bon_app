package com.example.recipe_app.model

import com.google.firebase.firestore.PropertyName

data class AnalyzedInstruction(
    val steps: List<Step>
)

data class Step(
    val number: Int,
    val step: String
)

data class ExtendedIngredient(      //for the get random recipes
    @PropertyName("name") val nameClean: String? = "", //ingredient name
    @PropertyName("amount") val amount: Double? = 0.0
) {
    //Firestore requires a no-argument constructor to deserialize
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
    @PropertyName("amount") val amount: Double = 0.0
) {
    //Firestore requires a no-argument constructor to deserialize
    constructor() : this("", 0.0)
}
