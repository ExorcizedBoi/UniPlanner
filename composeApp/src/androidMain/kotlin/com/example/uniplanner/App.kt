package com.example.uniplanner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.uniplanner.vistas.CalculadoraScreen
import com.example.uniplanner.vistas.HomeScreen
import com.example.uniplanner.vistas.MallaScreen

enum class Pantalla {
    HOME,
    CALCULADORA,
    MALLA
}

@Composable
fun App() {
    var pantallaActual by remember { mutableStateOf(Pantalla.HOME) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (pantallaActual) {
                Pantalla.HOME -> HomeScreen(
                    irACalculadora = {
                        pantallaActual = Pantalla.CALCULADORA
                    },
                    irAMalla = {
                        pantallaActual = Pantalla.MALLA
                    }
                )
                Pantalla.CALCULADORA -> CalculadoraScreen(
                    irAHome = {
                        pantallaActual = Pantalla.HOME
                    }
                )
                Pantalla.MALLA -> MallaScreen(
                    irAHome = {
                        pantallaActual = Pantalla.HOME
                    }
                )
            }
        }
    }
}