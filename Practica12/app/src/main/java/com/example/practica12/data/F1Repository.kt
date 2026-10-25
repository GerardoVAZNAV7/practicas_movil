package com.example.practica12.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Termina con "/" o Retrofit se queja
    private const val BASE_URL = "https://api.jolpi.ca/ergast/f1/"

    val f1Api: F1Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1Api::class.java)
    }
}

class F1Repository {
    private val api = RetrofitClient.f1Api
    private val limite = 100

    suspend fun getCalendario(year: Int): List<Race> =
        api.getCalendario(year, limite).mrData?.raceTable?.races ?: emptyList()

    // Si la temporada aun no tiene carreras, StandingsLists viene vacia (por eso el firstOrNull)
    suspend fun getTablaPilotos(year: Int): List<DriverStanding> =
        api.getTablaPilotos(year, limite).mrData?.standingsTable?.standingsLists
            ?.firstOrNull()?.driverStandings ?: emptyList()

    suspend fun getTablaConstructores(year: Int): List<ConstructorStanding> =
        api.getTablaConstructores(year, limite).mrData?.standingsTable?.standingsLists
            ?.firstOrNull()?.constructorStandings ?: emptyList()
}
