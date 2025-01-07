package com.example.recipe_app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_app.R
import com.example.recipe_app.dbprovider.RecipeDatabase
import com.example.recipe_app.adapter.RecipeAdapter
import com.example.recipe_app.viewmodels.RecipeViewModel
import com.example.recipe_app.factory.RecipeViewModelFactory
import com.example.recipe_app.databinding.FragmentHomeBinding
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.repository.RecipeRepository
import com.example.recipe_app.viewmodels.SharedRecipeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by viewModels {
        val applicationContext = requireContext().applicationContext // Ensure it's non-null
        RecipeViewModelFactory(
            RecipeRepository(RecipeDatabase.getDatabase(applicationContext).recipeDao())
        )
    }

    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()

    private fun navigateToRecipeDetails(recipe: Recipe) {
        sharedViewModel.selectRecipe(recipe) // Set the selected recipe in the ViewModel
        findNavController().navigate(R.id.action_homeFragment_to_recipeDetailsFragment)
    }


    private lateinit var recommendedAdapter: RecipeAdapter
    private lateinit var popularAdapter: RecipeAdapter
    private lateinit var searchAdapter: RecipeAdapter

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.titleSearchResults.visibility = View.GONE
        binding.recyclerViewSearchResults.visibility = View.GONE

        initAdapters()
        initRecyclerViews()
        initObservers()

        fetchInitialRecipes()

        initSpinners()

        initSearchView()

        binding.jokeButton.setOnClickListener{
            handleButtonClick()
        }

        return binding.root
    }

    private fun initAdapters(){
        recommendedAdapter = RecipeAdapter(emptyList()) { recipe ->
            navigateToRecipeDetails(recipe)
        }
        popularAdapter = RecipeAdapter(emptyList()) { recipe ->
            navigateToRecipeDetails(recipe)
        }
        searchAdapter = RecipeAdapter(emptyList()) { recipe ->
            navigateToRecipeDetails(recipe)
        }
    }

    private fun initRecyclerViews() {
        binding.recyclerViewRecommended.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }

        binding.recyclerViewPopular.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
        }

        binding.recyclerViewSearchResults.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }

    private fun initObservers(){
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
    }

    private fun fetchInitialRecipes(){
        // Fetch recommended and popular recipes
        recipeViewModel.fetchRecommendedRecipes(null)
        recipeViewModel.fetchPopularRecipes(null)
    }

    private fun initSpinners() {
        binding.spinnerCuisineType.onItemSelectedListener = createFilterListener()
        binding.spinnerMealType.onItemSelectedListener = createFilterListener()
    }

    private fun createFilterListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSectionsBasedOnFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateSectionsBasedOnFilters() {
        val selectedCuisine = binding.spinnerCuisineType.selectedItem.toString().lowercase()
        val selectedMealType = binding.spinnerMealType.selectedItem.toString().lowercase()
        Log.d("filters", "$selectedCuisine, $selectedMealType")

        // Create tags string: combine non-null filters separated by commas
        val tags = listOfNotNull(
            if (selectedCuisine != "all") selectedCuisine else null,
            if (selectedMealType != "all") selectedMealType else null
        ).joinToString(",").ifEmpty { "all, all" }

        Log.d("tags", tags)
        recipeViewModel.fetchRecommendedRecipes(tags) // Pass null if no filters
        recipeViewModel.fetchPopularRecipes(tags) // Pass null if no filters
        //recipeViewModel.fetchRecommendedRecipes(tags.ifEmpty { null }) // Pass null if no filters
        //recipeViewModel.fetchPopularRecipes(tags.ifEmpty { null }) // Pass null if no filters
    }

    private fun initSearchView(){
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
    }

    private fun showDefaultViews(){
        _binding?.let { binding ->
            binding.titleSearchResults.visibility = View.GONE
            binding.recyclerViewSearchResults.visibility = View.GONE
            binding.titleRecommended.visibility = View.VISIBLE
            binding.recyclerViewRecommended.visibility = View.VISIBLE
            binding.titlePopular.visibility = View.VISIBLE
            binding.recyclerViewPopular.visibility = View.VISIBLE
            binding.spinnerCuisineType.visibility = View.VISIBLE
            binding.spinnerMealType.visibility = View.VISIBLE
            binding.joke.visibility = View.VISIBLE
            binding.jokeButton.visibility = View.VISIBLE
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
            binding.spinnerCuisineType.visibility = View.GONE
            binding.spinnerMealType.visibility = View.GONE
            binding.joke.visibility = View.GONE
            binding.jokeButton.visibility = View.GONE
        } ?: Log.e("HomeFragment", "Search Results: Binding is null")
    }

    private fun handleButtonClick(){
        recipeViewModel.fetchJoke()
        val dialogView = layoutInflater.inflate(R.layout.joke_trivia_dialog, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val jokeTextView: TextView = dialogView.findViewById(R.id.jktrText)
        // Observe the joke LiveData to update the TextView
        recipeViewModel.joke.observe(viewLifecycleOwner) { joke ->
            if (joke != null) {
                jokeTextView.text = joke
            } else {
                jokeTextView.text = "Couldn't fetch a joke. Please try again!"
            }
        }

        val closeButton: Button = dialogView.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

