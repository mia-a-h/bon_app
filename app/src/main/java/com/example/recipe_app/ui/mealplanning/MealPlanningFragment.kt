package com.example.recipe_app.ui.mealplanning
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_app.R
import com.example.recipe_app.adapter.MealPlanAdapter
import com.example.recipe_app.databinding.FragmentMealPlanningBinding
import com.example.recipe_app.model.Accept
import com.example.recipe_app.model.MealPlanRequest
import com.example.recipe_app.model.MinMax
import com.example.recipe_app.model.Plan
import com.example.recipe_app.model.SectionRequest
import com.example.recipe_app.ui.home.SharedRecipeViewModel
import com.google.android.material.tabs.TabLayout

class MealPlanningFragment : Fragment() {

    private var _binding: FragmentMealPlanningBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()
    private val viewModel: MealPlanningViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlanningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupRecyclerView()
        setupAddMealButton()

        // Observe loading state
        viewModel.planningStatus.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe errors
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupTabs() {
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        days.forEach { day ->
            binding.dayTabLayout.addTab(binding.dayTabLayout.newTab().setText(day))
        }

        binding.dayTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.loadMealsForDay(it.position)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        val mealPlanAdapter = MealPlanAdapter(
            onMealClick = { recipe ->
                viewModel.setSelectedRecipe(recipe)
                findNavController().navigate(
                    R.id.action_mealPlanningFragment_to_edamamRecipeDetailsFragment
                )
            }
        )

        binding.mealsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mealPlanAdapter
        }

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            mealPlanAdapter.updateMeals(meals)  // Call on instance, not class
        }
    }


    private fun setupAddMealButton() {
        binding.addMealButton.setOnClickListener {
            val request = MealPlanRequest(
                size = 7,
                plan = Plan(
                    accept = Accept(
                        all = listOf(
                            mapOf("health" to listOf("balanced"))
                        )
                    ),
                    fit = mapOf(
                        Plan.ENERGY_KCAL to MinMax(min = 1000, max = 2000)
                    ),
                    exclude = null,
                    sections = mapOf(
                        "Breakfast" to SectionRequest(
                            accept = Accept(
                                all = listOf(
                                    mapOf("meal" to listOf("breakfast"))
                                )
                            ),
                            fit = mapOf(
                                Plan.ENERGY_KCAL to MinMax(min = 300, max = 500)
                            )
                        ),
                        "Lunch" to SectionRequest(
                            accept = Accept(
                                all = listOf(
                                    mapOf("meal" to listOf("lunch/dinner"))
                                )
                            ),
                            fit = mapOf(
                                Plan.ENERGY_KCAL to MinMax(min = 400, max = 600)
                            )
                        ),
                        "Dinner" to SectionRequest(
                            accept = Accept(
                                all = listOf(
                                    mapOf("meal" to listOf("lunch/dinner"))
                                )
                            ),
                            fit = mapOf(
                                Plan.ENERGY_KCAL to MinMax(min = 400, max = 700)
                            )
                        )
                    )
                )
            )
            viewModel.createMealPlan(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}