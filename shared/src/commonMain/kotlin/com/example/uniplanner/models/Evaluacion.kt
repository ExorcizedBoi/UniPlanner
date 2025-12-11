package com.example.uniplanner.models

data class Evaluacion(
    val id: Long,
    val ramo: String,
    val nombre: String,
    val porcentaje: Int,
    val fechaMillis: Long
)