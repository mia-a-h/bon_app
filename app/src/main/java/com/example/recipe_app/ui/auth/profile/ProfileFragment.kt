package com.example.recipe_app.ui.auth.profile

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Views
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        etBirthday = view.findViewById(R.id.etBirthday)
        spinnerGender = view.findViewById(R.id.spinnerGender)
        updateProfilebtn = view.findViewById(R.id.profilebtn)

        // Set DatePicker for Birthday
        etBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
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

        return view
    }

    // The rest of the methods (selectImage, loadUserProfile, updateUserProfile) are similar
    // Adapt these methods to use `requireContext()` when accessing context

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImageUri = it
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_default_profile)
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
                        Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_default_profile)
                            .into(ivProfilePicture)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } ?: run {
            userRef.update(updates)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
