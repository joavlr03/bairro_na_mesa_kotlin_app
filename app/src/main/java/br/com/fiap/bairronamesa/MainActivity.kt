package br.com.fiap.bairronamesa

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.fiap.bairronamesa.navigation.AppNavHost
import br.com.fiap.bairronamesa.ui.theme.BairroNaMesaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Ícones escuros sempre: o app só tem tema claro (fundo creme)
        val claro = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = claro, navigationBarStyle = claro)
        super.onCreate(savedInstanceState)
        setContent {
            BairroNaMesaTheme {
                AppNavHost()
            }
        }
    }
}
