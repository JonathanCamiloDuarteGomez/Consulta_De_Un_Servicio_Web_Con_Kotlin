package com.example.prueba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson

// Importaciones necesarias
class BuscadorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        // Llamar a la función para ejecutar la API y obtener datos de frutas
        CorreAPI()
    }

    // Función para realizar la llamada a la API y obtener datos de frutas
    fun CorreAPI() {
        // Crear un intent para pasar los datos obtenidos a la actividad PrincipalActivity
        var intentPrincipal = Intent(this, PrincipalActivity::class.java)

        // Ejecutar la llamada a la API usando Coroutines para realizar operaciones asíncronas
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Realizar la solicitud GET a la API para obtener datos de frutas
                val call = getRetrofit().create(APIService::class.java).getFrutasByNombre("all")
                val puppies = call.body()

                // Verificar si la solicitud fue exitosa
                if (call.isSuccessful) {
                    // Mostrar RecyclerView si la llamada fue exitosa
                    val lista: List<FrutasResponse> = puppies ?: emptyList()
                    val gson = Gson()
                    val jsonCadena: String = gson.toJson(lista)

                    // Pasar la lista de frutas a través del intent a la actividad PrincipalActivity
                    intentPrincipal.putExtra("LISTA", jsonCadena)
                    startActivity(intentPrincipal)
                } else {
                    // Manejar el caso de error si la llamada no fue exitosa
                    // Aquí podrías mostrar un mensaje de error o realizar alguna otra acción
                }
            } catch (e: Exception) {
                // Manejar excepciones en caso de errores durante la llamada a la API
                println("Error: ${e.message}")
            }
        }
    }

    // Función privada para obtener una instancia de Retrofit para realizar llamadas a la API
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/api/fruit/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

    /*
    private fun searchByName(query:String){



        CoroutineScope(Dispatchers.IO).launch {

            var paremetro:String = "all"
            if(query.isNotEmpty()){
                paremetro = query
            }

            try {
                val call = getRetrofit().create(APIService::class.java).getFrutasByNombre("$paremetro")
                val puppies = call.body()
                if(call.isSuccessful){
                    //show Recyclerview
                   // val lista:ArrayList<FrutasResponse> = puppies ?: emptyList()

                    val lista:List<FrutasResponse> = puppies?: emptyList()
                    val gson = Gson()
                    val jsonCadena: String = gson.toJson(lista)

                    intentPrincipal.putExtra("LISTA",jsonCadena)
                    startActivity(intentPrincipal)

                }else{
                    //show error
                }
            }catch (e: Exception){

                println("Error: ${e.message}")
            }



        }
    }




    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true;
    }
*/
