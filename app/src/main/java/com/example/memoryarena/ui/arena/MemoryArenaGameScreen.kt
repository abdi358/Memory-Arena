package com.example.memoryarena.ui.arena

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.memoryarena.model.CardSymbol
import com.example.memoryarena.model.DefaultPlayers
import com.example.memoryarena.model.MemoryCard
import com.example.memoryarena.model.Player
import com.example.memoryarena.ui.theme.ArenaPrimary
import com.example.memoryarena.ui.theme.ArenaSecondary
import com.example.memoryarena.ui.theme.ArenaSurface
import com.example.memoryarena.ui.theme.ArenaSurfaceContainer
import com.example.memoryarena.ui.theme.ArenaSurfaceHigh
import com.example.memoryarena.ui.theme.ArenaSurfaceLowest
import com.example.memoryarena.ui.theme.ArenaTertiary
import com.example.memoryarena.ui.theme.MemoryArenaTheme
import com.example.memoryarena.ui.theme.SunOnSurface
import com.example.memoryarena.ui.theme.SunOnSurfaceVariant
import com.example.memoryarena.ui.theme.SunPlayerBlue
import com.example.memoryarena.ui.theme.SunSurface
import com.example.memoryarena.ui.theme.SunSurfaceContainer
import com.example.memoryarena.ui.theme.SunSurfaceHigh
import kotlinx.coroutines.delay

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MemoryArenaGameScreenPreview() {
    MemoryArenaTheme {
        MemoryArenaGameScreen(
            playerCount = 4,
            playerNames = listOf("Alex", "Maya", "Leo", "Zoe")
        )
    }
}

