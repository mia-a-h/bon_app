package com.example.recipe_app

import androidx.room.TypeConverter

class Converters {

    // Convert List<String> to a String (for storage in Room)
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    // Convert String to List<String> (for reading from Room)
    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",")
    }
}
