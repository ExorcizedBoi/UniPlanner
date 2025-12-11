package com.example.uniplanner.models

abstract class RequisitosAcademicos(
    override val id: String,
    override val nombre: String,
    val prerrequisitos: List<String>
) : DatosAcademicos {

    // Lógica compartida para verificar si cumpliste los prerrequisitos
    protected fun requisitosCumplidos(historial: List<Resultado>): Boolean {
        if (prerrequisitos.isEmpty()) return true

        val aprobadosIds = historial.filter { it.aprobado }.map { it.idMateria }

        return prerrequisitos.all { req ->
            aprobadosIds.contains(req)
        }
    }
}