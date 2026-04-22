package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Settings
@Serializable
object Game

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {

    val navController = rememberNavController()

    Scaffold (topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text("Stroop Swap")
            }
        )
    }){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> {
                HomeScreen(
                    onNavigateToSettings = {
                        navController.navigate(route = Settings)
                    }
                )
            }
            composable<Settings> {
                SettingsScreen()
            }
            composable<Game> {
                GameScreen()
            }
        }
    }
}