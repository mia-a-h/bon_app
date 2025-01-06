package com.example.recipe_app

import androidx.room.TypeConverter
import com.example.recipe_app.model.AnalyzedInstruction
import com.example.recipe_app.model.ExtendedIngredient
import com.example.recipe_app.model.Nutrient
import com.example.recipe_app.model.Step
import com.google.gson.Gson

class Converters {

    // Converts List<String> to a comma-separated String
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    // Converts a comma-separated String back to List<String>
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    // Converts List<ExtendedIngredient> to a JSON string
    @TypeConverter
    fun fromExtendedIngredientList(value: List<ExtendedIngredient>?): String? {
        return Gson().toJson(value)
    }

    // Converts a JSON string back to List<ExtendedIngredient>
    @TypeConverter
    fun toExtendedIngredientList(value: String?): List<ExtendedIngredient>? {
        return Gson().fromJson(value, Array<ExtendedIngredient>::class.java).toList()
    }

    // Converts List<Nutrient> to a JSON string
    @TypeConverter
    fun fromNutrientList(value: List<Nutrient>?): String? {
        return Gson().toJson(value)
    }

    // Converts a JSON string back to List<Nutrient>
    @TypeConverter
    fun toNutrientList(value: String?): List<Nutrient> {
        return Gson().fromJson(value, Array<Nutrient>::class.java).toList()
    }

    @TypeConverter
    fun fromAnalyzedInstructionList(list: List<AnalyzedInstruction>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toAnalyzedInstructionList(json: String): List<AnalyzedInstruction> {
        return Gson().fromJson(json, Array<AnalyzedInstruction>::class.java).toList()
    }
}
