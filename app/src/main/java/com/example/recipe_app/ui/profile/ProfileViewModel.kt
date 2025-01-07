package com.example.recipe_app.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipe_app.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _profileData = MutableLiveData<Map<String, Any>>()
    val profileData: LiveData<Map<String, Any>> = _profileData
    private val _savedRecipes = MutableLiveData<List<Recipe>>()
    val savedRecipes: LiveData<List<Recipe>> = _savedRecipes
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUserProfile() {
        _isLoading.value = true
        val userId = auth.currentUser?.uid ?: run {
            _error.value = "User not logged in"
            _isLoading.value = false
            return
        }

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _profileData.value = document.data
                } else {
                    _error.value = "No profile data found"
                }
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = "Failed to load profile: ${e.message}"
                _isLoading.value = false
            }
    }

    fun updateProfile(updates: Map<String, Any>, profileImageUri: Uri?) {
        _isLoading.value = true
        val userId = auth.currentUser?.uid ?: run {
            _error.value = "User not logged in"
            _isLoading.value = false
            return
        }
        fun loadSavedRecipes() {
            _isLoading.value = true
            val userId = auth.currentUser?.uid ?: run {
                _error.value = "User not logged in"
                _isLoading.value = false
                return
            }

            firestore.collection("users")
                .document(userId)
                .collection("saved_recipes")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        _error.value = "Error loading saved recipes: ${e.message}"
                        return@addSnapshotListener
                    }

                    val recipesList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Recipe::class.java)
                    } ?: emptyList()

                    _savedRecipes.value = recipesList
                    _isLoading.value = false
                }
        }

        val userRef = firestore.collection("users").document(userId)

        if (profileImageUri != null) {
            val storageRef = storage.reference.child("profile_images/$userId.jpg")
            storageRef.putFile(profileImageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val updatedData = updates.toMutableMap().apply {
                            put("profileImageUrl", uri.toString())
                        }
                        updateFirestoreDocument(userRef, updatedData)
                    }
                }
                .addOnFailureListener { e ->
                    _error.value = "Image upload failed: ${e.message}"
                    _isLoading.value = false
                }
        } else {
            updateFirestoreDocument(userRef, updates)
        }
    }

    private fun updateFirestoreDocument(userRef: DocumentReference, updates: Map<String, Any>) {
        userRef.update(updates)
            .addOnSuccessListener {
                _profileData.value = updates
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = "Failed to update profile: ${e.message}"
                _isLoading.value = false
            }
    }
}