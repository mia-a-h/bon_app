package com.example.recipe_app.ui.home

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.adapter.IngredientsAdapter
import com.example.recipe_app.adapter.InstructionsAdapter
import com.example.recipe_app.adapter.NutrientAdapter
import com.example.recipe_app.databinding.FragmentRecipeDetailsBinding

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the selectedRecipe LiveData from the shared ViewModel
        sharedViewModel.selectedRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                // Update the UI with the recipe details
                binding.recipeName.text = it.name
                binding.recipeSummary.text = Html.fromHtml(it.summary, Html.FROM_HTML_MODE_LEGACY)
                binding.recipeSummary.movementMethod = LinkMovementMethod.getInstance()
                binding.cuisineTag.text = it.cuisine
                binding.mealTypeTag.text= it.mealType
                binding.totalTime.text = it.time.toString()

                binding.apply {
                    // Ingredients RecyclerView
                    ingredientsList.layoutManager = LinearLayoutManager(context)
                    ingredientsList.adapter = IngredientsAdapter(recipe.ingredients)

                    // Instructions RecyclerView
                    val flattenedSteps = recipe.instructions.flatMap { it.steps }
                    instructionsList.layoutManager = LinearLayoutManager(context)
                    instructionsList.adapter = InstructionsAdapter(flattenedSteps)

                    // Nutrition RecyclerView
                    nutritionList.layoutManager = LinearLayoutManager(context)
                    nutritionList.adapter = NutrientAdapter(it.nutrients)
                }

                Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.ic_sample_image)
                    .into(binding.imageView)
            }
        }

        // Toggle nutrition list visibility and change the icon
        binding.viewToggleIcon.setOnClickListener {
            if (binding.nutritionList.visibility == View.GONE) {
                // Show the nutrition list
                binding.nutritionList.visibility = View.VISIBLE
                binding.viewToggleIcon.setImageResource(R.drawable.ic_remove)  // Replace with  minus icon
            } else {
                // Hide the nutrition list
                binding.nutritionList.visibility = View.GONE
                binding.viewToggleIcon.setImageResource(R.drawable.ic_add)  // Replace with plus icon
            }
        }


        binding.shoppingBasketIcon.setOnClickListener {
            sharedViewModel.selectedRecipe.observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    val dialogView = layoutInflater.inflate(R.layout.shopping_list_dialog, null)
                    val dialog = AlertDialog.Builder(requireContext()) // Use requireContext()
                        .setView(dialogView)
                        .setCancelable(true) //prevent accidental dismissal
                        .create()

                    val shoppingListContent = dialogView.findViewById<RecyclerView>(R.id.shoppingList)
                    shoppingListContent.layoutManager = LinearLayoutManager(requireContext())
                    shoppingListContent.adapter = IngredientsAdapter(recipe.ingredients)

                    val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
                    val addToShoppingListPageButton = dialogView.findViewById<Button>(R.id.addToShoppingListPageButton)

                    cancelButton.setOnClickListener {
                        dialog.dismiss()
                    }

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