@Composable
fun MemoryArenaGameScreen(
    playerCount: Int = 2,
    playerNames: List<String> = DefaultPlayers.defaultNamesForCount(playerCount),
    initialSunlightMode: Boolean = false,
    onExitToSetup: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isSunlightMode by remember { mutableStateOf(initialSunlightMode) }

    // Players state with dynamic aura updating when toggling sun mode
    var players by remember(playerNames) {
        mutableStateOf(DefaultPlayers.createPlayers(playerNames, isSunlightMode))
    }

    // Update players aura color when sunlight mode toggles, preserving scores
    fun toggleSunlightMode() {
        val nextMode = !isSunlightMode
        isSunlightMode = nextMode
        val freshAuras = DefaultPlayers.createPlayers(playerNames, nextMode)
        players = players.mapIndexed { i, p ->
            val fresh = freshAuras.getOrElse(i) { p }
            p.copy(auraColor = fresh.auraColor)
        }
    }

    var activePlayerIndex by remember { mutableIntStateOf(0) }
    var totalMoves by remember { mutableIntStateOf(0) }
    var streakCombo by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Turn countdown timer (20 seconds)
    val maxTurnTime = 20
    var turnTimeRemaining by remember { mutableIntStateOf(maxTurnTime) }
    var turnProgress by remember { mutableFloatStateOf(1f) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // Card Deck setup:
    // 4 Players: 12 pairs = 24 cards (6 rows x 4 columns, adding two rows!)
    // 1P, 2P, 3P: 8 pairs = 16 cards (4 rows x 4 columns)
    val pairCount = if (playerCount == 4) 12 else 8
    var cards by remember(playerCount) {
        mutableStateOf(generateDeck(pairCount))
    }
    var flippedCardIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var isCheckingMatch by remember { mutableStateOf(false) }

    // Reset game helper
    fun restartGame() {
        players = DefaultPlayers.createPlayers(playerNames, isSunlightMode)
        activePlayerIndex = 0
        totalMoves = 0
        streakCombo = 0
        isGameOver = false
        cards = generateDeck(pairCount)
        flippedCardIds = emptyList()
        isCheckingMatch = false
        turnTimeRemaining = maxTurnTime
        turnProgress = 1f
        isTimerRunning = true
    }

    // Advance turn to next player
    fun advanceTurn() {
        if (players.isNotEmpty()) {
            activePlayerIndex = (activePlayerIndex + 1) % players.size
        }
        turnTimeRemaining = maxTurnTime
        turnProgress = 1f
        streakCombo = 0
    }

    // Turn countdown timer effect
    LaunchedEffect(activePlayerIndex, isGameOver, isTimerRunning) {
        turnTimeRemaining = maxTurnTime
        turnProgress = 1f
        while (turnTimeRemaining > 0 && !isGameOver && isTimerRunning) {
            delay(1000L)
            turnTimeRemaining--
            turnProgress = turnTimeRemaining.toFloat() / maxTurnTime.toFloat()
        }
        if (turnTimeRemaining <= 0 && !isGameOver && !isCheckingMatch) {
            // Turn timeout: flip back uncompleted cards and pass turn
            cards = cards.map { card ->
                if (flippedCardIds.contains(card.id) && !card.isMatched) {
                    card.copy(isFaceUp = false)
                } else card
            }
            flippedCardIds = emptyList()
            advanceTurn()
        }
    }

    // Match check effect when 2 cards are flipped
    LaunchedEffect(flippedCardIds) {
        if (flippedCardIds.size == 2 && !isCheckingMatch) {
            isCheckingMatch = true
            totalMoves++
            val firstId = flippedCardIds[0]
            val secondId = flippedCardIds[1]
            val firstCard = cards.find { it.id == firstId }
            val secondCard = cards.find { it.id == secondId }

            if (firstCard != null && secondCard != null) {
                if (firstCard.symbol == secondCard.symbol) {
                    // Match found!
                    delay(350L)
                    val activePlayer = players.getOrElse(activePlayerIndex) { players[0] }
                    cards = cards.map { card ->
                        if (card.id == firstId || card.id == secondId) {
                            card.copy(isMatched = true, matchedByColor = activePlayer.auraColor)
                        } else card
                    }
                    streakCombo++
                    val pointsEarned = 100 + (streakCombo * 20)
                    players = players.mapIndexed { idx, p ->
                        if (idx == activePlayerIndex) {
                            p.copy(
                                pairsFound = p.pairsFound + 1,
                                score = p.score + pointsEarned
                            )
                        } else p
                    }
                    flippedCardIds = emptyList()
                    isCheckingMatch = false

                    // Check for Game Over (all pairs matched)
                    if (cards.all { it.isMatched }) {
                        isGameOver = true
                        isTimerRunning = false
                    } else {
                        // Bonus extra turn for matching pair! Reset timer
                        turnTimeRemaining = maxTurnTime
                        turnProgress = 1f
                    }
                } else {
                    // Mismatch: delay briefly to let players memorize, then flip back
                    delay(1100L)
                    cards = cards.map { card ->
                        if (card.id == firstId || card.id == secondId) {
                            card.copy(isFaceUp = false)
                        } else card
                    }
                    flippedCardIds = emptyList()
                    isCheckingMatch = false
                    advanceTurn()
                }
            }
        }
    }

    // Card click handler
    val onCardClick: (MemoryCard) -> Unit = { clickedCard ->
        if (!isCheckingMatch && flippedCardIds.size < 2 && !clickedCard.isFaceUp && !clickedCard.isMatched) {
            cards = cards.map { card ->
                if (card.id == clickedCard.id) card.copy(isFaceUp = true) else card
            }
            flippedCardIds = flippedCardIds + clickedCard.id
        }
    }

    // Determine leader
    val maxScore = players.maxOfOrNull { it.score } ?: 0
    val activePlayer = players.getOrElse(activePlayerIndex) { players[0] }

    val screenBg = if (isSunlightMode) SunSurface else ArenaSurface
    val controlBg = if (isSunlightMode) SunSurfaceHigh else ArenaSurfaceHigh
    val controlIconTint = if (isSunlightMode) SunOnSurface else Color.White.copy(alpha = 0.8f)
    val telemetryBg = if (isSunlightMode) SunSurfaceContainer else ArenaSurfaceHigh
    val telemetryBorder = if (isSunlightMode) Color.Black.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.08f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(screenBg)
            .statusBarsPadding()
    ) {
        // Atmospheric Ambient Glow (only in Night Mode)
        if (!isSunlightMode) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(240.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(ArenaSecondary.copy(alpha = 0.12f), Color.Transparent)
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(240.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(ArenaPrimary.copy(alpha = 0.12f), Color.Transparent)
                        )
                    )
            )
        }

        // ════════════════════════════════════════════════════════════════════════
        // 1. TOP BAR: TOP-LEFT POD | CENTER CONTROLS HUD | TOP-RIGHT POD
        // ════════════════════════════════════════════════════════════════════════
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TOP-LEFT CORNER: Player 1 (Alex / P1)
            PlayerCornerPod(
                player = players[0],
                isActive = activePlayerIndex == 0,
                turnProgress = if (activePlayerIndex == 0) turnProgress else 1f,
                isLeader = (players[0].score == maxScore && maxScore > 0),
                position = CornerPosition.TOP_LEFT,
                isSunlightMode = isSunlightMode
            )

            // Center Quick HUD: Sun/Night Toggle, Restart (Compact 2-icon pill to keep player pods full-size)
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(controlBg)
                    .border(1.dp, telemetryBorder, CircleShape)
                    .padding(horizontal = 3.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sunlight / Night Mode On-the-Fly Toggle
                IconButton(
                    onClick = { toggleSunlightMode() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isSunlightMode) Icons.Filled.WbSunny else Icons.Filled.Nightlight,
                        contentDescription = "Toggle Sun/Night Mode",
                        tint = if (isSunlightMode) Color(0xFFD97706) else ArenaPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Restart Match
                IconButton(
                    onClick = { restartGame() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Restart Round",
                        tint = controlIconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // TOP-RIGHT CORNER: Player 2 (if 3 or 4 players)
            if (playerCount >= 3) {
                PlayerCornerPod(
                    player = players[1],
                    isActive = activePlayerIndex == 1,
                    turnProgress = if (activePlayerIndex == 1) turnProgress else 1f,
                    isLeader = (players[1].score == maxScore && maxScore > 0),
                    position = CornerPosition.TOP_RIGHT,
                    isSunlightMode = isSunlightMode
                )
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }
        }

        // ════════════════════════════════════════════════════════════════════════
        // 2. CENTER STAGE: TURN BANNER + CARD GRID + TELEMETRY CHIP
        // ════════════════════════════════════════════════════════════════════════
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = if (playerCount == 4) 10.dp else 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Turn Indicator Pill
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isSunlightMode) activePlayer.auraColor.copy(alpha = 0.15f)
                        else activePlayer.auraColor.copy(alpha = 0.18f)
                    )
                    .border(1.5.dp, activePlayer.auraColor, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(activePlayer.auraColor)
                    )
                    Text(
                        text = "${activePlayer.name.uppercase()} • ${if (flippedCardIds.isEmpty()) "PICK 1ST CARD" else "PICK 2ND CARD"}",
                        color = activePlayer.auraColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (playerCount == 4) 6.dp else 10.dp))

            // Playable 4x4 (16 cards) or 4x6 (24 cards) Grid in Center
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(if (playerCount == 4) 6.dp else 8.dp),
                verticalArrangement = Arrangement.spacedBy(if (playerCount == 4) 6.dp else 8.dp),
                contentPadding = PaddingValues(2.dp)
            ) {
                items(cards, key = { it.id }) { card ->
                    MemoryCardView(
                        card = card,
                        onClick = { onCardClick(card) },
                        isSunlightMode = isSunlightMode,
                        iconSize = if (playerCount == 4) 22.dp else 28.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (playerCount == 4) 6.dp else 10.dp))

            // Live Telemetry Chip (Centered below Grid)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(telemetryBg)
                    .border(1.dp, telemetryBorder, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val pairsFound = players.sumOf { it.pairsFound }
                    val telemetrySecondary = if (isSunlightMode) SunPlayerBlue else ArenaSecondary
                    val telemetryMovesColor = if (isSunlightMode) SunOnSurfaceVariant else Color(0xFFCBC3D7)

                    Text(
                        text = "Found: $pairsFound/$pairCount",
                        color = telemetrySecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "•",
                        color = if (isSunlightMode) Color.DarkGray else Color.White.copy(alpha = 0.3f),
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Moves: $totalMoves",
                        color = telemetryMovesColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                    if (streakCombo > 1) {
                        Text(
                            text = "•",
                            color = if (isSunlightMode) Color.DarkGray else Color.White.copy(alpha = 0.3f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${streakCombo}x Combo",
                            color = if (isSunlightMode) Color(0xFFEA580C) else ArenaTertiary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // ════════════════════════════════════════════════════════════════════════
        // 3. BOTTOM BAR: BOTTOM-LEFT POD | SPACER | BOTTOM-RIGHT POD
        // ════════════════════════════════════════════════════════════════════════
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // BOTTOM-LEFT CORNER: Player 3 in 4-Player mode
            if (playerCount == 4) {
                PlayerCornerPod(
                    player = players[2],
                    isActive = activePlayerIndex == 2,
                    turnProgress = if (activePlayerIndex == 2) turnProgress else 1f,
                    isLeader = (players[2].score == maxScore && maxScore > 0),
                    position = CornerPosition.BOTTOM_LEFT,
                    isSunlightMode = isSunlightMode
                )
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }

            // BOTTOM-RIGHT CORNER:
            // In 2P mode: Player 2 (Maya) sits here (diagonal surround)
            // In 3P mode: Player 3 sits here
            // In 4P mode: Player 4 (Zoe) sits here
            val bottomTrailingPlayer = when (playerCount) {
                2 -> players.getOrNull(1)
                3 -> players.getOrNull(2)
                4 -> players.getOrNull(3)
                else -> null
            }

            if (bottomTrailingPlayer != null) {
                val trailingIndex = when (playerCount) {
                    2 -> 1
                    3 -> 2
                    else -> 3
                }
                PlayerCornerPod(
                    player = bottomTrailingPlayer,
                    isActive = activePlayerIndex == trailingIndex,
                    turnProgress = if (activePlayerIndex == trailingIndex) turnProgress else 1f,
                    isLeader = (bottomTrailingPlayer.score == maxScore && maxScore > 0),
                    position = CornerPosition.BOTTOM_RIGHT,
                    isSunlightMode = isSunlightMode
                )
            }
        }

        // ════════════════════════════════════════════════════════════════════════
        // 4. VICTORY / GAME RESULTS DIALOG
        // ════════════════════════════════════════════════════════════════════════
        if (isGameOver) {
            val winner = players.maxByOrNull { it.score } ?: players[0]
            val dialogBg = if (isSunlightMode) SunSurfaceContainer else ArenaSurfaceHigh
            val dialogTextPrimary = if (isSunlightMode) SunOnSurface else Color(0xFFE4E0F5)
            val dialogTableBg = if (isSunlightMode) SunSurfaceHigh else ArenaSurfaceContainer

            Dialog(onDismissRequest = {}) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = dialogBg,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(24.dp, RoundedCornerShape(24.dp)),
                    border = androidx.compose.foundation.BorderStroke(2.dp, winner.auraColor)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Crown & Winner Avatar
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(winner.auraColor.copy(alpha = if (isSunlightMode) 0.25f else 0.2f))
                                .border(2.dp, winner.auraColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = winner.initial,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = winner.auraColor
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (playerCount == 1) "SOLO CHALLENGE COMPLETED!" else "VICTORY ROYALE!",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = winner.auraColor,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "${winner.name} finished with ${winner.pairsFound} pairs (${winner.score} pts)",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = dialogTextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Final Leaderboard
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(dialogTableBg)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val sortedPlayers = players.sortedByDescending { it.score }
                            sortedPlayers.forEachIndexed { rank, p ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "#${rank + 1}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (rank == 0) (if (isSunlightMode) Color(0xFFEA580C) else ArenaTertiary) else Color.Gray
                                        )
                                        Text(
                                            text = p.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isSunlightMode) SunOnSurface else Color.White
                                        )
                                        Text(
                                            text = "(${p.tag})",
                                            fontSize = 10.sp,
                                            color = p.auraColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "${p.pairsFound} pairs • ${p.score} pts",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = p.auraColor
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rematch Button
                        Button(
                            onClick = { restartGame() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = winner.auraColor,
                                contentColor = if (isSunlightMode) Color.White else ArenaSurfaceLowest
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PLAY AGAIN (REMATCH)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Return to Setup Button
                        OutlinedButton(
                            onClick = { onExitToSetup() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, telemetryBorder)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = dialogTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CHANGE PLAYERS / SETUP",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = dialogTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun generateDeck(pairCount: Int): List<MemoryCard> {
    val allSymbols = CardSymbol.entries
    val chosenSymbols = allSymbols.shuffled().take(pairCount)
    val cardPairs = chosenSymbols.flatMapIndexed { index, symbol ->
        listOf(
            MemoryCard(id = index * 2, symbol = symbol),
            MemoryCard(id = index * 2 + 1, symbol = symbol)
        )
    }
    return cardPairs.shuffled()
}
