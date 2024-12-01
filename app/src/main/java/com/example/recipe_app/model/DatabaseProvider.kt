package com.example.recipe_app.model

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile //This annotation ensures that changes to db_instance are immediately visible to all threads.
    //It prevents threads from caching the variable, ensuring the database instance is always up-to-date.
    private var db_instance: RecipeDatabase? = null

    fun getDatabase(context: Context): RecipeDatabase{
        return db_instance ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipe_database"
            ).build()
            db_instance = instance
            instance
        }
    }
}