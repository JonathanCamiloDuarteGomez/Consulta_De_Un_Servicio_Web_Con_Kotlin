package com.example.prueba

//import com.google.gson.annotations.SerializedName
import java.util.Objects

//data class FrutasResponse(var lista: List<Objects>)



data class FrutasResponse(
    val id: Int,
    val family: String,
    val order: String,
    val genus: String,
    val name: String,
    val nutritions: objNutritions ,
)

data class objNutritions(

    val calories :Float,
    val fat :Float,
    val sugar :Float,
    val carbohydrates :Float,
    val protein :Float,
)