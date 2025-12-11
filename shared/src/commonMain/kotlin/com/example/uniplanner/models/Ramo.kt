package com.example.uniplanner.models

class Ramo(
    id: String,
    nombre: String,
    val creditos: Int,
    val esAnual: Boolean,
    prerrequisitos: List<String> = emptyList(),
    val semestre: Int
) : RequisitosAcademicos(id, nombre, prerrequisitos) {

    override fun calcularEstado(historial: List<Resultado>): EstadoMateria {
        val resultado = historial.find { it.idMateria == id }

        return when {
            resultado?.aprobado == true -> EstadoMateria.APROBADA
            // Si existe resultado pero no está aprobado, se bloquea (rojo)
            resultado != null && !resultado.aprobado -> EstadoMateria.BLOQUEADO
            requisitosCumplidos(historial) -> EstadoMateria.DISPONIBLE
            else -> EstadoMateria.BLOQUEADO
        }
    }
}