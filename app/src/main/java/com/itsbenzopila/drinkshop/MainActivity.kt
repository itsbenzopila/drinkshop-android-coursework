package com.itsbenzopila.drinkshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.itsbenzopila.drinkshop.presentation.navigation.AppNavGraph
import com.itsbenzopila.drinkshop.presentation.theme.DrinkshopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkshopTheme {
                AppNavGraph()
            }
        }
    }
}
