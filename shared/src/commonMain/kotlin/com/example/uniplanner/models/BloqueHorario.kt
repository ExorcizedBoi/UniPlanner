package com.example.uniplanner.models

data class BloqueHorario(
    val id: Long,
    val nombreRamo: String,
    val diaSemana: Int,
    val horaInicio: Int,
    val minutoInicio: Int,
    val duracionMinutos: Int,
    val colorArgb: Long,
)