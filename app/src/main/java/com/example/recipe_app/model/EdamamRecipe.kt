// Data class for the entire API response
data class EdamamApiResponse(
    val hits: List<Hit>
)

// Data class for each hit in the response
data class Hit(
    val recipe: EdamamRecipe
)

// Data class for the recipe details
data class EdamamRecipe(
    val uri: String?,
    val label: String?,
    val image: String?,
    val source: String?,
    val url: String?,
    val ingredientLines: List<String>?,
    val calories: Double?,
    val totalTime: Double?,
    val cuisineType: List<String>?,
    val mealType: List<String>?,
    val dietLabels: List<String>?,
    val healthLabels: List<String>?
    // Add other relevant fields as needed
)
