package com.example.recipe_app.ui.mealplanning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.R
import com.example.recipe_app.adapter.MealAdapter
import com.example.recipe_app.databinding.FragmentMealPlanningBinding
import com.example.recipe_app.model.*
import com.example.recipe_app.viewmodels.SharedRecipeViewModel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class MealPlanningFragment : Fragment() {

    private var _binding: FragmentMealPlanningBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()
    private val viewModel: MealPlanningViewModel by viewModels()

    private lateinit var spinnerMealsPerDay: Spinner
    private lateinit var spinnerAllergies: Spinner
    private lateinit var spinnerDiets: Spinner
    private lateinit var spinnerCalorieMin: Spinner
    private lateinit var spinnerCalorieMax: Spinner
    private lateinit var submitButton: Button
    private lateinit var mealPlanRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlanningBinding.inflate(inflater, container, false)

        spinnerMealsPerDay = binding.spinnerMealsPerDay
        spinnerAllergies = binding.spinnerAllergies
        spinnerDiets = binding.spinnerDiets
        spinnerCalorieMin = binding.spinnerCalorieMin
        spinnerCalorieMax = binding.spinnerCalorieMax
        submitButton = binding.submitButton
        mealPlanRecyclerView = binding.mealPlanRecyclerView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupButton()



        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerView() {
        // Initialize the adapter with an empty list and set it to the RecyclerView
        val adapter = MealAdapter(emptyList())
        mealPlanRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter // Set the adapter to the RecyclerView
        }
    }

    private fun setupButton() {
        submitButton.setOnClickListener {
            // Load mock data when the button is clicked
            val mockData = loadMockData()

            // Update the RecyclerView adapter with the mock data
            (mealPlanRecyclerView.adapter as? MealAdapter)?.updateData(mockData.meals)

            // Provide user feedback
            Toast.makeText(requireContext(), "Mock data loaded!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadMockData(): MealPlanResponses {
        val jsonString = requireContext().assets.open("mock.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, MealPlanResponses::class.java)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
