package com.example.uniplanner.Servicios

import com.example.uniplanner.models.*

class SimuladorMalla {

    /**
     * Simula el futuro: Recibe la malla y una lista de IDs de ramos que el usuario
     * "teme" reprobar. Retorna los IDs de todo lo que se bloquearía en consecuencia.
     */
    fun simularImpactoReprobacion(
        mallaCompleta: List<RequisitosAcademicos>, // Polimorfismo: Aceptamos Ramos y Modulos
        historialReal: List<Resultado>,
        ramosAReprobar: List<String>
    ): List<String> {

        val historialSimulado = historialReal.toMutableList()

        historialSimulado.removeAll { ramosAReprobar.contains(it.idMateria) }

        ramosAReprobar.forEach { id ->
            if (historialSimulado.none { it.idMateria == id }) {
                historialSimulado.add(Resultado(id, 3.0, false))
            }
        }

        val afectados = mutableListOf<String>()

        mallaCompleta.forEach { elemento ->
            val estadoSimulado = elemento.calcularEstado(historialSimulado)

            if (estadoSimulado == EstadoMateria.BLOQUEADO) {
                if (esDependienteDe(elemento, ramosAReprobar, mallaCompleta)) {
                    afectados.add(elemento.id)
                }
            }
        }
        return afectados
    }

    private fun esDependienteDe(
        objetivo: RequisitosAcademicos,
        culpables: List<String>,
        malla: List<RequisitosAcademicos>
    ): Boolean {
        if (objetivo.prerrequisitos.any { culpables.contains(it) }) return true

        if (objetivo.prerrequisitos.isEmpty()) return false

        val padres = malla.filter { objetivo.prerrequisitos.contains(it.id) }

        return padres.any { padre -> esDependienteDe(padre, culpables, malla) }
    }
}