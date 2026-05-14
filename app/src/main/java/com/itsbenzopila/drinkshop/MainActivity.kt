package com.itsbenzopila.drinkshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.itsbenzopila.drinkshop.presentation.navigation.AppNavGraph
import com.itsbenzopila.drinkshop.presentation.theme.DrinkshopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as DrinkshopApplication).container
        setContent {
            DrinkshopTheme {
                AppNavGraph(container = container)
            }
        }
    }
}
