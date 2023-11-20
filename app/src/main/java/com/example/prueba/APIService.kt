package com.example.prueba


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface APIService {
    @GET
    suspend fun getFrutasByNombre(@Url url:String): Response<List<FrutasResponse>>
}