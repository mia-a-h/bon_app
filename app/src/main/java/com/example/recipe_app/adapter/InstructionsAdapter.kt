package com.example.recipe_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe_app.R
import com.example.recipe_app.model.AnalyzedInstruction
import com.example.recipe_app.model.Step

class InstructionsAdapter(private val instructions: List<Step>) : RecyclerView.Adapter<InstructionsAdapter.InstructionViewHolder>() {

    class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instructionNumber: TextView = itemView.findViewById(R.id.instruction_number)
        val instructionStep: TextView = itemView.findViewById(R.id.instruction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instruction_item, parent, false)
        return InstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val step = instructions[position]
        holder.instructionNumber.text = step.number.toString()
        holder.instructionStep.text = step.step
    }

    override fun getItemCount(): Int = instructions.size
}
