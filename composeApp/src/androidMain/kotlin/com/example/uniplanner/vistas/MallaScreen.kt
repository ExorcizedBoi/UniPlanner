package com.example.uniplanner.vistas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniplanner.models.EstadoMateria
import com.example.uniplanner.models.RequisitosAcademicos
import com.example.uniplanner.models.Resultado
import com.example.uniplanner.models.Ramo
import com.example.uniplanner.models.Modulos
import com.example.uniplanner.persistencia.MallaData
import com.example.uniplanner.persistencia.RepositorioEnMemoria
import com.example.uniplanner.Servicios.SimuladorMalla

data class MateriaVisual(
    val datos: RequisitosAcademicos,
    val estado: EstadoMateria,
    val esCritico: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MallaScreen(
    onMenuClick: () -> Unit
) {
    var historial by remember { mutableStateOf(RepositorioEnMemoria.obtenerHistorial().toList()) }
    var modoSimulacion by remember { mutableStateOf(false) }
    var simuladosReprobados by remember { mutableStateOf(setOf<String>()) }
    val simulador = remember { SimuladorMalla() }

    val listaVisual = remember(historial, modoSimulacion, simuladosReprobados) {
        MallaData.listaMaterias.map { materia ->
            var estado = materia.calcularEstado(historial)
            var esCritico = false

            if (modoSimulacion) {
                val afectados = simulador.simularImpactoReprobacion(
                    MallaData.listaMaterias,
                    historial,
                    simuladosReprobados.toList()
                )
                if (afectados.contains(materia.id)) {
                    estado = EstadoMateria.BLOQUEADO
                    esCritico = true
                }
                if (simuladosReprobados.contains(materia.id)) {
                    esCritico = true
                }
            }
            MateriaVisual(materia, estado, esCritico)
        }
    }

    val semestres = listaVisual.groupBy {
        if (it.datos is Ramo) it.datos.semestre else (it.datos as Modulos).semestre
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Malla curricular", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            "Simular",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(4.dp))
                        Switch(
                            checked = modoSimulacion,
                            onCheckedChange = {
                                modoSimulacion = it
                                simuladosReprobados = emptySet()
                            },
                            modifier = Modifier.scale(0.8f),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.error,
                                checkedTrackColor = MaterialTheme.colorScheme.errorContainer
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // CORRECCIÓN: Fondo dinámico
                .background(MaterialTheme.colorScheme.background)
        ) {

            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                LeyendaItem(Color(0xFF4CAF50), "Aprobado")
                Spacer(Modifier.width(8.dp))
                LeyendaItem(Color(0xFF2196F3), "Disponible")
                Spacer(Modifier.width(8.dp))
                LeyendaItem(Color.Gray, "Bloqueado")
                Spacer(Modifier.width(8.dp))
                LeyendaItem(Color.Red, "Imposible")
            }

            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (i in 1..11) {
                    item {
                        ColumnSemestre(
                            numero = i,
                            materias = semestres[i] ?: emptyList(),
                            onClick = { id ->
                                if (modoSimulacion) {
                                    simuladosReprobados = if (simuladosReprobados.contains(id)) {
                                        simuladosReprobados - id
                                    } else {
                                        simuladosReprobados + id
                                    }
                                } else {
                                    val yaEstaba = historial.any { it.idMateria == id && it.aprobado }
                                    if (yaEstaba) {
                                        val nuevoHistorial = historial.toMutableList()
                                        nuevoHistorial.removeAll { it.idMateria == id }
                                        RepositorioEnMemoria.borrarHistorial()
                                        nuevoHistorial.forEach { RepositorioEnMemoria.guardarNota(it) }
                                        historial = RepositorioEnMemoria.obtenerHistorial().toList()
                                    } else {
                                        RepositorioEnMemoria.guardarNota(Resultado(id, 6.0, true))
                                        historial = RepositorioEnMemoria.obtenerHistorial().toList()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnSemestre(numero: Int, materias: List<MateriaVisual>, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Semestre $numero",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        materias.forEach { visual ->
            CardMateria(visual, onClick)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CardMateria(visual: MateriaVisual, onClick: (String) -> Unit) {
    val materia = visual.datos
    val colorFondo = if (visual.esCritico) {
        Color(0xFFFFEBEE)
    } else {
        when (visual.estado) {
            EstadoMateria.APROBADA -> Color(0xFF4CAF50)
            EstadoMateria.DISPONIBLE -> Color(0xFF2196F3)
            EstadoMateria.BLOQUEADO -> Color.LightGray
        }
    }

    val colorBorde = if (visual.esCritico) Color.Red else Color.Transparent
    val grosorBorde = if (visual.esCritico) 2.dp else 0.dp
    val colorTexto = if (visual.estado == EstadoMateria.BLOQUEADO || visual.esCritico) Color.Black else Color.White
    val esHito = materia is Modulos

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if(esHito) 130.dp else 110.dp)
            .clickable(enabled = true) { onClick(materia.id) },
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        border = BorderStroke(grosorBorde, colorBorde),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (esHito) {
                Text("IMPORTANTE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Red)
            }
            Text(
                text = materia.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorTexto,
                maxLines = 3
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(materia.id, fontSize = 10.sp, color = colorTexto)
                val textoCreditos = if (materia is Ramo) {
                    "${materia.creditos} Cr"
                } else {
                    val m = materia as Modulos
                    "Req: ${m.creditosRequeridos} Cr"
                }
                Text(textoCreditos, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colorTexto)
            }
        }
    }
}

@Composable
fun LeyendaItem(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(4.dp))
        Text(texto, fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

fun Modifier.scale(scale: Float): Modifier = this.then(
    Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
)