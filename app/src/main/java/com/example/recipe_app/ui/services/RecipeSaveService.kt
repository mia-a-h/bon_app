package com.example.recipe_app.ui.services
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.recipe_app.model.Recipe
import java.util.UUID

class RecipeSaveService {
    fun saveRecipe(
        recipe: Recipe,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("saved_recipes")
            .document(recipe.id.toString())
            .set(recipe)
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("saved_recipes")
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


//    fun saveRecipeForUser(
//        recipe: Recipe,
//        userId: String, // Pass the user ID explicitly
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val firestore = FirebaseFirestore.getInstance()
//
//        // Save the recipe in the user-specific "saved_recipes" collection
//        firestore.collection("users")
//            .document(userId) // Reference the user's document
//            .collection("saved_recipes") // Reference the "saved_recipes" collection
//            .document(recipe.id.toString() ?: UUID.randomUUID().toString()) // Use recipe ID or generate one
//            .set(recipe) // Save the recipe
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { e ->
//                Log.e("RecipeSaveService", "Failed to save recipe: ${e.message}", e)
//                onFailure(e)
//            }
//    }


