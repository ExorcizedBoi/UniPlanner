package com.example.uniplanner.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniplanner.persistencia.RepositorioEnMemoria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    onMenuClick: () -> Unit,
    esOscuro: Boolean,
    onCambioTema: (Boolean) -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ajustes y Datos") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {

            Text(
                "Apariencia",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Modo Oscuro", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Cambiar apariencia de la app", fontSize = 12.sp)
                    }
                    Switch(
                        checked = esOscuro,
                        onCheckedChange = { onCambioTema(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Zona de Peligro",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )

            AjusteItem(
                titulo = "Reiniciar Carrera Académica",
                subtitulo = "Borra todas las notas y ramos.",
                icono = Icons.Filled.Delete,
                colorIcono = MaterialTheme.colorScheme.error,
                onClick = { mostrarDialogo = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Información",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )

            AjusteItem(
                titulo = "Acerca de UniPlanner",
                subtitulo = "Versión 1.0.0 - Proyecto de Título",
                icono = Icons.Filled.Info,
                colorIcono = MaterialTheme.colorScheme.primary,
                onClick = { }
            )
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("¿Estás seguro?") },
                text = { Text("Esta acción no se puede deshacer. Perderás todo tu progreso registrado.") },
                confirmButton = {
                    Button(
                        onClick = {
                            RepositorioEnMemoria.borrarHistorial()
                            mostrarDialogo = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Sí, Borrar Todo")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun AjusteItem(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    colorIcono: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitulo, fontSize = 12.sp)
            }
        }
    }
}