package com.itsbenzopila.drinkshop.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Catalog : Screen("catalog")
    data object DrinkDetail : Screen("drink/{drinkId}") {
        fun route(drinkId: Long) = "drink/$drinkId"
        const val ARG = "drinkId"
    }
    data object Cart : Screen("cart")
    data object Orders : Screen("orders")
    data object Profile : Screen("profile")
}
