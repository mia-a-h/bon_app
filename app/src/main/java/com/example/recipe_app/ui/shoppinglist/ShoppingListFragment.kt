package com.example.recipe_app.ui

import ShoppingListViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_app.ui.adapters.ShoppingListAdapter
import com.example.recipe_app.databinding.FragmentShoppingListBinding
import com.google.firebase.auth.FirebaseAuth

class ShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListBinding
    private val shoppingListViewModel: ShoppingListViewModel by activityViewModels()
    private lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false)

        // Initialize the RecyclerView adapter
        shoppingListAdapter = ShoppingListAdapter()

        binding.recyclerViewShoppingLists.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewShoppingLists.adapter = shoppingListAdapter

        // Get the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Observe shopping lists from ViewModel
        shoppingListViewModel.shoppingLists.observe(viewLifecycleOwner) { shoppingLists ->
            if (shoppingLists.isEmpty()) {
                binding.noShoppingListsTextView.visibility = View.VISIBLE
                binding.recyclerViewShoppingLists.visibility = View.GONE
            } else {
                binding.noShoppingListsTextView.visibility = View.GONE
                binding.recyclerViewShoppingLists.visibility = View.VISIBLE
                shoppingListAdapter.submitList(shoppingLists)
            }
        }

        // Fetch shopping lists
        shoppingListViewModel.getShoppingList(userId)

        return binding.root
    }
}

