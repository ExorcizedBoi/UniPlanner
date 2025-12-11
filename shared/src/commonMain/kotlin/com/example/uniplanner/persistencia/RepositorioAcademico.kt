package com.example.uniplanner.persistencia

import com.example.uniplanner.models.Resultado
import com.example.uniplanner.models.Evaluacion

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

    private const val SEP_ITEM = "||item||"
    private const val SEP_FIELD = "<:>"

    fun inicializar() {
        try {
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
        } catch (e: Exception) {
            println("Error al cargar datos: ${e.message}")
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
}