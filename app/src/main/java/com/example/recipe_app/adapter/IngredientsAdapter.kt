package com.example.recipe_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.R
import com.example.recipe_app.model.ExtendedIngredient

class IngredientsAdapter(private val ingredients: List<ExtendedIngredient>
, private val onIngredientClick: (Int) -> Unit)
    : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {
    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.ingredientName)
        val ingredientQuantity: TextView = itemView.findViewById(R.id.ingredientQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient.nameClean
        holder.ingredientQuantity.text = ingredient.amount.toString()

        holder.itemView.setOnClickListener {
            onIngredientClick(position)
        }
    }

    override fun getItemCount(): Int = ingredients.size
}
