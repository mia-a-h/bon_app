package com.example.recipe_app

import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.SpoonacularRecipeResponse

fun mapToLocalRecipe(apiRecipe: SpoonacularRecipeResponse): Recipe {
    return Recipe(
        id = apiRecipe.id,
        name = apiRecipe.title,
        instructions = emptyList(), // Replace with actual instructions from API if available
        image = apiRecipe.image,
        time = apiRecipe.readyInMinutes,
        cuisine = apiRecipe.cuisines.joinToString(", "),
        mealType = apiRecipe.dishTypes.joinToString(", "),
        dietaryPreferences = apiRecipe.diets.joinToString(", "), // Join all dietary preferences into a single string
        dietaryRestrictions = apiRecipe.restrictions.let { it } ?: emptyList() // If available, map to list
    )
}
