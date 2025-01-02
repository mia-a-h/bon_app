package com.example.recipe_app.model

data class SubstituteResponse(
    val ingredient: String,
    val substitutes: List<String>,
    val message: String
)

