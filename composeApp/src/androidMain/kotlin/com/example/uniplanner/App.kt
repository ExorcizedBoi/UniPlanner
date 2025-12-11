package com.example.uniplanner

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uniplanner.vistas.CalculadoraScreen
import com.example.uniplanner.vistas.HomeScreen
import com.example.uniplanner.vistas.MallaScreen
import com.example.uniplanner.vistas.EvaluacionesScreen
import com.example.uniplanner.vistas.AjustesScreen
import kotlinx.coroutines.launch

enum class Pantalla {
    HOME,
    EVALUACIONES,
    CALCULADORA,
    MALLA,
    AJUSTES
}

@Composable
fun App() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf(Pantalla.HOME) }

    var esOscuro by remember { mutableStateOf(false) }
    val colores = if (esOscuro) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colores) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        "UniPlanner Pro",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = pantallaActual == Pantalla.HOME,
                        icon = { Icon(Icons.Filled.Home, null) },
                        onClick = {
                            pantallaActual = Pantalla.HOME
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Evaluaciones") },
                        selected = pantallaActual == Pantalla.EVALUACIONES,
                        icon = { Icon(Icons.Filled.DateRange, null) },
                        onClick = {
                            pantallaActual = Pantalla.EVALUACIONES
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    NavigationDrawerItem(
                        label = { Text("Malla Interactiva") },
                        selected = pantallaActual == Pantalla.MALLA,
                        icon = { Icon(Icons.Filled.List, null) },
                        onClick = {
                            pantallaActual = Pantalla.MALLA
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Calculadora Notas") },
                        selected = pantallaActual == Pantalla.CALCULADORA,
                        icon = { Icon(Icons.Filled.Calculate, null) },
                        onClick = {
                            pantallaActual = Pantalla.CALCULADORA
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("Ajustes") },
                        selected = pantallaActual == Pantalla.AJUSTES,
                        icon = { Icon(Icons.Filled.Settings, null) },
                        onClick = {
                            pantallaActual = Pantalla.AJUSTES
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        ) {
            Surface(
                modifier = Modifier.padding(),
                color = MaterialTheme.colorScheme.background
            ) {
                when (pantallaActual) {
                    Pantalla.HOME -> HomeScreen(onMenuClick = { scope.launch { drawerState.open() } })
                    Pantalla.EVALUACIONES -> EvaluacionesScreen(onMenuClick = { scope.launch { drawerState.open() } })
                    Pantalla.CALCULADORA -> CalculadoraScreen(onMenuClick = { scope.launch { drawerState.open() } })
                    Pantalla.MALLA -> MallaScreen(onMenuClick = { scope.launch { drawerState.open() } })
                    Pantalla.AJUSTES -> AjustesScreen(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        esOscuro = esOscuro,
                        onCambioTema = { esOscuro = it }
                    )
                }
            }
        }
    }
}