package com.example.uniplanner.vistas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

data class FilaNota(
    val id: Int,
    var nota: String = "",
    var porcentaje: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraScreen(
    onMenuClick: () -> Unit
) {
    var listaNotas by remember {
        mutableStateOf(listOf(FilaNota(1), FilaNota(2), FilaNota(3)))
    }
    var notaAprobacion by remember { mutableStateOf("40") }
    var ponderacionExamen by remember { mutableStateOf("30") }
    var promedioResultado by remember { mutableStateOf<Double?>(null) }
    var notaNecesaria by remember { mutableStateOf<Double?>(null) }
    var mensajeError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Calculadora de Notas", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "Ingresa tus notas y ponderaciones.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                items(listaNotas) { fila ->
                    val ejemploNota = if (fila.id == 1) "60" else ""
                    val ejemploPorc = if (fila.id == 1) "25" else ""

                    RowNota(
                        fila = fila,
                        placeholderNota = ejemploNota,
                        placeholderPorc = ejemploPorc,
                        onChange = { nuevaFila ->
                            listaNotas = listaNotas.map { if (it.id == fila.id) nuevaFila else it }
                        },
                        onDelete = {
                            if (listaNotas.size > 1) {
                                listaNotas = listaNotas.filter { it.id != fila.id }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    TextButton(
                        onClick = {
                            val nuevoId = (listaNotas.maxOfOrNull { it.id } ?: 0) + 1
                            listaNotas = listaNotas + FilaNota(nuevoId)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Agregar otra nota")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Configuración Examen", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = notaAprobacion,
                                    onValueChange = { notaAprobacion = it },
                                    label = { Text("Nota Aprobar") },
                                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = ponderacionExamen,
                                    onValueChange = { ponderacionExamen = it },
                                    label = { Text("% Examen") },
                                    modifier = Modifier.weight(1f).padding(start = 4.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    trailingIcon = { Text("%") },
                                    singleLine = true
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Button(
                onClick = {
                    try {
                        mensajeError = ""
                        var sumaNotasPonderadas = 0.0
                        var sumaPorcentajes = 0.0

                        listaNotas.forEach {
                            if (it.nota.isNotEmpty() && it.porcentaje.isNotEmpty()) {
                                val n = it.nota.toDouble()
                                val p = it.porcentaje.toDouble()
                                sumaNotasPonderadas += n * (p / 100.0)
                                sumaPorcentajes += p
                            }
                        }

                        if (sumaPorcentajes == 0.0) {
                            mensajeError = "Ingresa al menos una nota válida."
                            return@Button
                        }

                        val promedioActual = sumaNotasPonderadas / (sumaPorcentajes / 100.0)
                        promedioResultado = promedioActual

                        if (ponderacionExamen.isNotEmpty() && notaAprobacion.isNotEmpty()) {
                            val pesoExamen = ponderacionExamen.toDouble() / 100.0
                            val notaTarget = notaAprobacion.toDouble()
                            val notaNecesariaCalculada = (notaTarget - sumaNotasPonderadas) / pesoExamen
                            notaNecesaria = notaNecesariaCalculada
                        } else {
                            notaNecesaria = null
                        }

                    } catch (e: Exception) {
                        mensajeError = "Revisa los números."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Calcular", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (promedioResultado != null || mensajeError.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if(mensajeError.isNotEmpty()) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (mensajeError.isNotEmpty()) {
                            Text(mensajeError, color = MaterialTheme.colorScheme.onErrorContainer)
                        } else {
                            val promedio = promedioResultado!!
                            val promedioRedondeado = (promedio * 10).roundToInt() / 10.0

                            Text("Promedio actual: $promedioRedondeado", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                            if (notaNecesaria != null) {
                                val necesaria = notaNecesaria!!
                                val necesariaRedondeada = (necesaria * 10).roundToInt() / 10.0
                                Text("Necesitas un $necesariaRedondeada en el examen.")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun RowNota(
    fila: FilaNota,
    placeholderNota: String,
    placeholderPorc: String,
    onChange: (FilaNota) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Nota ${fila.id}",
            modifier = Modifier.width(60.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        OutlinedTextField(
            value = fila.nota,
            onValueChange = { onChange(fila.copy(nota = it)) },
            modifier = Modifier.weight(1f).padding(4.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = if (placeholderNota.isNotEmpty()) { { Text(placeholderNota) } } else null,
            singleLine = true
        )

        OutlinedTextField(
            value = fila.porcentaje,
            onValueChange = { onChange(fila.copy(porcentaje = it)) },
            modifier = Modifier.weight(1f).padding(4.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = if (placeholderPorc.isNotEmpty()) { { Text(placeholderPorc) } } else null,
            trailingIcon = { Text("%") },
            singleLine = true
        )

        TextButton(onClick = onDelete) {
            Text("X", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
    }
}