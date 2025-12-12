package com.example.uniplanner.models

class Modulos(
    id: String,
    nombre: String,
    val creditosRequeridos: Int,
    prerrequisitos: List<String>,
    val semestre: Int
) : RequisitosAcademicos(
    id,
    nombre,
    prerrequisitos
) {
    override fun calcularEstado(historial: List<Resultado>): EstadoMateria {
        val creditosAcumulados = historial.filter { it.aprobado }.size * 5

        val requisitosOk = requisitosCumplidos(historial)

        return when {
            requisitosOk && creditosAcumulados >= creditosRequeridos -> EstadoMateria.DISPONIBLE
            else -> EstadoMateria.BLOQUEADO
        }
    }
}