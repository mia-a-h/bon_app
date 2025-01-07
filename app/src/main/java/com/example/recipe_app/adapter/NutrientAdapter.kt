package com.example.recipe_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.R
import com.example.recipe_app.model.ExtendedIngredient
import com.example.recipe_app.model.Nutrient

class NutrientAdapter(private val nutrients: List<Nutrient>) : RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>() {

    class NutrientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nutrientName: TextView = itemView.findViewById(R.id.nutrientName)
        val nutrientAmount: TextView = itemView.findViewById(R.id.nutrientQuantity)
        val nutrientUnit: TextView = itemView.findViewById(R.id.nutrientUnit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nutrient_item, parent, false)
        return NutrientViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val nutrient = nutrients[position]
        holder.nutrientName.text = nutrient.name
        holder.nutrientAmount.text = nutrient.amount.toString()
        holder.nutrientUnit.text = nutrient.unit
    }

    override fun getItemCount(): Int = nutrients.size
}
