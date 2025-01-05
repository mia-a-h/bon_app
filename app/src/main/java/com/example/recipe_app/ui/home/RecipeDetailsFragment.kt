package com.example.recipe_app.ui.home

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.adapter.IngredientsAdapter
import com.example.recipe_app.adapter.InstructionsAdapter
import com.example.recipe_app.adapter.NutrientAdapter
import com.example.recipe_app.databinding.FragmentRecipeDetailsBinding
import com.example.recipe_app.viewmodels.SavedRecipesViewModel

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel for retrieving the current recipe
    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()

    // SavedRecipesViewModel for saving the recipe
    private val savedRecipesViewModel: SavedRecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Observe saveStatus so we can show success/failure messages
        savedRecipesViewModel.saveStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Recipe saved!", Toast.LENGTH_SHORT).show()
                binding.favoriteButton.isSelected = true
            } else {
                Toast.makeText(context, "Failed to save recipe", Toast.LENGTH_SHORT).show()
            }
        }

        // 2) Observe the selectedRecipe from the shared ViewModel
        sharedViewModel.selectedRecipe.observe(viewLifecycleOwner) { recipe ->
            // If recipe is null, do nothing
            recipe ?: return@observe

            // Update the UI with the recipe details
            binding.recipeName.text = recipe.name
            binding.recipeSummary.text = Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_LEGACY)
            binding.recipeSummary.movementMethod = LinkMovementMethod.getInstance()
            binding.cuisineTag.text = recipe.cuisine
            binding.mealTypeTag.text = recipe.mealType
            binding.totalTime.text = recipe.time.toString()

            // *** Set the click listener here, where 'recipe' is in scope
            binding.favoriteButton.setOnClickListener {
                // Call the instance's saveRecipe function
                savedRecipesViewModel.saveRecipe(recipe)
            }

            // Ingredients RecyclerView
            binding.ingredientsList.layoutManager = LinearLayoutManager(context)
            binding.ingredientsList.adapter = IngredientsAdapter(recipe.ingredients)

            // Instructions RecyclerView
            val flattenedSteps = recipe.instructions.flatMap { it.steps }
            binding.instructionsList.layoutManager = LinearLayoutManager(context)
            binding.instructionsList.adapter = InstructionsAdapter(flattenedSteps)

            // Nutrition RecyclerView
            binding.nutritionList.layoutManager = LinearLayoutManager(context)
            binding.nutritionList.adapter = NutrientAdapter(recipe.nutrients)

            // Load the image
            Glide.with(this)
                .load(recipe.image)
                .placeholder(R.drawable.ic_sample_image)
                .into(binding.imageView)
        }

        // Toggle nutrition list visibility + icon
        binding.viewToggleIcon.setOnClickListener {
            if (binding.nutritionList.visibility == View.GONE) {
                binding.nutritionList.visibility = View.VISIBLE
                binding.viewToggleIcon.setImageResource(R.drawable.ic_remove)
            } else {
                binding.nutritionList.visibility = View.GONE
                binding.viewToggleIcon.setImageResource(R.drawable.ic_add)
            }
        }

        // Shopping basket icon for showing dialog
        binding.shoppingBasketIcon.setOnClickListener {
            sharedViewModel.selectedRecipe.value?.let { recipe ->
                // Show an AlertDialog with the list of ingredients
                val dialogView = layoutInflater.inflate(R.layout.shopping_list_dialog, null)
                val dialog = AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .setCancelable(true)
                    .create()

                val shoppingListContent = dialogView.findViewById<RecyclerView>(R.id.shoppingList)
                shoppingListContent.layoutManager = LinearLayoutManager(requireContext())
                shoppingListContent.adapter = IngredientsAdapter(recipe.ingredients)

                val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
                val addToShoppingListPageButton = dialogView.findViewById<Button>(R.id.addToShoppingListPageButton)

                cancelButton.setOnClickListener { dialog.dismiss() }
                addToShoppingListPageButton.setOnClickListener {
                    Toast.makeText(requireContext(), "Shopping list added!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                dialog.show()
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
