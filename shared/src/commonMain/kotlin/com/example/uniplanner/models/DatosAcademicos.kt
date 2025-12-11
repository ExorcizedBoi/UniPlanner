package com.example.uniplanner.models

interface DatosAcademicos {
    val id: String
    val nombre: String

    // Polimorfismo: Calcula el estado usando el historial de Resultados
    fun calcularEstado(historial: List<Resultado>): EstadoMateria
}