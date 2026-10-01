package com.example.horoscopo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.ui.screens.HoroscopeApp
import com.example.horoscopo.ui.theme.HoroscopoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = HoroscopeRepository)
            }
        }
    }
}
