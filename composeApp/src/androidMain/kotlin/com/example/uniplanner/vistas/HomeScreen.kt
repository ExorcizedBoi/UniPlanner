package com.example.uniplanner.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniplanner.persistencia.MallaData
import com.example.uniplanner.persistencia.RepositorioEnMemoria
import com.example.uniplanner.models.Ramo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit
) {
    val historial = RepositorioEnMemoria.obtenerHistorial()

    val totalCreditos = MallaData.listaMaterias.sumOf {
        if (it is Ramo) it.creditos else 0
    }

    val creditosAprobados = MallaData.listaMaterias.filter { materia ->
        historial.any { h -> h.idMateria == materia.id && h.aprobado }
    }.sumOf {
        if (it is Ramo) it.creditos else 0
    }

    val porcentajeAvance = if (totalCreditos > 0) creditosAprobados.toFloat() / totalCreditos.toFloat() else 0f

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Inicio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Abrir Menú")
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
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "UniPlanner Pro",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Gestión Académica Inteligente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Tu Avance Curricular",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Créditos Acumulados", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${(porcentajeAvance * 100).toInt()}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { porcentajeAvance },
                        modifier = Modifier.fillMaxWidth().height(12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surface,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )

                    Text(
                        text = "$creditosAprobados de $totalCreditos créditos",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.End).padding(top = 6.dp)
                    )
                }
            }
        }
    }
}