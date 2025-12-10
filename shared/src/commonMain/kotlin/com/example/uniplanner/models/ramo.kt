package com.example.uniplanner.models

enum class EstadoMateria {
    BLOQUEADO,
    DISPONIBLE,
    APROBADA
}
data class Materia(
    val id: String,   // id del ramo por ej: "INF-111"
    val nombre: String,  // nombre del ramo por ej: "Álgebra I"
    val semestre: Int,  // semestre por ej: semestre 1, 2, 3...
    val creditos: Int, // creditos necesarios por cada ramo
    val esAnual: Boolean,  // determina si el ramo es anual o no, true = anual, false = semestral
    val prerrequisitos: List<String> = emptyList(), // lista de los ids de los ramos prerquisitos ["INF-100"]
    var estado: EstadoMateria = EstadoMateria.BLOQUEADO  // estado de la materia dependiendo de los prerrequisitos
)