package com.example.recipe_app.utils

import android.util.Log
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.SpoonacularRecipeResponse

fun mapSearchToLocalRecipe(apiRecipe: SpoonacularRecipeResponse): Recipe {
        return Recipe(
            id = apiRecipe.id,
            name = apiRecipe.title.orEmpty(),
            summary = apiRecipe.summary.orEmpty(),
            instructions = emptyList(),
            ingredients = emptyList(),
            nutrients = emptyList(),
            image = apiRecipe.image ?: "",
            time = apiRecipe.readyInMinutes ?: 0,
            cuisine = apiRecipe.cuisines?.filterNotNull()?.joinToString(", ") ?: "unknown",
            mealType = apiRecipe.dishTypes?.filterNotNull()?.joinToString(", ") ?: "unknown",
            dietaryPreferences = apiRecipe.diets?.filterNotNull()?.joinToString(", ") ?: "none",
            dietaryRestrictions = apiRecipe.restrictions ?: emptyList()
        )
}