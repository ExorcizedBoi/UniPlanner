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

        // 1. Crear escenario hipotético (Clonar historial y aplicar fallos)
        val historialSimulado = historialReal.toMutableList()

        // Si ya teníamos aprobado algo que ahora queremos simular reprobar, lo quitamos
        historialSimulado.removeAll { ramosAReprobar.contains(it.idMateria) }

        // Agregamos la "reprobación" explícita (nota roja ficticia, ej: 3.0)
        ramosAReprobar.forEach { id ->
            // Evitamos duplicados
            if (historialSimulado.none { it.idMateria == id }) {
                historialSimulado.add(Resultado(id, 3.0, false))
            }
        }

        // 2. Detectar efecto dominó (Recursividad)
        val afectados = mutableListOf<String>()

        // Revisamos CADA elemento de la malla para ver si cambia su estado en este escenario
        mallaCompleta.forEach { elemento ->
            // Usamos el Polimorfismo: calcularEstado se comporta distinto si es Ramo o Modulos
            val estadoSimulado = elemento.calcularEstado(historialSimulado)

            // Si en la simulación aparece BLOQUEADO...
            if (estadoSimulado == EstadoMateria.BLOQUEADO) {
                // ...verificamos si es culpa directa o indirecta de los ramos simulados
                if (esDependienteDe(elemento, ramosAReprobar, mallaCompleta)) {
                    afectados.add(elemento.id)
                }
            }
        }
        return afectados
    }

    /**
     * Función recursiva para rastrear la cadena de prerrequisitos hacia atrás.
     * Responde: "¿El 'objetivo' depende de alguno de los 'culpables'?"
     */
    private fun esDependienteDe(
        objetivo: RequisitosAcademicos,
        culpables: List<String>,
        malla: List<RequisitosAcademicos>
    ): Boolean {
        // Caso Base 1: El objetivo tiene como prerrequisito directo a uno de los culpables
        if (objetivo.prerrequisitos.any { culpables.contains(it) }) return true

        // Caso Base 2: Si no tiene prerrequisitos, no es dependiente
        if (objetivo.prerrequisitos.isEmpty()) return false

        // Caso Recursivo: Buscar en los "padres" (los prerrequisitos del objetivo)
        // Filtramos la malla para encontrar los objetos completos de los prerrequisitos
        val padres = malla.filter { objetivo.prerrequisitos.contains(it.id) }

        // Si ALGUNO de los padres depende de los culpables, entonces el objetivo también
        return padres.any { padre -> esDependienteDe(padre, culpables, malla) }
    }
}