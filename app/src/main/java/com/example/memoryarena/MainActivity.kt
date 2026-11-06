package com.example.memoryarena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.memoryarena.ui.arena.MemoryArenaGameScreen
import com.example.memoryarena.ui.setup.GameSetupScreen
import com.example.memoryarena.ui.theme.MemoryArenaTheme

sealed interface AppScreen {
    data class Setup(
        val playerCount: Int = 2,
        val isSunlightMode: Boolean = false
    ) : AppScreen

    data class Game(
        val playerCount: Int,
        val playerNames: List<String>,
        val isSunlightMode: Boolean
    ) : AppScreen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()
        setContent {
            MemoryArenaTheme {
                var currentScreen by remember {
                    mutableStateOf<AppScreen>(AppScreen.Setup())
                }

                when (val screen = currentScreen) {
                    is AppScreen.Setup -> {
                        GameSetupScreen(
                            initialPlayerCount = screen.playerCount,
                            initialSunlightMode = screen.isSunlightMode,
                            onStartGame = { count, names, sunMode ->
                                currentScreen = AppScreen.Game(
                                    playerCount = count,
                                    playerNames = names,
                                    isSunlightMode = sunMode
                                )
                            }
                        )
                    }
                    is AppScreen.Game -> {
                        BackHandler {
                            currentScreen = AppScreen.Setup(
                                playerCount = screen.playerCount,
                                isSunlightMode = screen.isSunlightMode
                            )
                        }
                        MemoryArenaGameScreen(
                            playerCount = screen.playerCount,
                            playerNames = screen.playerNames,
                            initialSunlightMode = screen.isSunlightMode,
                            onExitToSetup = {
                                currentScreen = AppScreen.Setup(
                                    playerCount = screen.playerCount,
                                    isSunlightMode = screen.isSunlightMode
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}
