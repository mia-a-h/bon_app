package com.example.recipe_app.ui.services

import android.util.Log
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.ShoppingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShoppingListSaveService {

    private val db = FirebaseFirestore.getInstance()

    fun saveShoppingListToFirestore(recipe: Recipe) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val recipeId = recipe.id.toString() // Unique identifier for the recipe

        // Create the shopping list object (or map) from the ingredients
        val shoppingList = mapOf(
            "ingredients" to recipe.ingredients.map { ingredient ->
                mapOf(
                    "name" to ingredient.nameClean,
                    "quantity" to ingredient.amount
                )
            }
        )

        // Save the shopping list to Firestore under the user's shopping_lists subcollection
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId!!)
            .collection("shopping_lists")
            .document(recipeId) // Store shopping list with recipeId as the document ID
            .set(shoppingList)
            .addOnSuccessListener {
                Log.d("ShoppingListSave", "Shopping list saved successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("ShoppingListSave", "Failed to save shopping list: ${e.message}", e)
            }
    }

    fun getShoppingListFromFirestore(userId: String, callback: (List<ShoppingList>) -> Unit) {
        db.collection("users")
            .document(userId)
            .collection("shopping_lists")
            .get()
            .addOnSuccessListener { result ->
                val shoppingLists = mutableListOf<ShoppingList>()
                for (document in result) {
                    // Assuming the shopping list has an id and ingredients field
                    val shoppingList = document.toObject(ShoppingList::class.java)
                    shoppingLists.add(shoppingList)
                }
                callback(shoppingLists)
            }
            .addOnFailureListener { e ->
                Log.e("ShoppingListFetch", "Failed to fetch shopping lists: ${e.message}", e)
                callback(emptyList()) // If error, return empty list
            }
    }

    fun deleteShoppingListFromFirestore(userId: String, recipeId: String) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("shopping_lists")
            .document(recipeId)
            .delete()
            .addOnSuccessListener {
                Log.d("ShoppingListDelete", "Shopping list deleted successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("ShoppingListDelete", "Failed to delete shopping list: ${e.message}", e)
            }
    }

}