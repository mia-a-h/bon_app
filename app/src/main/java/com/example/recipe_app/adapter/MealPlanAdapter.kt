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

    /*A list of Recipe objects (representing meals). This is the data source for the adapter.
    b, it’s l awwal it's an empty list (emptyList()).*/
    private val onMealClick: (Recipe) -> Unit /*A higher-order function lambda passed as a parameter to handle what happens when a meal is clicked.*/
) : RecyclerView.Adapter<MealPlanAdapter.MealViewHolder>() { // uses the  mealviewholder that offers the data that needs to be used by the adapter


    class MealViewHolder(
        private val binding: ItemMealPlanBinding, /* provides easy access to the views in the layout.*/
        private val onMealClick: (Recipe) -> Unit //unit mtl void

    private val onMealClick: (Recipe) -> Unit
) : RecyclerView.Adapter<MealPlanAdapter.MealViewHolder>() {

    class MealViewHolder(
        private val binding: ItemMealPlanBinding,
        private val onMealClick: (Recipe) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.apply {

                mealTitle.text = recipe.name //sets the meal title name
                mealType.text = recipe.mealType //sets the type eza dinner or lunch
                cookingTime.text = "${recipe.time} min" //sets the time

                mealTitle.text = recipe.name
                mealType.text = recipe.mealType
                cookingTime.text = "${recipe.time} min"


                Glide.with(root.context)
                    .load(recipe.image)
                    .placeholder(R.drawable.ic_meal_placeholder)
                    .into(mealImage)

                root.setOnClickListener { onMealClick(recipe) }

                /*Adds a click listener to the entire row.
                When clicked, it calls the onMealClick lambda, passing the clicked Recipe object.*/

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