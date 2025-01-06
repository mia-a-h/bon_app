package com.example.recipe_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.model.Meal

class MealAdapter(private var meals: List<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
        private val recipeName: TextView = itemView.findViewById(R.id.recipe_name)
        private val cuisineTag: TextView = itemView.findViewById(R.id.cuisine_tag)
        private val mealTypeTag: TextView = itemView.findViewById(R.id.meal_type_tag)

        fun bind(meal: Meal) {
            recipeName.text = meal.recipe.name.orEmpty()
            cuisineTag.text = ""
            mealTypeTag.text = meal.mealType

            // Load recipe image using Glide
            Glide.with(itemView.context)
                .load(meal.recipe.image.orEmpty())
                .placeholder(R.drawable.ic_sample_image) // Default placeholder
                .into(recipeImage)

            itemView.setOnClickListener {
                // Handle item click if needed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    fun updateData(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}
