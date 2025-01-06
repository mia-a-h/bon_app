package com.example.recipe_app.utils

import EdamamRecipe
import android.util.Log
import com.example.recipe_app.model.AnalyzedInstruction
import com.example.recipe_app.model.ExtendedIngredient
import com.example.recipe_app.model.Meal
import com.example.recipe_app.model.Nutrient
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.SpoonacularRecipeResponse
import com.example.recipe_app.model.Step
import org.jsoup.Jsoup

fun mapToLocalRecipe(apiRecipe: SpoonacularRecipeResponse): Recipe {

    val analyzedInstructions = apiRecipe.analyzedInstructions.map { analyzedInstruction ->
        // Wrap each List<Step> into an AnalyzedInstruction
        AnalyzedInstruction(
            steps = analyzedInstruction.steps.map { instruction ->
                Step(number = instruction.number, step = instruction.step)
            }
        )
    }

    // Map extendedIngredients to ExtendedIngredient objects, not just names
    val extractedIngredients = apiRecipe.extendedIngredients.map { ingredient ->
        // Assuming ExtendedIngredient is a data class, you can extract the necessary fields here
        ExtendedIngredient(nameClean = ingredient.nameClean, amount = ingredient.amount)
    }

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
            cuisine = apiRecipe.cuisines?.filterNotNull()?.joinToString(", ") ?: "unknown",
            mealType = apiRecipe.dishTypes?.filterNotNull()?.joinToString(", ") ?: "unknown",
            dietaryPreferences = apiRecipe.diets?.filterNotNull()?.joinToString(", ") ?: "none",
            dietaryRestrictions = apiRecipe.restrictions ?: emptyList()
        )
    } catch (e: Exception) {
        Log.e("RecipeMapper", "Error mapping recipe: ${e.message}")
        throw e
    }


}


fun mapMealToRecipe(meal: Meal): Recipe {
    return Recipe(
        id = null, // ID is auto-generated in Room, so set it as null or 0 for insertion
        name = meal.recipe.name.orEmpty(),
        cuisine = "",
        mealType = meal.mealType,
        summary = "Recipe from ${meal.recipe.source.orEmpty()}",
        instructions = emptyList(), // Populate if instruction data is available
        ingredients = emptyList(),  // Populate if ingredient data is available
        nutrients = emptyList(),    // Populate nutrients if available
        image = meal.recipe.image.orEmpty(),
        time = meal.recipe.totalTime?.toInt(),
        dietaryPreferences = "",
        dietaryRestrictions = listOf()
    )
}



//fun parseInstructions(instructionsHtml: String): List<String>{
//    val document = Jsoup.parse(instructionsHtml)
//    return document.select("li").map { it.text() }
//}