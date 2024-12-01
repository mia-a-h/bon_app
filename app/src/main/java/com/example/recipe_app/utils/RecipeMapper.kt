package com.example.recipe_app.utils

import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.SpoonacularRecipeResponse

fun mapToLocalRecipe(apiRecipe: SpoonacularRecipeResponse): Recipe {
    return Recipe(
        id = apiRecipe.id,
        name = apiRecipe.title,
        instructions = emptyList(), // Replace with actual instructions from API if available
        image = apiRecipe.image?: "",
        time = apiRecipe.readyInMinutes?:0,
        cuisine = apiRecipe.cuisines?.filterNotNull()?.joinToString(", ")?:"unknown",
        mealType = apiRecipe.dishTypes?.filterNotNull()?.joinToString(", ")?: "unknown",
        dietaryPreferences = apiRecipe.diets?.filterNotNull()?.joinToString(", ")?: "none", // Join all dietary preferences into a single string
        dietaryRestrictions = apiRecipe.restrictions?: emptyList() // If available, map to list
    )
}