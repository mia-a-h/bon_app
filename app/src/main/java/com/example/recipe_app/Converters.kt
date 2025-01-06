package com.example.recipe_app

import androidx.room.TypeConverter
import com.example.recipe_app.model.AnalyzedInstruction
import com.example.recipe_app.model.ExtendedIngredient
import com.example.recipe_app.model.MinMax
import com.example.recipe_app.model.Nutrient
//import com.example.recipe_app.model.SectionResponse
import com.example.recipe_app.model.Step
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson

class Converters {

//    // Convert List<String> to a String (for storage in Room)
//    @TypeConverter
//    fun fromList(value: List<String>): String {
//        return value.joinToString(",")
//    }
//
//    // Convert String to List<String> (for reading from Room)
//    @TypeConverter
//    fun toList(value: String): List<String> {
//        return value.split(",")
//    }

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


        @TypeConverter
        fun fromMapStringMinMax(map: Map<String, MinMax>?): String? {
            return Gson().toJson(map)
        }

        @TypeConverter
        fun toMapStringMinMax(value: String?): Map<String, MinMax>? {
            return Gson().fromJson(value, object : TypeToken<Map<String, MinMax>>() {}.type)
        }

//        @TypeConverter
//        fun fromMapStringSectionResponse(map: Map<String, SectionResponse>?): String? {
//            return Gson().toJson(map)
//        }
//
//        @TypeConverter
//        fun toMapStringSectionResponse(value: String?): Map<String, SectionResponse>? {
//            return Gson().fromJson(value, object : TypeToken<Map<String, SectionResponse>>() {}.type)
//        }
    }

