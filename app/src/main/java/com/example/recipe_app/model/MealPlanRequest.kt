package com.example.recipe_app.model

data class MealPlanRequest(
    val size: Int,            // Number of days to generate the meal plan for
    val plan: Plan            // The detailed plan containing dietary preferences, calorie limits, etc.
)

data class Plan(
    val accept: Accept,                              // Dietary preferences (e.g., balanced, vegan)
    val fit: Map<String, MinMax>,                   // Calorie or nutrient range constraints
    val sections: Map<String, SectionRequest>?      // Optional, specific sections like breakfast, lunch, dinner
)

data class Accept(
    val all: List<Map<String, List<String>>>        // Health labels or other filters
)

data class MinMax(
    val min: Int,                                   // Minimum value for a constraint (e.g., calories)
    val max: Int                                    // Maximum value for a constraint (e.g., calories)
)

data class SectionRequest(
    val accept: Accept,                             // Preferences for this section
    val fit: Map<String, MinMax>                   // Constraints for this section
)
