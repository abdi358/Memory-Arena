package com.example.memoryarena.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.memoryarena.ui.theme.PlayerBlue
import com.example.memoryarena.ui.theme.PlayerGreen
import com.example.memoryarena.ui.theme.PlayerRed
import com.example.memoryarena.ui.theme.PlayerYellow
import com.example.memoryarena.ui.theme.SunPlayerBlue
import com.example.memoryarena.ui.theme.SunPlayerGreen
import com.example.memoryarena.ui.theme.SunPlayerRed
import com.example.memoryarena.ui.theme.SunPlayerYellow

data class Player(
    val id: Int,
    val name: String,
    val tag: String,
    val initial: String,
    val auraColor: Color,
    val pairsFound: Int = 0,
    val score: Int = 0
)

enum class CardSymbol(val icon: ImageVector, val label: String) {
    PSYCHOLOGY(Icons.Filled.Psychology, "Brain"),
    BOLT(Icons.Filled.Bolt, "Lightning"),
    DIAMOND(Icons.Filled.Diamond, "Crystal"),
    FAVORITE(Icons.Filled.Favorite, "Heart"),
    ESPORTS(Icons.Filled.SportsEsports, "Arcade"),
    FIRE(Icons.Filled.LocalFireDepartment, "Flame"),
    STAR(Icons.Filled.Star, "Star"),
    MEDAL(Icons.Filled.WorkspacePremium, "Medal"),
    SPA(Icons.Filled.Spa, "Lotus"),
    TOKEN(Icons.Filled.Token, "Glyph"),
    EXTENSION(Icons.Filled.Extension, "Puzzle"),
    MAGIC(Icons.Filled.AutoAwesome, "Spark")
}

data class MemoryCard(
    val id: Int,
    val symbol: CardSymbol,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false,
    val matchedByColor: Color? = null
)

object DefaultPlayers {
    fun createPlayers(
        names: List<String>,
        isSunlightMode: Boolean = false
    ): List<Player> {
        val darkColors = listOf(PlayerRed, PlayerGreen, PlayerBlue, PlayerYellow)
        val sunColors = listOf(SunPlayerRed, SunPlayerGreen, SunPlayerBlue, SunPlayerYellow)
        val colors = if (isSunlightMode) sunColors else darkColors

        return names.mapIndexed { index, rawName ->
            val cleanName = rawName.trim().ifEmpty { "Player ${index + 1}" }
            val initial = cleanName.first().uppercase()
            val color = colors[index % colors.size]
            Player(
                id = index + 1,
                name = cleanName,
                tag = "P${index + 1}",
                initial = initial,
                auraColor = color
            )
        }
    }

    fun defaultNamesForCount(count: Int): List<String> = when (count) {
        1 -> listOf("Alex")
        2 -> listOf("Alex", "Maya")
        3 -> listOf("Alex", "Maya", "Leo")
        4 -> listOf("Alex", "Maya", "Leo", "Zoe")
        else -> listOf("Alex")
    }
}
