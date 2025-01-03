package com.example.recipe_app.model

import com.google.gson.annotations.SerializedName

// Request Classes
data class MealPlanRequest(
    val size: Int,
    val plan: Plan
)

data class Plan(
    val accept: Accept?,
    val fit: Map<String, MinMax>?,
    val exclude: List<String>?,
    val sections: Map<String, SectionRequest>
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
    val fit: Map<String, MinMax>?
)

// Response Classes
data class MealPlanResponse(
    val status: String,
    val selection: List<Selection>
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