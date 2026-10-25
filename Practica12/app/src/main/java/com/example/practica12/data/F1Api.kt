package com.example.practica12.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Endpoints de Jolpica (la misma API que probe en Postman en la Actividad 8.1)
// El limit es porque por defecto solo regresa 30 registros.
interface F1Api {
    @GET("{year}.json")
    suspend fun getCalendario(
        @Path("year") year: Int,
        @Query("limit") limit: Int
    ): ScheduleResponse

    @GET("{year}/driverStandings.json")
    suspend fun getTablaPilotos(
        @Path("year") year: Int,
        @Query("limit") limit: Int
    ): DriverStandingsResponse

    @GET("{year}/constructorStandings.json")
    suspend fun getTablaConstructores(
        @Path("year") year: Int,
        @Query("limit") limit: Int
    ): ConstructorStandingsResponse
}
