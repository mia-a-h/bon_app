package com.example.recipe_app.model

import com.google.gson.annotations.SerializedName

data class MealPlanResponses(
    @SerializedName("meals") val meals: List<Meal>
)
data class Meal(
    @SerializedName("day") val day: String,       // Day of the meal (e.g., "Monday")
    @SerializedName("mealType") val mealType: String,  // Type of meal (e.g., "Breakfast", "Lunch")
    @SerializedName("recipe") val recipe: EdamamRecipe
)

data class EdamamRecipe(
    @SerializedName("id") val id: String,        // Unique identifier for the recipe
    @SerializedName("name") val name: String,      // Name of the recipe
    @SerializedName("image") val image: String,     // URL to an image of the dish
    @SerializedName("source") val source: String,    // Source or author of the recipe
    @SerializedName("url") val url: String,       // URL to the full recipe
    @SerializedName("calories") val calories: Double,  // Total calories per serving
    @SerializedName("totalTime") val totalTime: Int     // Total time to prepare the recipe in minutes
)