package com.example.recipe_app.model

//import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipe_app.Converters
import com.example.recipe_app.dao.RecipeDao


@Entity(tableName = "Recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val name: String,
    val cuisine: String?,
    val mealType: String?,
    val summary: String?,
    val instructions: List<AnalyzedInstruction>,
    val ingredients: List<ExtendedIngredient>,
    val nutrients: List<Nutrient>,
    val image: String?, // URL for the recipe image
    val time: Int?,
    val dietaryPreferences: String?,
    val dietaryRestrictions: List<String>?
) {
    // Firebase requires a no-argument constructor for deserialization
    constructor() : this(
        id = 0,
        name = "",
        cuisine = null,
        mealType = null,
        summary = null,
        instructions = emptyList(),
        ingredients = emptyList(),
        nutrients = emptyList(),
        image = null,
        time = null,
        dietaryPreferences = null,
        dietaryRestrictions = null
    )

    companion object {
        fun fromEdamamRecipe(edamamRecipe: EdamamRecipe): Recipe {
            return Recipe(
                id = 0, // Set to 0 or null as `EdamamRecipe` does not provide an ID
                name = edamamRecipe.name,
                cuisine = null, // Cuisine is not available in `EdamamRecipe`
                mealType = null, // Meal type is not available in `EdamamRecipe`
                summary = null, // Summary is not available in `EdamamRecipe`
                instructions = emptyList(), // Instructions need mapping if available
                ingredients = emptyList(), // Ingredients need mapping if available
                nutrients = emptyList(), // Nutrients need mapping if available
                image = edamamRecipe.image,
                time = edamamRecipe.totalTime,
                dietaryPreferences = null, // Dietary preferences need mapping if available
                dietaryRestrictions = null // Dietary restrictions need mapping if available
            )
        }
    }
}
