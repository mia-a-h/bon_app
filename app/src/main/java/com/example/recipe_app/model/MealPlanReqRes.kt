package com.example.recipe_app.model

import com.google.gson.annotations.SerializedName

// Request Classes
/*These classes
represent the structure of the request
the app sends to the API to generate a meal plan.*/
data class MealPlanRequest(
    val size: Int,
    val plan: Plan
)

data class Plan(
    val accept: Accept?,  // Specifies what to include in the plan
    val fit: Map<String, MinMax>?,  // Nutritional constraints (e.g., calorie range)
    val exclude: List<String>?, // Ingredients to exclude (e.g., allergies)
    val sections: Map<String, SectionRequest>  // Different meal sections (e.g., breakfast, lunch, dinner)
){
    companion object {
        @SerializedName("SUGAR.added")
        const val SUGAR_ADDED = "SUGAR.added"

        @SerializedName("ENERC_KCAL")
        const val ENERGY_KCAL = "ENERC_KCAL"
    }
}

data class Accept(
    val all: List<Map<String, List<String>>>
)

data class MinMax(
    val min: Int? = null,
    val max: Int? = null
)

data class SectionRequest(
    val accept: Accept?,
    val fit: Map<String, MinMax>? // Nutritional constraints for this section
)

// Response Classes
/*These classes (MealPlanResponse, Selection, etc.)
 represent the structure of the data
 the app receives from the API after making a request.*/
data class MealPlanResponse(   // Status of the response (e.g., "success")
    val status: String,
    val selection: List<Selection>  // The list of selected meal plans
)

data class Selection(
    val sections: Map<String, SectionResponse>
)

data class SectionResponse(
    val assigned: String? = null,
    val _links: Links? = null,
    val sections: Map<String, SubSection>? = null
)

data class SubSection(
    val assigned: String,
    val _links: Links
)

data class Links(
    val self: Link
)

data class Link(
    val title: String,
    val href: String
)