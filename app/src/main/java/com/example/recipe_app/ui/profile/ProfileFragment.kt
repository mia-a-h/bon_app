package com.example.recipe_app.ui.profile

import android.app.DatePickerDialog
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.adapter.MealPlanAdapter
import com.example.recipe_app.databinding.FragmentProfileBinding
import com.example.recipe_app.viewmodels.SavedRecipesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private var profileImageUri: Uri? = null
    private var isSavedRecipesVisible = false
    private val savedRecipesViewModel: SavedRecipesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
        setupSavedRecipesRecyclerView()
        viewModel.loadUserProfile()
    }

    private fun setupObservers() {
        viewModel.profileData.observe(viewLifecycleOwner) { data ->
            binding.apply {
                etFirstName.setText(data["firstName"] as? String ?: "")
                etLastName.setText(data["lastName"] as? String ?: "")
                etBirthday.setText(data["birthday"] as? String ?: "")
                val gender = data["gender"] as? String ?: ""
                val genderIndex = resources.getStringArray(R.array.gender_options).indexOf(gender)
                if (genderIndex >= 0) {
                    spinnerGender.setSelection(genderIndex)
                }
                val profileImageUrl = data["profileImageUrl"] as? String
                if (!profileImageUrl.isNullOrEmpty()) {
                    Glide.with(this@ProfileFragment)
                        .load(profileImageUrl)
                        .placeholder(R.drawable.ic_default_profile)
                        .into(ivProfilePicture)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.profilebtn.isEnabled = !isLoading
            // You might want to add a progress bar to show loading state
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            profilebtn.setOnClickListener {
                val updates = hashMapOf<String, Any>(
                    "firstName" to etFirstName.text.toString().trim(),
                    "lastName" to etLastName.text.toString().trim(),
                    "birthday" to etBirthday.text.toString().trim(),
                    "gender" to spinnerGender.selectedItem.toString()
                )
                viewModel.updateProfile(updates, profileImageUri)
            }

            showSavedRecipesButton.setOnClickListener {
                isSavedRecipesVisible = !isSavedRecipesVisible
                binding.savedRecipesRecyclerView.visibility =
                    if (isSavedRecipesVisible) View.VISIBLE else View.GONE
                binding.showSavedRecipesButton.text =
                    if (isSavedRecipesVisible) "Hide Saved Recipes" else "Show Saved Recipes"

                if (isSavedRecipesVisible) {
                    savedRecipesViewModel.loadSavedRecipes()
                }
            }

        }
    }
    private fun setupSavedRecipesRecyclerView() {
        val adapter = MealPlanAdapter(
            onMealClick = { recipe ->
                // Handle recipe click (navigate to details)
            }
        )

        binding.savedRecipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        savedRecipesViewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            adapter.updateMeals(recipes)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}