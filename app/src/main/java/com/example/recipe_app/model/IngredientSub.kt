package com.example.recipe_app.model

data class IngredientSub(
    val ingredient: String,
    val substitutes: List<String>,
    val message: String
)
