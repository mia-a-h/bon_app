// File: UserService.kt
package com.example.recipe_app.services

import com.google.firebase.firestore.FirebaseFirestore

class UserService {

    private val firestore = FirebaseFirestore.getInstance()

    fun getUserRole(userId: String, onResult: (String?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val role = document.getString("role")
                    onResult(role)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
