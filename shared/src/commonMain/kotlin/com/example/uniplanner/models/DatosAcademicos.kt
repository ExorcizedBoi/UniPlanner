package com.example.uniplanner.models

interface DatosAcademicos {
    val id: String
    val nombre: String

    fun calcularEstado(historial: List<Resultado>): EstadoMateria
}