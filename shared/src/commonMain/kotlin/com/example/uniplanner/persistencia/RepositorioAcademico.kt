package com.example.uniplanner.persistencia

import com.example.uniplanner.models.Resultado
import com.example.uniplanner.models.Evaluacion
import com.example.uniplanner.models.BloqueHorario

interface IRepositorioAcademico {
    fun guardarNota(resultado: Resultado)
    fun obtenerHistorial(): List<Resultado>
    fun borrarHistorial()
    fun guardarEvaluacion(evaluacion: Evaluacion)
    fun borrarEvaluacion(id: Long)
    fun obtenerEvaluaciones(): List<Evaluacion>
}

object RepositorioEnMemoria : IRepositorioAcademico {
    var guardarDatos: ((clave: String, valor: String) -> Unit)? = null
    var cargarDatos: ((clave: String) -> String?)? = null

    private val datos = mutableListOf<Resultado>()
    private val evaluaciones = mutableListOf<Evaluacion>()
    private val horario = mutableListOf<BloqueHorario>()
    private const val SEP_ITEM = "||item||"
    private const val SEP_FIELD = "<:>"
    private const val COLOR_DEFAULT = 0xFFD0BCFF

    fun inicializar() {
        val historialStr = cargarDatos?.invoke("HISTORIAL")
        if (!historialStr.isNullOrEmpty()) {
            datos.clear()
            val items = historialStr.split(SEP_ITEM)
            items.forEach { item ->
                if (item.isNotEmpty()) {
                    val partes = item.split(SEP_FIELD)
                    if (partes.size == 3) {
                        datos.add(Resultado(
                            idMateria = partes[0],
                            nota = partes[1].toDoubleOrNull() ?: 1.0,
                            aprobado = partes[2].toBoolean()
                        ))
                    }
                }
            }
        }

        val evalStr = cargarDatos?.invoke("EVALUACIONES")
        if (!evalStr.isNullOrEmpty()) {
            evaluaciones.clear()
            val items = evalStr.split(SEP_ITEM)
            items.forEach { item ->
                if (item.isNotEmpty()) {
                    val partes = item.split(SEP_FIELD)
                    if (partes.size == 5) {
                        evaluaciones.add(Evaluacion(
                            id = partes[0].toLongOrNull() ?: 0L,
                            ramo = partes[1],
                            nombre = partes[2],
                            porcentaje = partes[3].toIntOrNull() ?: 0,
                            fechaMillis = partes[4].toLongOrNull() ?: 0L
                        ))
                    }
                }
            }
        }

        val horarioStr = cargarDatos?.invoke("HORARIO")
        if (!horarioStr.isNullOrEmpty()) {
            horario.clear()
            val items = horarioStr.split(SEP_ITEM)
            items.forEach { item ->
                if (item.isNotBlank()) {
                    val partes = item.split(SEP_FIELD)
                    // Validamos que tenga al menos los campos básicos (6)
                    // El campo 7 (color) es opcional para compatibilidad hacia atrás
                    if (partes.size >= 6) {
                        horario.add(BloqueHorario(
                            id = partes[0].toLongOrNull() ?: 0L,
                            nombreRamo = partes[1],
                            diaSemana = partes[2].toIntOrNull() ?: 1,
                            horaInicio = partes[3].toIntOrNull() ?: 8,
                            minutoInicio = partes[4].toIntOrNull() ?: 0,
                            duracionMinutos = partes[5].toIntOrNull() ?: 60,
                            // Si existe el campo 7 lo usamos, si no, usamos el default
                            colorArgb = if (partes.size > 6) partes[6].toLongOrNull() ?: COLOR_DEFAULT else COLOR_DEFAULT
                        ))
                    }
                }
            }
        }
    }

    private fun guardarTodo() {
        val historialStr = datos.joinToString(SEP_ITEM) {
            "${it.idMateria}$SEP_FIELD${it.nota}$SEP_FIELD${it.aprobado}"
        }
        guardarDatos?.invoke("HISTORIAL", historialStr)

        val evalStr = evaluaciones.joinToString(SEP_ITEM) {
            "${it.id}$SEP_FIELD${it.ramo}$SEP_FIELD${it.nombre}$SEP_FIELD${it.porcentaje}$SEP_FIELD${it.fechaMillis}"
        }
        guardarDatos?.invoke("EVALUACIONES", evalStr)

        val horarioStr = horario.joinToString(SEP_ITEM) {
            "${it.id}$SEP_FIELD${it.nombreRamo}$SEP_FIELD${it.diaSemana}$SEP_FIELD${it.horaInicio}$SEP_FIELD${it.minutoInicio}$SEP_FIELD${it.duracionMinutos}$SEP_FIELD${it.colorArgb}"
        }
        guardarDatos?.invoke("HORARIO", horarioStr)
    }

    override fun guardarNota(resultado: Resultado) {
        datos.removeAll { it.idMateria == resultado.idMateria }
        datos.add(resultado)
        guardarTodo()
    }

    override fun obtenerHistorial(): List<Resultado> {
        return datos
    }

    override fun borrarHistorial() {
        datos.clear()
        evaluaciones.clear()
        horario.clear()
        guardarTodo()
    }

    override fun guardarEvaluacion(evaluacion: Evaluacion) {
        evaluaciones.add(evaluacion)
        guardarTodo()
    }

    override fun borrarEvaluacion(id: Long) {
        evaluaciones.removeAll { it.id == id }
        guardarTodo()
    }

    override fun obtenerEvaluaciones(): List<Evaluacion> {
        return evaluaciones
    }

    // --- Métodos de Horario ---
    fun guardarBloque(bloque: BloqueHorario) {
        horario.add(bloque)
        guardarTodo()
    }

    fun borrarBloque(id: Long) {
        horario.removeAll { it.id == id }
        guardarTodo()
    }

    fun obtenerHorario(): List<BloqueHorario> {
        return horario
    }
}