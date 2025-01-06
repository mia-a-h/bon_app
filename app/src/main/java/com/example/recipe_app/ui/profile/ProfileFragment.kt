package com.example.recipe_app.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.adapter.RecipeAdapter
import com.example.recipe_app.databinding.FragmentProfileBinding
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.viewmodels.SavedRecipesViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val savedRecipesViewModel: SavedRecipesViewModel by viewModels()
    private var profileImageUri: Uri? = null

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
        setupProfileObservers()
        setupSavedRecipesRecyclerView()
        setupClickListeners()
        viewModel.loadUserProfile() // Load user profile data
    }

    private fun setupProfileObservers() {
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
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            // Handle profile updates
            profilebtn.setOnClickListener {
                val updates = hashMapOf(
                    "firstName" to etFirstName.text.toString().trim(),
                    "lastName" to etLastName.text.toString().trim(),
                    "birthday" to etBirthday.text.toString().trim(),
                    "gender" to spinnerGender.selectedItem.toString()
                )
                viewModel.updateProfile(updates, profileImageUri)
            }

            // Handle showing saved recipes
            showSavedRecipesButton.setOnClickListener {
                savedRecipesViewModel.loadSavedRecipes() // Fetch saved recipes
                binding.savedRecipesRecyclerView.visibility = View.VISIBLE
                showSavedRecipesButton.text = "Reload Saved Recipes" // Update button text
            }

            // Optional: Add a hide button for better UX
//            hideSavedRecipesButton.setOnClickListener {
//                binding.savedRecipesRecyclerView.visibility = View.GONE
//            }
        }
    }

    private fun setupSavedRecipesRecyclerView() {
        // Initialize the adapter for displaying saved recipes
        val adapter = RecipeAdapter(emptyList()) { recipe ->
            Toast.makeText(requireContext(), "Clicked on: ${recipe.name}", Toast.LENGTH_SHORT).show()
        }

        binding.savedRecipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context) // Vertical list
            this.adapter = adapter // Attach adapter
        }

        // Observe saved recipes LiveData and update the adapter
        savedRecipesViewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            println("ProfileFragment: Updating RecyclerView with ${recipes.size} recipes")
            binding.savedRecipesRecyclerView.visibility = if (recipes.isEmpty()) View.GONE else View.VISIBLE
            adapter.updateData(recipes) // Update RecyclerView with fetched recipes
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
