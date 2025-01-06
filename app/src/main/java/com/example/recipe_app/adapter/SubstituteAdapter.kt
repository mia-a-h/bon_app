package com.example.recipe_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.R

class SubstituteAdapter(private val substitutes: List<String>) :
    RecyclerView.Adapter<SubstituteAdapter.SubstituteViewHolder>() {

    inner class SubstituteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val substituteName: TextView = itemView.findViewById(R.id.substituteName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubstituteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.substitute_item, parent, false)
        return SubstituteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubstituteViewHolder, position: Int) {
        holder.substituteName.text = substitutes[position]
    }

    override fun getItemCount(): Int = substitutes.size
}
