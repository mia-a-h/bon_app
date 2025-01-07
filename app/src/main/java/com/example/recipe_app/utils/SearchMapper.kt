package com.example.recipe_app.utils

import android.util.Log
import com.example.recipe_app.model.AnalyzedInstruction
import com.example.recipe_app.model.ExtendedIngredient
import com.example.recipe_app.model.Ingredient
import com.example.recipe_app.model.Nutrient
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.SpoonacularRecipeResponse
import com.example.recipe_app.model.SpoonacularSearchResponse
import com.example.recipe_app.model.Step

fun mapSearchToLocalRecipe(apiRecipe: SpoonacularSearchResponse): Recipe {
    val analyzedInstructions = apiRecipe.analyzedInstructions.map { analyzedInstruction ->
        AnalyzedInstruction(
            steps = analyzedInstruction.steps.map { instruction ->
                Step(number = instruction.number, step = instruction.step)
            }
        )
    }

    val extractedIngredients = apiRecipe.nutrition.ingredients.map { ingredient ->
        ExtendedIngredient(nameClean = ingredient.name, amount = ingredient.amount)
    }

    Log.d("mapSearchToLocalRecipe", "ingredients $extractedIngredients")

    // Ensure extractedNutrients is a list of Nutrient objects
    val extractedNutrients = apiRecipe.nutrition.nutrients.map { nutrient ->
        Nutrient(name = nutrient.name, amount = nutrient.amount, unit = nutrient.unit)
    }

    val desiredNutrients = listOf("Calories", "Fat", "Carbohydrates", "Protein", "Fiber", "Sugar")

    val finalNutrients = extractedNutrients.filter { nutrient -> nutrient.name in desiredNutrients }

    try {
        return Recipe(
            id = apiRecipe.id,
            name = apiRecipe.title.orEmpty(),
            summary = apiRecipe.summary.orEmpty(),
            instructions = analyzedInstructions,
            ingredients = extractedIngredients,
            nutrients = finalNutrients,
            image = apiRecipe.image ?: "",
            time = apiRecipe.readyInMinutes ?: 0,
            cuisine = apiRecipe.cuisines?.filterNotNull()?.joinToString(", ") ?: "unknown", //can use limit parameter
            mealType = apiRecipe.dishTypes?.filterNotNull()?.joinToString(", ","", "", 3) ?: "unknown",
            dietaryPreferences = apiRecipe.diets?.filterNotNull()?.joinToString(", ") ?: "none",
            dietaryRestrictions = apiRecipe.restrictions ?: emptyList()
        )
    } catch (e: Exception) {
        Log.e("RecipeMapper", "Error mapping recipe: ${e.message}")
        throw e
    }
}