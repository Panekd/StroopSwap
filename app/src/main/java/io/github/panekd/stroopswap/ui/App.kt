package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Settings
@Serializable
object Info
@Serializable
object Game

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent)
        ) {
            composable<Home> {
                HomeScreen(
                    toSettings = { navController.navigate(route = Settings) },
                    toInfo = { navController.navigate(route = Info) },
                    toGame = { navController.navigate(route = Game) }
                )
            }
            composable<Settings> {
                SettingsScreen()
            }
            composable<Info> {
                InfoScreen()
            }
            composable<Game> {
                GameScreen()
            }
        }
    }