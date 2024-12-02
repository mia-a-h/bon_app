package com.example.recipe_app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_app.dbprovider.RecipeDatabase
import com.example.recipe_app.adapter.RecipeAdapter
import com.example.recipe_app.viewmodels.RecipeViewModel
import com.example.recipe_app.factory.RecipeViewModelFactory
import com.example.recipe_app.databinding.FragmentHomeBinding
import com.example.recipe_app.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var searchJob: Job? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by viewModels {
        val applicationContext = requireContext().applicationContext // Ensure it's non-null
        RecipeViewModelFactory(RecipeRepository(RecipeDatabase.getDatabase(applicationContext).recipeDao()))
    }
    private lateinit var recommendedAdapter: RecipeAdapter
    private lateinit var popularAdapter: RecipeAdapter
    private lateinit var filterAdapter: RecipeAdapter
    private lateinit var searchAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.titleSearchResults.visibility = View.GONE
        binding.recyclerViewSearchResults.visibility = View.GONE

        recommendedAdapter = RecipeAdapter(emptyList())
        popularAdapter = RecipeAdapter(emptyList())
        filterAdapter = RecipeAdapter(emptyList())
        searchAdapter = RecipeAdapter(emptyList())

        binding.recyclerViewRecommended.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewRecommended.adapter = recommendedAdapter

        binding.recyclerViewPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopular.adapter = popularAdapter

        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewSearchResults.adapter = searchAdapter

        // Observe the LiveData from your ViewModel and update the adapters
        recipeViewModel.recommendedRecipes.observe(viewLifecycleOwner) { recommendedRecipes ->
            recommendedAdapter.updateData(recommendedRecipes)
        }

        recipeViewModel.popularRecipes.observe(viewLifecycleOwner) { popularRecipes ->
            popularAdapter.updateData(popularRecipes)
        }

        recipeViewModel.searchedRecipes.observe(viewLifecycleOwner) { searchResults ->
            if (searchResults.isNotEmpty()) {
                searchAdapter.updateData(searchResults)
                showSearchResults()
            } else {
                showDefaultViews()
                Toast.makeText(requireContext(), "No results found", Toast.LENGTH_LONG).show()
            }
        }

        // Fetch recommended and popular recipes
        recipeViewModel.fetchRecommendedRecipes()
        recipeViewModel.fetchPopularRecipes()

        binding.spinnerCuisineType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCuisine = parent?.getItemAtPosition(position).toString()
                val selectedMeal = binding.spinnerMealType.selectedItem.toString()
                Log.d("home fragment function", selectedCuisine)
                recipeViewModel.fetchFilteredRecipes(selectedCuisine, selectedMeal)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerMealType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMeal = parent?.getItemAtPosition(position).toString()
                val selectedCuisine = binding.spinnerCuisineType.selectedItem.toString()
                Log.d("home fragment function", selectedMeal)
                recipeViewModel.fetchFilteredRecipes(selectedCuisine, selectedMeal)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (searchAdapter.itemCount > 0) {
                        // If there are already results, no need to re-trigger the search
                        binding.searchView.clearFocus()
                    } else {
                        Log.d("Search", "Search submitted: $it")
                        recipeViewModel.searchRecipes(query = it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()

                newText?.let { query ->
                    searchJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500) // Wait for 500ms before executing the search
                        if (query.isNotBlank()) {
                            Log.d("Search", "Query changed: $query")
                            recipeViewModel.searchRecipes(query = query)
                        } else {
                            showDefaultViews()
                        }
                    }
                }
                return true
            }
        })


        recipeViewModel.filteredRecipes.observe(viewLifecycleOwner) { filteredRecipes ->
            Log.d("updating...", filteredRecipes.toString())
            Log.d("HomeFragment", "Filtered recipes: $filteredRecipes")
            recommendedAdapter.updateData(filteredRecipes)
            popularAdapter.updateData(filteredRecipes) // Or use separate logic if you want different filters for popular recipes
        }


        return binding.root
    }

    private fun showDefaultViews(){
        _binding?.let { binding ->
            binding.titleSearchResults.visibility = View.GONE
            binding.recyclerViewSearchResults.visibility = View.GONE
            binding.titleRecommended.visibility = View.VISIBLE
            binding.recyclerViewRecommended.visibility = View.VISIBLE
            binding.titlePopular.visibility = View.VISIBLE
            binding.recyclerViewPopular.visibility = View.VISIBLE
        } ?: Log.e("HomeFragment", "Default Views: Binding is null")
    }

    private fun showSearchResults(){
        _binding?.let { binding -> // Use the non-null binding here
            binding.titleSearchResults.visibility = View.VISIBLE
            binding.recyclerViewSearchResults.visibility = View.VISIBLE
            binding.titleRecommended.visibility = View.GONE
            binding.recyclerViewRecommended.visibility = View.GONE
            binding.titlePopular.visibility = View.GONE
            binding.recyclerViewPopular.visibility = View.GONE
        } ?: Log.e("HomeFragment", "Search Results: Binding is null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

