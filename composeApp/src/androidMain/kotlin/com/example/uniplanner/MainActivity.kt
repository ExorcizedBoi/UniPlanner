package com.example.uniplanner

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.uniplanner.persistencia.RepositorioEnMemoria
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val locale = Locale("es", "CL")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)

        val prefs = getSharedPreferences("uniplanner_datos", Context.MODE_PRIVATE)

        RepositorioEnMemoria.guardarDatos = { clave, valor ->
            prefs.edit().putString(clave, valor).apply()
        }

        RepositorioEnMemoria.cargarDatos = { clave ->
            prefs.getString(clave, "")
        }

        RepositorioEnMemoria.inicializar()

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}