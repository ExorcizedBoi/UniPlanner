package com.example.uniplanner.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniplanner.models.EstadoMateria
import com.example.uniplanner.models.Materia
import com.example.uniplanner.persistencia.MallaData

@Composable
fun MallaScreen(irAHome: () -> Unit) {
    var aprobadas by remember { mutableStateOf(setOf<String>()) }

    val materiasCalculadas = remember(aprobadas) {
        MallaData.listaMaterias.map { materia ->
            val estaAprobada = aprobadas.contains(materia.id)
            val requisitosCumplidos = materia.prerrequisitos.all { reqId ->
                aprobadas.contains(reqId)
            }

            val estadoFinal = when {
                estaAprobada -> EstadoMateria.APROBADA
                requisitosCumplidos -> EstadoMateria.DISPONIBLE
                else -> EstadoMateria.BLOQUEADO
            }
            materia.copy(estado = estadoFinal)
        }
    }

    val semestres = materiasCalculadas.groupBy { it.semestre }

    Column(Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = irAHome) { Text("← Volver") }
            Spacer(Modifier.width(16.dp))
            Text("Malla Interactiva", style = MaterialTheme.typography.headlineSmall)
        }

        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            LeyendaItem(Color(0xFF4CAF50), "Aprobado")
            Spacer(Modifier.width(8.dp))
            LeyendaItem(Color(0xFF2196F3), "Disponible")
            Spacer(Modifier.width(8.dp))
            LeyendaItem(Color.Gray, "Bloqueado")
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
                        onMateriaClick = { id ->
                            aprobadas = if (aprobadas.contains(id)) {
                                aprobadas - id
                            } else {
                                aprobadas + id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ColumnSemestre(numero: Int, materias: List<Materia>, onMateriaClick: (String) -> Unit) {
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
            textAlign = TextAlign.Center
        )

        materias.forEach { materia ->
            CardMateria(materia, onMateriaClick)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CardMateria(materia: Materia, onClick: (String) -> Unit) {
    val colorFondo = when (materia.estado) {
        EstadoMateria.APROBADA -> Color(0xFF4CAF50)
        EstadoMateria.DISPONIBLE -> Color(0xFF2196F3)
        EstadoMateria.BLOQUEADO -> Color.LightGray
    }

    val colorTexto = if (materia.estado == EstadoMateria.BLOQUEADO) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable(enabled = materia.estado != EstadoMateria.BLOQUEADO) {
                onClick(materia.id)
            },
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
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
                Text("${materia.creditos} Cr", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colorTexto)
            }
        }
    }
}

@Composable
fun LeyendaItem(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(4.dp))
        Text(texto, fontSize = 12.sp)
    }
}