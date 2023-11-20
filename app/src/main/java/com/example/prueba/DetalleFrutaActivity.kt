package com.example.prueba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetalleFrutaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_fruta)

        val frutas:String = intent.extras?.getString("FRUTA").orEmpty()

        val name = findViewById<TextView>(R.id.name)
        val family = findViewById<TextView>(R.id.family)
        val genus = findViewById<TextView>(R.id.genus)

        val calories = findViewById<TextView>(R.id.calories)
        val fat = findViewById<TextView>(R.id.fat)
        val sugar = findViewById<TextView>(R.id.sugar)
        val carbohydrates = findViewById<TextView>(R.id.carbohydrates)
        val protein = findViewById<TextView>(R.id.protein)

        if ( frutas.isNotEmpty()){
            val gson = Gson()

            val fruraDeserializada: FrutasResponse = gson.fromJson(frutas, object : TypeToken<FrutasResponse>() {}.type)

            name.text = fruraDeserializada.name
            family.text = "Family: ${fruraDeserializada.family}"
            genus.text = "Genus: ${fruraDeserializada.genus}"

            calories.text = "Calories: ${fruraDeserializada.nutritions.calories}"
            fat.text = "Fat: ${fruraDeserializada.nutritions.fat}"
            sugar.text = "Sugar: ${fruraDeserializada.nutritions.sugar}"
            carbohydrates.text = "Carbohydrates: ${fruraDeserializada.nutritions.carbohydrates}"
            protein.text = "Protein: ${fruraDeserializada.nutritions.protein}"
        }

    }

}