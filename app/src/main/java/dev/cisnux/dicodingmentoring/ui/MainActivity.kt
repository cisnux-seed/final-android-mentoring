package dev.cisnux.dicodingmentoring.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DicodingMentoringTheme {
                DicodingMentoringApp()
            }
        }
    }
}