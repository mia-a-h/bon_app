package com.example.recipe_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_app.network.EdamamApiService
import com.example.recipe_app.model.*
import kotlinx.coroutines.launch
import retrofit2.Response

class MealPlannerViewModel(
    private val apiService: EdamamApiService,
    private val appId: String,
    private val appKey: String
) : ViewModel() {

    private val _mealPlan = MutableLiveData<MealPlanResponse>()
    val mealPlan: LiveData<MealPlanResponse> = _mealPlan

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getMealPlan(request: MealPlanRequest) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = apiService.getMealPlan(appId, appKey, listOf("public"),request)

                handleResponse(response)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun handleResponse(response: Response<MealPlanResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                _mealPlan.value = it
            } ?: run {
                _error.value = "Empty response"
            }
        } else {
            _error.value = "Error: ${response.code()} - ${response.message()}"
        }
    }
}