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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_app.model.DatabaseProvider
import com.example.recipe_app.RecipeAdapter
import com.example.recipe_app.RecipeViewModel
import com.example.recipe_app.RecipeViewModelFactory
import com.example.recipe_app.databinding.FragmentHomeBinding
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.repository.RecipeRepository

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by viewModels {
        val applicationContext = requireContext().applicationContext // Ensure it's non-null
        RecipeViewModelFactory(RecipeRepository(DatabaseProvider.getDatabase(applicationContext).recipeDao()))
    }
    //private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recommendedAdapter: RecipeAdapter
    private lateinit var popularAdapter: RecipeAdapter
    private lateinit var filterAdapter: RecipeAdapter
    private lateinit var searchAdapter: RecipeAdapter

    //private lateinit var recipeList: MutableList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.titleSearchResults.visibility = View.GONE
        binding.recyclerViewSearchResults.visibility = View.GONE
        //recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
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
                binding.titleSearchResults.visibility = View.VISIBLE
                binding.recyclerViewSearchResults.visibility = View.VISIBLE
                binding.recyclerViewRecommended.visibility = View.GONE
                binding.recyclerViewPopular.visibility = View.GONE
                searchAdapter.updateData(searchResults)
            } else {
                binding.titleSearchResults.visibility = View.GONE
                binding.recyclerViewSearchResults.visibility = View.GONE
                binding.recyclerViewRecommended.visibility = View.VISIBLE
                binding.recyclerViewPopular.visibility = View.VISIBLE
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
                recipeViewModel.fetchFilteredRecipes(selectedCuisine, selectedMeal)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerMealType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMeal = parent?.getItemAtPosition(position).toString()
                val selectedCuisine = binding.spinnerCuisineType.selectedItem.toString()
                recipeViewModel.fetchFilteredRecipes(selectedCuisine, selectedMeal)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Trigger search in ViewModel
                    Log.d("Search", "Search submitted: $it")
                    recipeViewModel.searchRecipes(query = it)
                    // Optionally clear focus after search
                    binding.searchView.clearFocus()
                    binding.titleSearchResults.visibility = View.VISIBLE
                    binding.recyclerViewSearchResults.visibility = View.VISIBLE
                    binding.recyclerViewRecommended.visibility = View.GONE
                    binding.recyclerViewPopular.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isNotBlank()) {
                        Log.d("Search", "Query changed: $it")
                        // Live updates as user types
                        recipeViewModel.searchRecipes(query = it)
                    }
                }
                return true
            }
        })


        recipeViewModel.filteredRecipes.observe(viewLifecycleOwner) { filteredRecipes ->
            recommendedAdapter.updateData(filteredRecipes)
            popularAdapter.updateData(filteredRecipes) // Or use separate logic if you want different filters for popular recipes
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

