package com.example.recipe_app.dbprovider

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipe_app.Converters
import com.example.recipe_app.dao.RecipeDao
import com.example.recipe_app.model.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract  class RecipeDatabase: RoomDatabase(){
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile //This annotation ensures that changes to db_instance are immediately visible to all threads.
        //It prevents threads from caching the variable, ensuring the database instance is always up-to-date.
        private var db_instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
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
}