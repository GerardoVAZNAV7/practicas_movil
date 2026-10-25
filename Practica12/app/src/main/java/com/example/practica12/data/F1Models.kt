package com.example.practica12.data

import com.google.gson.annotations.SerializedName

// OJO: la API (Jolpica) regresa TODO como texto, hasta los numeros ("round": "17").
// Si algun dia quiero operar con ellos hay que convertirlos con toIntOrNull().
// Todo es nullable porque en temporadas viejas faltan campos (ej. permanentNumber).

// ---------- Calendario: /{year}.json ----------
data class ScheduleResponse(@SerializedName("MRData") val mrData: ScheduleData?)
data class ScheduleData(@SerializedName("RaceTable") val raceTable: RaceTable?)
data class RaceTable(@SerializedName("Races") val races: List<Race>?)

data class Race(
    val season: String?,
    val round: String?,
    val raceName: String?,
    @SerializedName("Circuit") val circuit: Circuit?,
    val date: String?,
    val time: String?,
    // Las sesiones solo vienen en las temporadas recientes
    @SerializedName("FirstPractice") val firstPractice: Session?,
    @SerializedName("SecondPractice") val secondPractice: Session?,
    @SerializedName("ThirdPractice") val thirdPractice: Session?,
    @SerializedName("Qualifying") val qualifying: Session?,
    @SerializedName("Sprint") val sprint: Session?,
    @SerializedName("SprintQualifying") val sprintQualifying: Session?
)
data class Session(val date: String?, val time: String?)
data class Circuit(
    val circuitName: String?,
    @SerializedName("Location") val location: Location?
)
data class Location(val locality: String?, val country: String?)

// ---------- Pilotos: /{year}/driverStandings.json ----------
data class DriverStandingsResponse(@SerializedName("MRData") val mrData: DriverStandingsData?)
data class DriverStandingsData(@SerializedName("StandingsTable") val standingsTable: DriverStandingsTable?)
data class DriverStandingsTable(@SerializedName("StandingsLists") val standingsLists: List<DriverStandingsList>?)
data class DriverStandingsList(@SerializedName("DriverStandings") val driverStandings: List<DriverStanding>?)

data class DriverStanding(
    val position: String?,
    val points: String?,
    val wins: String?,
    @SerializedName("Driver") val driver: Driver?,
    // Es lista porque un piloto puede cambiar de equipo en la temporada
    @SerializedName("Constructors") val constructors: List<Constructor>?
)
data class Driver(
    val driverId: String?,
    val permanentNumber: String?,
    val code: String?,
    val url: String?,
    val givenName: String?,
    val familyName: String?,
    val dateOfBirth: String?,
    val nationality: String?
)
data class Constructor(
    val constructorId: String?,
    val url: String?,
    val name: String?,
    val nationality: String?
)

// ---------- Constructores: /{year}/constructorStandings.json ----------
data class ConstructorStandingsResponse(@SerializedName("MRData") val mrData: ConstructorStandingsData?)
data class ConstructorStandingsData(@SerializedName("StandingsTable") val standingsTable: ConstructorStandingsTable?)
data class ConstructorStandingsTable(@SerializedName("StandingsLists") val standingsLists: List<ConstructorStandingsList>?)
data class ConstructorStandingsList(@SerializedName("ConstructorStandings") val constructorStandings: List<ConstructorStanding>?)

data class ConstructorStanding(
    val position: String?,
    val points: String?,
    val wins: String?,
    @SerializedName("Constructor") val constructor: Constructor?
)
