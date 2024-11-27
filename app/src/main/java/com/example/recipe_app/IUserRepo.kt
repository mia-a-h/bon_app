package com.example.recipe_app

interface IUserRepo {
    suspend fun getUsers(): List<User>
}