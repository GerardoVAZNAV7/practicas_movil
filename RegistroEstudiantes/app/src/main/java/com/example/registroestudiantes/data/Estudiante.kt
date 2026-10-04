package com.example.registroestudiantes.data

data class Estudiante(
    val matricula: String,
    val nombre: String,
    val carrera: String,
    val turno: String,
    val activo: Boolean
)
