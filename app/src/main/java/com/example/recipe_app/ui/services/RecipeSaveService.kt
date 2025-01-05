package com.example.recipe_app.ui.services
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.recipe_app.model.Recipe

class RecipeSaveService {
    fun saveRecipe(
        recipe: Recipe,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        // Option A: Use .add(recipe)
        firestore.collection("recipes")
            .add(recipe)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("RecipeSaveService", "Failed to save recipe: ${e.message}", e)
                onFailure()
            }
    }

    fun getSavedRecipes(
        onSuccess: (List<Recipe>) -> Unit,
        onFailure: () -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = querySnapshot.toObjects(Recipe::class.java)
                onSuccess(recipes)
            }
            .addOnFailureListener { e ->
                Log.e("RecipeSaveService", "Failed to load recipes: ${e.message}", e)
                onFailure()
            }
    }
}
