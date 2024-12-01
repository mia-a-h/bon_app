// File: ProfileActivity.kt
package com.example.recipe_app.ui.auth.profile

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var profileImageUri: Uri? = null // Assume this is set via an image picker
    private lateinit var etName: EditText
    private lateinit var etContactInfo: EditText
    private lateinit var updateProfilebtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        updateProfilebtn = findViewById(R.id.profilebtn)
        etName = findViewById(R.id.name)
        etContactInfo =findViewById(R.id.contact)
        updateProfilebtn.setOnClickListener {
            val name = etName.text.toString().trim()
            val contactInfo = etContactInfo.text.toString().trim()
            updateUserProfile(name, contactInfo, profileImageUri)
        }

        // Implement image picker to set profileImageUri
    }

    private fun updateUserProfile(name: String, contactInfo: String, profileImageUri: Uri?) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(userId)

        val updates = hashMapOf<String, Any>(
            "name" to name,
            "contactInfo" to contactInfo
        )

        profileImageUri?.let {
            // Upload profile image to Firebase Storage
            val storageRef = storage.reference.child("profile_images/$userId.jpg")
            storageRef.putFile(it)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        updates["profileImageUrl"] = uri.toString()
                        userRef.update(updates)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } ?: run {
            // No profile image to upload
            userRef.update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
