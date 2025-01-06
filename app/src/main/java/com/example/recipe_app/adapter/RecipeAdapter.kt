package com.example.recipe_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.model.Recipe

class RecipeAdapter(private var recipes: List<Recipe>, private val onItemClicked: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipe_name)
        val cuisineTag: TextView = itemView.findViewById(R.id.cuisine_tag)
        val mealTypeTag: TextView = itemView.findViewById(R.id.meal_type_tag)
        val image: ImageView = itemView.findViewById(R.id.recipe_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]  //which recipe i'm on
        holder.recipeName.text = recipe.name
        holder.cuisineTag.text = recipe.cuisine
        holder.mealTypeTag.text = recipe.mealType

        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .placeholder(R.drawable.ic_sample_image)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClicked(recipe)
        }
    }

    override fun getItemCount() = recipes.size

    fun updateData(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
        println("RecipeAdapter: Updated with ${newRecipes.size} recipes")
    }


}
