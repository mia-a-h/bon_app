package com.example.recipe_app


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao{
    @Insert
    suspend fun insert(user: User)

    @Insert
    suspend fun insertAll(user: List<User>)
    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM Users ORDER BY timestamp DESC")
    fun getAllUsers(): List<User>

}