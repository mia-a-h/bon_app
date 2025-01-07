package com.example.recipe_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.databinding.ShoppinglistItemBinding
import com.example.recipe_app.model.ShoppingList

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private var shoppingLists = listOf<ShoppingList>()

    // Update the list of shopping lists
    fun submitList(lists: List<ShoppingList>) {
        shoppingLists = lists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ShoppinglistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]
        holder.bind(shoppingList)
    }

    override fun getItemCount(): Int {
        return shoppingLists.size
    }

    inner class ShoppingListViewHolder(private val binding: ShoppinglistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shoppingList: ShoppingList) {
            // Display recipe name and ingredients in each shopping list item
            binding.recipeNameTextView.text = shoppingList.recipeId // You can customize this as per your structure

            // Bind the ingredients
            val ingredientsText = shoppingList.ingredients.joinToString("\n") { "${it.name}: ${it.amount}" }
            binding.ingredientsTextView.text = ingredientsText
        }
    }
}
