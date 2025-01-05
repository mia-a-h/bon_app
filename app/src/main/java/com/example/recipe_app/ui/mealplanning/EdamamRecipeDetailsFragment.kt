package com.example.recipe_app.ui.mealplanning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.databinding.FragmentEdamamRecipeDetailsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EdamamRecipeDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class EdamamRecipeDetailsFragment : Fragment() {
    private var _binding: FragmentEdamamRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MealPlanningViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEdamamRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe selected recipe
        sharedViewModel.selectedRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                binding.apply {
                    recipeName.text = it.name
                    mealType.text = it.mealType
                    cookingTime.text = "${it.time} min"

                    Glide.with(this@EdamamRecipeDetailsFragment)
                        .load(it.image)
                        .into(recipeImage)


                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}