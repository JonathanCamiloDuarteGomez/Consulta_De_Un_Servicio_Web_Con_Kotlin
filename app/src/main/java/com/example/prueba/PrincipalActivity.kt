package com.example.prueba


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PrincipalActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    // Lista mutable para almacenar las frutas deserializadas
    var listaDeserializada: MutableList<FrutasResponse> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        // Obtener referencia al SearchView y configurar el listener
        val txtBucar = findViewById<SearchView>(R.id.txtBuscar)
        txtBucar.setOnQueryTextListener(this)

        // Referencia al contenedor LinearLayout
        val contentButton = findViewById<LinearLayout>(R.id.contentButton)

        // Obtener la lista de frutas deserializadas de la actividad anterior
        val listaFrutas: String = intent.extras?.getString("LISTA").orEmpty()

        // Verificar si la lista de frutas no está vacía
        if (listaFrutas.isNotEmpty()) {
            val gson = Gson()

            // Deserializar la lista de frutas usando Gson
            listaDeserializada = gson.fromJson(listaFrutas, object : TypeToken<List<FrutasResponse>>() {}.type)

            // Iterar sobre la lista de frutas deserializadas
            listaDeserializada.forEach { item ->
                // Imprimir el nombre de la fruta en la consola
                println("Fruta: $item.name")

                // Crear un nuevo botón
                val miBoton = Button(this)
                miBoton.text = item.name

                // Configurar el listener del botón para abrir la actividad DetalleFrutaActivity al hacer clic
                miBoton.setOnClickListener {
                    clickButton(item)
                }

                // Agregar el botón al contenedor LinearLayout
                contentButton.addView(miBoton)
            }
        }

        // Llamada a la función crearSpinner para generar un Spinner dinámico y manejar la opción seleccionada
        val listaOpciones = listOf("Select", "Calorias", "Fat", "Sugar", "Carbohydrates", "Protein")
        val spinnerId = R.id.orderBy
        crearSpinner(this, findViewById(android.R.id.content), listaOpciones, spinnerId) { selectedOption ->
            Log.d("opciones", selectedOption)
            // Manejar la opción seleccionada
            when (selectedOption) {
                "Select" -> {ordenarFruta("Select")}
                "Calorias" -> { ordenarFruta("Calorias") }
                "Fat" -> { ordenarFruta("Fat") }
                "Sugar" -> { ordenarFruta("Sugar") }
                "Carbohydrates" -> { ordenarFruta("Carbohydrates") }
                "Protein" -> { ordenarFruta("Protein") }
                else -> {
                    ordenarFruta("Select") // Ordenar por defecto si la opción no coincide
                    // Manejar el caso cuando no coincide con ninguna opción
                }
            }
        }
    }

    // Función para crear un Spinner dinámico y agregarlo al diseño XML
    fun crearSpinner(context: Context, view: View, listaOpciones: List<String>, spinnerId: Int, callback: (String) -> Unit) {
        val spinnerDinamico = view.findViewById<Spinner>(spinnerId)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listaOpciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDinamico.adapter = adapter

        spinnerDinamico.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = listaOpciones[position]
                Log.d("opciones", selectedOption)
                callback(selectedOption) // Llama a la función de devolución de llamada
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Se llama cuando no se selecciona ningún elemento.
            }
        }
    }

    // Función para manejar el clic en el botón de la fruta
    fun clickButton(fruta:FrutasResponse){
        var intentFruta = Intent(this, DetalleFrutaActivity::class.java)
        val gson = Gson()
        val jsonCadena: String = gson.toJson(fruta)
        intentFruta.putExtra("FRUTA", jsonCadena)
        startActivity(intentFruta)
    }

    // Función para buscar una fruta por texto
    fun BuscarFruta(texto:String){
        var listaFiltrada: List<FrutasResponse> = listaDeserializada
        if (texto.isNotEmpty()){
            listaFiltrada= listaDeserializada.filter { it.name.lowercase().contains(texto.lowercase(), ignoreCase = true) }
        }

        val contentButton = findViewById<LinearLayout>(R.id.contentButton)

        // Bñorrar el contenido del LinearLayout
        contentButton.removeAllViews()

        // Crear botones para las frutas filtradas
        listaFiltrada.forEach{item  ->
            val miBoton = Button(this)
            miBoton.text = item.name

            miBoton.setOnClickListener {
                clickButton(item)
            }

            contentButton.addView(miBoton)
        }
    }

    // Función para ordenar las frutas por una propiedad específica
    fun ordenarFruta(txt: String) {
        var listaFiltrada: List<FrutasResponse> = listaDeserializada

        listaFiltrada = when (txt) {
            "Select" -> listaFiltrada
            "Calorias" -> listaDeserializada.sortedByDescending { it.nutritions.calories }
            "Fat" -> listaDeserializada.sortedByDescending { it.nutritions.fat }
            "Sugar" -> listaDeserializada.sortedByDescending { it.nutritions.sugar }
            "Carbohydrates" -> listaDeserializada.sortedByDescending { it.nutritions.carbohydrates }
            "Protein" -> listaDeserializada.sortedByDescending { it.nutritions.protein }
            else -> listaDeserializada // Si el texto no coincide, devuelve la lista original
        }

        val contentButton = findViewById<LinearLayout>(R.id.contentButton)

        // Borrar el contenido del LinearLayout
        contentButton.removeAllViews()

        // Crear botones con las frutas ordenadas
        listaFiltrada.forEach { item ->
            val miBoton = Button(this)
            miBoton.text = item.name

            miBoton.setOnClickListener {
                clickButton(item)
            }

            contentButton.addView(miBoton)
        }
    }

    // Funciones para el SearchView
    override fun onQueryTextSubmit(query: String?): Boolean {
        BuscarFruta(query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        BuscarFruta(newText.toString())
        return true
    }
}
