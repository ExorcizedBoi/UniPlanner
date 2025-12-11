package com.example.uniplanner.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniplanner.models.BloqueHorario
import com.example.uniplanner.persistencia.RepositorioEnMemoria
import com.example.uniplanner.persistencia.MallaData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorarioScreen(onMenuClick: () -> Unit) {
    var listaBloques by remember { mutableStateOf(RepositorioEnMemoria.obtenerHorario()) }
    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var bloqueAEliminar by remember { mutableStateOf<BloqueHorario?>(null) }

    val alturaHora = 60.dp
    val horaInicioDia = 8

    if (bloqueAEliminar != null) {
        AlertDialog(
            onDismissRequest = { bloqueAEliminar = null },
            title = { Text("¿Eliminar clase?") },
            text = { Text("Se borrará '${bloqueAEliminar?.nombreRamo}' del horario.") },
            confirmButton = {
                Button(
                    onClick = {
                        bloqueAEliminar?.let {
                            RepositorioEnMemoria.borrarBloque(it.id)
                            listaBloques = RepositorioEnMemoria.obtenerHorario()
                        }
                        bloqueAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { bloqueAEliminar = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Horario", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onMenuClick) { Icon(Icons.Filled.Menu, "Menú") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogoCrear = true }) { Icon(Icons.Filled.Add, "Agregar") }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(start = 40.dp)) {
                listOf("L", "M", "M", "J", "V").forEach { dia ->
                    Box(modifier = Modifier.weight(1f).padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Text(dia, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Row(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.width(40.dp)) {
                    for (h in horaInicioDia..22) {
                        Text("$h:00", fontSize = 10.sp, modifier = Modifier.height(alturaHora).fillMaxWidth(), textAlign = TextAlign.End, color = Color.Gray)
                    }
                }
                for (dia in 1..5) {
                    Box(modifier = Modifier.weight(1f).height(alturaHora * (23 - horaInicioDia)).border(0.5.dp, Color.LightGray)) {
                        for (i in 0 until (23 - horaInicioDia)) {
                            HorizontalDivider(modifier = Modifier.offset(y = alturaHora * i), color = Color.LightGray.copy(alpha = 0.5f))
                        }
                        listaBloques.filter { it.diaSemana == dia }.forEach { bloque ->
                            val minutosDesdeInicio = (bloque.horaInicio - horaInicioDia) * 60 + bloque.minutoInicio
                            val offsetY = (minutosDesdeInicio.toFloat() / 60f) * alturaHora.value

                            val colorFondo = Color(bloque.colorArgb)

                            val colorTexto = if (colorFondo.luminance() > 0.5) Color.Black else Color.White

                            Card(
                                colors = CardDefaults.cardColors(containerColor = colorFondo),
                                modifier = Modifier
                                    .padding(1.dp).fillMaxWidth()
                                    .height((bloque.duracionMinutos.toFloat() / 60f * alturaHora.value).dp)
                                    .offset(y = offsetY.dp)
                                    .clickable { bloqueAEliminar = bloque }
                            ) {
                                Column(modifier = Modifier.padding(2.dp)) {
                                    Text(
                                        bloque.nombreRamo,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 10.sp,
                                        color = colorTexto
                                    )
                                    Text(
                                        "${bloque.horaInicio}:${bloque.minutoInicio.toString().padStart(2,'0')}",
                                        fontSize = 8.sp,
                                        color = colorTexto.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogoCrear) {
            DialogoNuevoBloque(
                onDismiss = { mostrarDialogoCrear = false },
                onConfirm = {
                    RepositorioEnMemoria.guardarBloque(it)
                    listaBloques = RepositorioEnMemoria.obtenerHorario()
                    mostrarDialogoCrear = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoNuevoBloque(onDismiss: () -> Unit, onConfirm: (BloqueHorario) -> Unit) {
    var ramo by remember { mutableStateOf("") }
    var dia by remember { mutableIntStateOf(1) }

    var horaInicio by remember { mutableStateOf("8") }
    var minInicio by remember { mutableStateOf("30") }

    var horaFin by remember { mutableStateOf("10") }
    var minFin by remember { mutableStateOf("00") }

    var expandido by remember { mutableStateOf(false) }
    val ramos = remember { MallaData.listaMaterias.map { it.nombre }.sorted() }

    val coloresDisponibles = listOf(
        0xFFE57373, 0xFF64B5F6, 0xFF81C784, 0xFFFFD54F,
        0xFFBA68C8, 0xFFFF8A65, 0xFF90A4AE
    )
    var colorSeleccionado by remember { mutableStateOf(coloresDisponibles[1]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Clase") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = !expandido }) {
                    OutlinedTextField(value = ramo, onValueChange = { ramo = it }, label = { Text("Ramo") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) })
                    ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                        ramos.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { ramo = it; expandido = false }) }
                    }
                }

                Column {
                    Text("Día", style = MaterialTheme.typography.labelSmall)
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        listOf(1 to "L", 2 to "M", 3 to "M", 4 to "J", 5 to "V").forEach { (d, l) ->
                            FilterChip(selected = dia == d, onClick = { dia = d }, label = { Text(l, fontSize = 10.sp) }, modifier = Modifier.padding(horizontal = 2.dp))
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Inicio", style = MaterialTheme.typography.labelSmall)
                        Row {
                            OutlinedTextField(value = horaInicio, onValueChange = { if(it.length <= 2) horaInicio = it }, placeholder = {Text("HH")}, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                            Text(":", modifier = Modifier.padding(horizontal = 4.dp, vertical = 14.dp))
                            OutlinedTextField(value = minInicio, onValueChange = { if(it.length <= 2) minInicio = it }, placeholder = {Text("MM")}, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Término", style = MaterialTheme.typography.labelSmall)
                        Row {
                            OutlinedTextField(value = horaFin, onValueChange = { if(it.length <= 2) horaFin = it }, placeholder = {Text("HH")}, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                            Text(":", modifier = Modifier.padding(horizontal = 4.dp, vertical = 14.dp))
                            OutlinedTextField(value = minFin, onValueChange = { if(it.length <= 2) minFin = it }, placeholder = {Text("MM")}, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                        }
                    }
                }

                Column {
                    Text("Color", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        coloresDisponibles.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorHex))
                                    .border(
                                        width = if (colorSeleccionado == colorHex) 3.dp else 0.dp,
                                        color = if (colorSeleccionado == colorHex) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { colorSeleccionado = colorHex }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val hIni = horaInicio.toIntOrNull() ?: 0
                val mIni = minInicio.toIntOrNull() ?: 0
                val hFin = horaFin.toIntOrNull() ?: 0
                val mFin = minFin.toIntOrNull() ?: 0

                val minutosTotalInicio = hIni * 60 + mIni
                val minutosTotalFin = hFin * 60 + mFin
                val duracion = minutosTotalFin - minutosTotalInicio

                if (duracion > 0 && ramo.isNotEmpty()) {
                    onConfirm(BloqueHorario(
                        id = System.currentTimeMillis(),
                        nombreRamo = ramo,
                        diaSemana = dia,
                        horaInicio = hIni,
                        minutoInicio = mIni,
                        duracionMinutos = duracion,
                        colorArgb = colorSeleccionado
                    ))
                }
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}