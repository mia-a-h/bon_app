package com.example.recipe_app.utils

import com.example.recipe_app.model.IngredientSub
import com.example.recipe_app.model.SubstituteResponse

fun mapToLocalSub(apiSub: SubstituteResponse): IngredientSub{
    return IngredientSub(
        ingredient = apiSub.ingredient,
        message = apiSub.message,
        substitutes = apiSub.substitutes
    )
}