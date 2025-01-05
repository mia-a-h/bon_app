package com.example.recipe_app.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.databinding.ItemMealPlanBinding
import com.example.recipe_app.model.Recipe

class MealPlanAdapter(
    private var meals: List<Recipe> = emptyList(),
    private val onMealClick: (Recipe) -> Unit
) : RecyclerView.Adapter<MealPlanAdapter.MealViewHolder>() {

    class MealViewHolder(
        private val binding: ItemMealPlanBinding,
        private val onMealClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.apply {
                mealTitle.text = recipe.name
                mealType.text = recipe.mealType
                cookingTime.text = "${recipe.time} min"

                Glide.with(root.context)
                    .load(recipe.image)
                    .placeholder(R.drawable.ic_meal_placeholder)
                    .into(mealImage)

                root.setOnClickListener { onMealClick(recipe) }
            }
        }
    }

    // Creates new ViewHolder when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        // Inflate the item layout
        val binding = ItemMealPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealViewHolder(binding, onMealClick)
    }

    // Binds data to ViewHolder at specific position
    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    // Returns total number of items
    override fun getItemCount() = meals.size

    // Updates the list of meals and refreshes the RecyclerView
    fun updateMeals(newMeals: List<Recipe>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}