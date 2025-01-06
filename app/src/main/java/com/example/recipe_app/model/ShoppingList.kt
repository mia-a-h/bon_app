package com.example.recipe_app.model

data class ShoppingList(
    val recipeId: String = "",
    val ingredients: List<Ingredient> = emptyList()
)
