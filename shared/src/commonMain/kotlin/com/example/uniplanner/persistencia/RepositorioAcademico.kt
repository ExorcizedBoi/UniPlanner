package com.example.uniplanner.persistencia

import com.example.uniplanner.models.Resultado
import com.example.uniplanner.models.Evaluacion // Importamos el nuevo modelo

interface IRepositorioAcademico {
    // --- HISTORIAL DE RAMOS ---
    fun guardarNota(resultado: Resultado)
    fun obtenerHistorial(): List<Resultado>
    fun borrarHistorial()

    // --- NUEVO: GESTIÓN DE EVALUACIONES ---
    fun guardarEvaluacion(evaluacion: Evaluacion)
    fun borrarEvaluacion(id: Long)
    fun obtenerEvaluaciones(): List<Evaluacion>
}

object RepositorioEnMemoria : IRepositorioAcademico {
    private val datos = mutableListOf<Resultado>()
    private val evaluaciones = mutableListOf<Evaluacion>() // Lista persistente en memoria

    // Implementación Ramos
    override fun guardarNota(resultado: Resultado) {
        datos.removeAll { it.idMateria == resultado.idMateria }
        datos.add(resultado)
    }

    override fun obtenerHistorial(): List<Resultado> {
        return datos
    }

    override fun borrarHistorial() {
        datos.clear()
        evaluaciones.clear() // Opcional: reiniciar todo
    }

    // Implementación Evaluaciones
    override fun guardarEvaluacion(evaluacion: Evaluacion) {
        evaluaciones.add(evaluacion)
    }

    override fun borrarEvaluacion(id: Long) {
        evaluaciones.removeAll { it.id == id }
    }

    override fun obtenerEvaluaciones(): List<Evaluacion> {
        return evaluaciones
    }
}