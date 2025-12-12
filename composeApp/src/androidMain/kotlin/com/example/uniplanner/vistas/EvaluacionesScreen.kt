package com.example.uniplanner.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniplanner.models.Evaluacion
import com.example.uniplanner.persistencia.MallaData
import com.example.uniplanner.persistencia.RepositorioEnMemoria
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionesScreen(onMenuClick: () -> Unit) {
    var listaEvaluaciones by remember {
        mutableStateOf(RepositorioEnMemoria.obtenerEvaluaciones().toList())
    }

    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis evaluaciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarDialogo = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, "Nueva")
                Spacer(Modifier.width(8.dp))
                Text("Nueva Evaluación")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            if (listaEvaluaciones.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay evaluaciones pendientes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(listaEvaluaciones.sortedBy { it.fechaMillis }) { eval ->
                        CardEvaluacion(eval) {
                            RepositorioEnMemoria.borrarEvaluacion(eval.id)
                            listaEvaluaciones = RepositorioEnMemoria.obtenerEvaluaciones().toList()
                        }
                    }
                }
            }
        }

        if (mostrarDialogo) {
            DialogoNuevaEvaluacion(
                onDismiss = { mostrarDialogo = false },
                onConfirm = { nueva ->
                    RepositorioEnMemoria.guardarEvaluacion(nueva)
                    listaEvaluaciones = RepositorioEnMemoria.obtenerEvaluaciones().toList()
                    mostrarDialogo = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoNuevaEvaluacion(onDismiss: () -> Unit, onConfirm: (Evaluacion) -> Unit) {
    var ramoSeleccionado by remember { mutableStateOf("") }
    var nombreEvaluacion by remember { mutableStateOf("") }
    var porcentajeStr by remember { mutableStateOf("") }
    var fechaSeleccionadaMillis by remember { mutableStateOf<Long?>(null) }

    var expandidoRamos by remember { mutableStateOf(false) }
    val listaRamos = remember { MallaData.listaMaterias.map { it.nombre }.sorted() }

    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    fechaSeleccionadaMillis = datePickerState.selectedDateMillis
                    mostrarCalendario = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = { Text("Seleccionar fecha", modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)) },
                headline = {
                    val texto = if (datePickerState.selectedDateMillis != null) {
                        SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date(datePickerState.selectedDateMillis!!))
                            .replaceFirstChar { it.uppercase() }
                    } else "Ingresa una fecha"
                    Text(texto, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp))
                }
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Evaluación") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandidoRamos,
                    onExpandedChange = { expandidoRamos = !expandidoRamos }
                ) {
                    OutlinedTextField(
                        value = ramoSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Asignatura") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoRamos) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandidoRamos,
                        onDismissRequest = { expandidoRamos = false }
                    ) {
                        listaRamos.forEach { ramo ->
                            DropdownMenuItem(
                                text = { Text(ramo) },
                                onClick = {
                                    ramoSeleccionado = ramo
                                    expandidoRamos = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = nombreEvaluacion,
                    onValueChange = { nombreEvaluacion = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = porcentajeStr,
                    onValueChange = { if (it.all { char -> char.isDigit() }) porcentajeStr = it },
                    label = { Text("Porcentaje (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Text("%") }
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (fechaSeleccionadaMillis != null) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(fechaSeleccionadaMillis!!)) else "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha") },
                        trailingIcon = { Icon(Icons.Filled.CalendarToday, null) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Box(Modifier.matchParentSize().clickable { mostrarCalendario = true })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ramoSeleccionado.isNotEmpty() && nombreEvaluacion.isNotEmpty() &&
                        porcentajeStr.isNotEmpty() && fechaSeleccionadaMillis != null) {
                        val nueva = Evaluacion(
                            id = System.currentTimeMillis(),
                            ramo = ramoSeleccionado,
                            nombre = nombreEvaluacion,
                            porcentaje = porcentajeStr.toInt(),
                            fechaMillis = fechaSeleccionadaMillis!!
                        )
                        onConfirm(nueva)
                    }
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun CardEvaluacion(eval: Evaluacion, onDelete: () -> Unit) {
    val hoy = System.currentTimeMillis()
    val diff = eval.fechaMillis - hoy
    val diasRestantes = ceil(diff / (1000.0 * 60 * 60 * 24)).toInt()

    val formatoTarjeta = SimpleDateFormat("dd MMM", Locale.getDefault())
    val textoFecha = formatoTarjeta.format(Date(eval.fechaMillis))

    val (colorFondo, colorTexto) = when {
        diasRestantes < 0 -> Pair(Color(0xFFEEEEEE), Color.Gray)
        diasRestantes <= 3 -> Pair(Color(0xFFFFEBEE), Color(0xFFD32F2F))
        diasRestantes <= 7 -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = eval.ramo,
                    style = MaterialTheme.typography.labelMedium,
                    color = if(diasRestantes > 7) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                )
                Text(
                    text = eval.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${eval.porcentaje}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = textoFecha,
                        style = MaterialTheme.typography.bodySmall,
                        color = if(diasRestantes > 7) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (diasRestantes >= 0) {
                    Text(
                        text = "$diasRestantes días",
                        fontWeight = FontWeight.Bold,
                        color = colorTexto
                    )
                } else {
                    Text("Finalizado", fontSize = 10.sp, color = Color.Gray)
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Borrar",
                        tint = if(diasRestantes > 7) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                    )
                }
            }
        }
    }
}