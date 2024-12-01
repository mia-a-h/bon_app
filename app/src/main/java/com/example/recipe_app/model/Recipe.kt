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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val cuisine: String?,
    val mealType: String?,
    val instructions: List<String>?,
    val image: String?, // URL for the recipe image
    val time: Int?, // Adjusted to an integer (e.g., minutes) as Spoonacular provides `readyInMinutes`
    val dietaryPreferences: String?,
    val dietaryRestrictions: List<String>?
)
