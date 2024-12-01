package com.example.recipe_app.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM Recipes")
    fun getSavedRecipes(): LiveData<List<Recipe>>

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE (:cuisineType = 'All' OR cuisine LIKE '%' || :cuisineType || '%') AND (:mealType = 'All' OR mealType LIKE '%' || :mealType || '%')\n")
    fun getRecipesByFilter(cuisineType: String, mealType: String): LiveData<List<Recipe>>


}
