package com.example.recipe_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipe_app.network.EdamamRetrofitClient
import com.example.recipe_app.viewmodels.MealPlannerViewModel

class MealPlannerViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealPlannerViewModel::class.java)) {
            val apiService = EdamamRetrofitClient.api
            val appId = "your_app_id"
            val appKey = "your_app_key"
            @Suppress("UNCHECKED_CAST")
            return MealPlannerViewModel(apiService, appId, appKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}