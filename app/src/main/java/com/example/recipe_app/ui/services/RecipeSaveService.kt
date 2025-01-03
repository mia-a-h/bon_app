package com.example.recipe_app.ui.services
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.recipe_app.model.Recipe

class RecipeSaveService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveRecipe(recipe: Recipe, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("saved_recipes")
            .document(recipe.id.toString())
            .set(recipe)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getSavedRecipes(onSuccess: (List<Recipe>) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("saved_recipes")
            .get()
            .addOnSuccessListener { documents ->
                val recipes = documents.mapNotNull { it.toObject(Recipe::class.java) }
                onSuccess(recipes)
            }
            .addOnFailureListener { onFailure(it) }
    }
}