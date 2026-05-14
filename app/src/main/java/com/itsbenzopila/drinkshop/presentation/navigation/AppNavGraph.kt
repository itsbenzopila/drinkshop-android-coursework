package com.itsbenzopila.drinkshop.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.itsbenzopila.drinkshop.di.AppContainer
import com.itsbenzopila.drinkshop.presentation.auth.LoginScreen
import com.itsbenzopila.drinkshop.presentation.auth.RegisterScreen
import com.itsbenzopila.drinkshop.presentation.cart.CartScreen
import com.itsbenzopila.drinkshop.presentation.catalog.CatalogScreen
import com.itsbenzopila.drinkshop.presentation.catalog.DrinkDetailScreen
import com.itsbenzopila.drinkshop.presentation.orders.OrdersScreen
import com.itsbenzopila.drinkshop.presentation.profile.ProfileScreen
import com.itsbenzopila.drinkshop.presentation.splash.SplashScreen

private data class BottomItem(
    val screen: Screen,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val bottomBarRoutes = listOf(
    BottomItem(Screen.Catalog, "Каталог", Icons.Default.LocalCafe),
    BottomItem(Screen.Cart, "Корзина", Icons.Default.ShoppingCart),
    BottomItem(Screen.Orders, "Заказы", Icons.Default.Receipt),
    BottomItem(Screen.Profile, "Профиль", Icons.Default.Person),
)

@Composable
fun AppNavGraph(container: AppContainer) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val showBottomBar = currentRoute in bottomBarRoutes.map { it.screen.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomBarRoutes.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                if (currentRoute != item.screen.route) {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(Screen.Catalog.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = androidx.compose.ui.Modifier.padding(padding),
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    container = container,
                    onAuthenticated = {
                        navController.navigate(Screen.Catalog.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onUnauthenticated = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    container = container,
                    onSignedIn = {
                        navController.navigate(Screen.Catalog.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onGoRegister = { navController.navigate(Screen.Register.route) },
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    container = container,
                    onSignedUp = {
                        navController.navigate(Screen.Catalog.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Screen.Catalog.route) {
                CatalogScreen(
                    container = container,
                    onOpenDrink = { id -> navController.navigate(Screen.DrinkDetail.route(id)) },
                )
            }
            composable(
                Screen.DrinkDetail.route,
                arguments = listOf(navArgument(Screen.DrinkDetail.ARG) { type = NavType.LongType }),
            ) { entry ->
                val drinkId = entry.arguments?.getLong(Screen.DrinkDetail.ARG) ?: return@composable
                DrinkDetailScreen(
                    container = container,
                    drinkId = drinkId,
                    onBack = { navController.popBackStack() },
                    onAdded = { navController.navigate(Screen.Cart.route) },
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    container = container,
                    onOrderPlaced = {
                        navController.navigate(Screen.Orders.route) {
                            popUpTo(Screen.Catalog.route)
                        }
                    },
                )
            }
            composable(Screen.Orders.route) {
                OrdersScreen(container = container)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    container = container,
                    onSignedOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}
