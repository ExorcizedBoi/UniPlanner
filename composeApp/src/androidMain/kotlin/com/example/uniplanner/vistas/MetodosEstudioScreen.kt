package com.example.uniplanner.vistas

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetodosEstudioScreen(onMenuClick: () -> Unit) {
    var tabSeleccionada by remember { mutableIntStateOf(0) }
    val titulosTabs = listOf("Reloj Pomodoro", "Técnicas")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Zona de estudio", fontWeight = FontWeight.Bold) },
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
        ) {
            // TABS
            TabRow(
                selectedTabIndex = tabSeleccionada,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                titulosTabs.forEachIndexed { index, titulo ->
                    Tab(
                        selected = tabSeleccionada == index,
                        onClick = { tabSeleccionada = index },
                        text = { Text(titulo, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // CONTENIDO
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if (tabSeleccionada == 0) {
                    VistaPomodoro()
                } else {
                    VistaTecnicas()
                }
            }
        }
    }
}

@Composable
fun VistaPomodoro() {
    val context = LocalContext.current

    var esModoLargo by remember { mutableStateOf(false) }
    var esDescanso by remember { mutableStateOf(false) }
    var estaCorriendo by remember { mutableStateOf(false) }

    val tiempoEstudio = if (esModoLargo) 50 * 60 else 25 * 60
    val tiempoDescanso = if (esModoLargo) 10 * 60 else 5 * 60

    var tiempoRestante by remember { mutableIntStateOf(tiempoEstudio) }

    LaunchedEffect(esModoLargo, esDescanso) {
        if (!estaCorriendo) {
            tiempoRestante = if (esDescanso) tiempoDescanso else tiempoEstudio
        }
    }

    LaunchedEffect(estaCorriendo, tiempoRestante) {
        if (estaCorriendo && tiempoRestante > 0) {
            delay(1000L)
            tiempoRestante--
        } else if (tiempoRestante == 0 && estaCorriendo) {
            estaCorriendo = false

            val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500)
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(500)
                }
            }
        }
    }

    val minutos = tiempoRestante / 60
    val segundos = tiempoRestante % 60
    val tiempoFormateado = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            FilterChip(
                selected = !esModoLargo,
                onClick = {
                    esModoLargo = false
                    esDescanso = false
                    estaCorriendo = false
                    tiempoRestante = 25 * 60
                },
                label = { Text("Clásico (25/5)") },
                modifier = Modifier.padding(end = 8.dp)
            )
            FilterChip(
                selected = esModoLargo,
                onClick = {
                    esModoLargo = true
                    esDescanso = false
                    estaCorriendo = false
                    tiempoRestante = 50 * 60
                },
                label = { Text("Largo (50/10)") }
            )
        }

        Surface(
            color = if (esDescanso) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = if (esDescanso) "Hora del descanso" else "Hora de concentrarte",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = if (esDescanso) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            val maxTiempo = if (esDescanso) tiempoDescanso else tiempoEstudio
            val progreso = tiempoRestante.toFloat() / maxTiempo.toFloat()

            CircularProgressIndicator(
                progress = { 1f - progreso },
                modifier = Modifier.fillMaxSize(),
                color = if(esDescanso) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                strokeWidth = 10.dp,
            )

            Text(
                text = tiempoFormateado,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalIconButton(
                onClick = {
                    estaCorriendo = false
                    tiempoRestante = if (esDescanso) tiempoDescanso else tiempoEstudio
                },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar")
            }

            Button(
                onClick = { estaCorriendo = !estaCorriendo },
                modifier = Modifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = if (estaCorriendo) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(if (estaCorriendo) "PAUSAR" else "INICIAR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            esDescanso = !esDescanso
            estaCorriendo = false
        }) {
            Text(if (esDescanso) "Saltar a Modo Estudio" else "Saltar a Modo Descanso")
        }
    }
}

@Composable
fun VistaTecnicas() {
    val tecnicas = listOf(
        Tecnica("Método Pomodoro Clásico", "Trabaja 25 min, descansa 5. El estándar de oro para evitar la fatiga mental."),
        Tecnica("Método Pomodoro 50/10", "Trabaja 50 min, descansa 10. Ideal para tareas complejas o programación profunda, dando mayor inmersión con menos interrupciones."),
        Tecnica("Active Recall", "No solo leas. Cierra el libro e intenta recordar y explicar lo aprendido. Es la forma más potente de memorizar."),
        Tecnica("Spaced Repetition", "Repasa el contenido en un intervalo de dias (1 día, 3 días, 1 semana). Para asi combatir la curva del olvido."),
        Tecnica("Rubber Ducking", "Explícale tu materia a un patito de goma (o cualquier objeto inanimado). Al expresar, encuentras los errores."),
        Tecnica("Método Feynman", "Intenta explicar lo que haz estudiado en términos tan simples que hasta un niño de 5 años lo entienda.")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text(
                "Mejora tu rendimiento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(tecnicas) { tecnica ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = tecnica.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = tecnica.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

data class Tecnica(val titulo: String, val descripcion: String)