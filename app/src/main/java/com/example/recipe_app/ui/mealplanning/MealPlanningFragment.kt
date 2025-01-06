package com.example.recipe_app.ui.mealplanning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.R
import com.example.recipe_app.adapter.MealAdapter
import com.example.recipe_app.model.Meal
import com.example.recipe_app.model.MealPlanResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class MealPlanningFragment : Fragment() {

    private lateinit var spinnerMealsPerDay: Spinner
    private lateinit var spinnerAllergies: Spinner
    private lateinit var spinnerDiets: Spinner
    private lateinit var spinnerCalorieMin: Spinner
    private lateinit var spinnerCalorieMax: Spinner
    private lateinit var submitButton: Button
    private lateinit var mealPlanRecyclerView: RecyclerView
    private lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_planning, container, false)

        // Initialize UI elements
        spinnerMealsPerDay = view.findViewById(R.id.spinner_meals_per_day)
        spinnerAllergies = view.findViewById(R.id.spinner_allergies)
        spinnerDiets = view.findViewById(R.id.spinner_diets)
        spinnerCalorieMin = view.findViewById(R.id.spinner_calorie_min)
        spinnerCalorieMax = view.findViewById(R.id.spinner_calorie_max)
        submitButton = view.findViewById(R.id.submit_button)
        mealPlanRecyclerView = view.findViewById(R.id.mealPlanRecyclerView)

        // Set up RecyclerView
        setupMealPlanRecyclerView()

        // Load mock data for testing
        loadMockMealPlanData()

        // Set click listener for the submit button
        submitButton.setOnClickListener {
            Toast.makeText(requireContext(), "Submit clicked! Replace with real API call.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun setupMealPlanRecyclerView() {
        mealAdapter = MealAdapter(emptyList())
        mealPlanRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mealPlanRecyclerView.adapter = mealAdapter
    }

    private fun loadMockMealPlanData() {
        val mealPlanResponse = loadMockMealPlanResponse()
        mealPlanResponse?.let {
            updateRecyclerView(it.meals)
        } ?: run {
            Toast.makeText(requireContext(), "Failed to load mock meal plan data.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadMockMealPlanResponse(): MealPlanResponse? {
        return try {
            val inputStream = requireContext().assets.open("mock.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = bufferedReader.use { it.readText() }
            Gson().fromJson(jsonString, MealPlanResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateRecyclerView(meals: List<Meal>) {
        mealAdapter.updateData(meals)
    }
}
