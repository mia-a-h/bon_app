package com.example.recipe_app.ui.auth.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var profileImageUri: Uri? = null
    private lateinit var ivProfilePicture: ImageView
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etBirthday: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var updateProfilebtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Glide.with(this)
            .load(R.drawable.ic_default_profile)
            .into(ivProfilePicture)
        // Initialize Views
        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etBirthday = findViewById(R.id.etBirthday)
        spinnerGender = findViewById(R.id.spinnerGender)
        updateProfilebtn = findViewById(R.id.profilebtn)

        // Set DatePicker for Birthday
        etBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val birthday = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    etBirthday.setText(birthday)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        // Set OnClickListener to ImageView to pick an image
        ivProfilePicture.setOnClickListener {
            selectImage()
        }

        // Load current user data
        loadUserProfile()

        // Update profile on button click
        updateProfilebtn.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val birthday = etBirthday.text.toString().trim()
            val gender = spinnerGender.selectedItem.toString()
            updateUserProfile(firstName, lastName, birthday, gender, profileImageUri)
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImageUri = it
                // Load the selected image into the ImageView using Glide
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_default_profile) // Default image while loading
                    .into(ivProfilePicture)
            }
        }

    private fun selectImage() {
        imagePickerLauncher.launch("image/*")
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    etFirstName.setText(document.getString("firstName"))
                    etLastName.setText(document.getString("lastName"))
                    etBirthday.setText(document.getString("birthday"))
                    val gender = document.getString("gender") ?: ""
                    val genderIndex = resources.getStringArray(R.array.gender_options).indexOf(gender)
                    if (genderIndex >= 0) {
                        spinnerGender.setSelection(genderIndex)
                    }
                    val profileImageUrl = document.getString("profileImageUrl")
                    if (!profileImageUrl.isNullOrEmpty()) {
                        // Load the profile image using Glide
                        Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_default_profile) // Default image while loading
                            .into(ivProfilePicture)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserProfile(
        firstName: String,
        lastName: String,
        birthday: String,
        gender: String,
        profileImageUri: Uri?
    ) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(userId)

        val updates = hashMapOf<String, Any>(
            "firstName" to firstName,
            "lastName" to lastName,
            "birthday" to birthday,
            "gender" to gender
        )

        profileImageUri?.let {
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
