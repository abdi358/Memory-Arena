package com.example.memoryarena.ui.setup

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memoryarena.model.DefaultPlayers
import com.example.memoryarena.ui.theme.ArenaPrimary
import com.example.memoryarena.ui.theme.ArenaSecondary
import com.example.memoryarena.ui.theme.ArenaSurface
import com.example.memoryarena.ui.theme.ArenaSurfaceContainer
import com.example.memoryarena.ui.theme.ArenaSurfaceHigh
import com.example.memoryarena.ui.theme.ArenaSurfaceLowest
import com.example.memoryarena.ui.theme.MemoryArenaTheme
import com.example.memoryarena.ui.theme.PlayerBlue
import com.example.memoryarena.ui.theme.PlayerGreen
import com.example.memoryarena.ui.theme.PlayerRed
import com.example.memoryarena.ui.theme.PlayerYellow
import com.example.memoryarena.ui.theme.SunOnSurface
import com.example.memoryarena.ui.theme.SunOnSurfaceVariant
import com.example.memoryarena.ui.theme.SunPlayerBlue
import com.example.memoryarena.ui.theme.SunPlayerGreen
import com.example.memoryarena.ui.theme.SunPlayerRed
import com.example.memoryarena.ui.theme.SunPlayerYellow
import com.example.memoryarena.ui.theme.SunSurface
import com.example.memoryarena.ui.theme.SunSurfaceContainer
import com.example.memoryarena.ui.theme.SunSurfaceHigh

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun GameSetupScreenPreview() {
    MemoryArenaTheme {
        GameSetupScreen(
            initialPlayerCount = 4,
            initialSunlightMode = false,
            onStartGame = { _, _, _ -> }
        )
    }
}

@Composable
fun GameSetupScreen(
    initialPlayerCount: Int = 2,
    initialSunlightMode: Boolean = false,
    onStartGame: (playerCount: Int, playerNames: List<String>, isSunlightMode: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCount by remember { mutableIntStateOf(initialPlayerCount) }
    var isSunlightMode by remember { mutableStateOf(initialSunlightMode) }

    // Pre-filled names that user can edit
    val names = remember {
        mutableStateListOf("Alex", "Maya", "Leo", "Zoe")
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val surfaceBg = if (isSunlightMode) SunSurface else ArenaSurface
    val textPrimary = if (isSunlightMode) SunOnSurface else Color(0xFFE4E0F5)
    val textSecondary = if (isSunlightMode) SunOnSurfaceVariant else Color(0xFFCBC3D7)
    val cardBg = if (isSunlightMode) SunSurfaceContainer else ArenaSurfaceHigh
    val cardBorder = if (isSunlightMode) Color.Black.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.08f)

    val playerColors = if (isSunlightMode) {
        listOf(SunPlayerRed, SunPlayerGreen, SunPlayerBlue, SunPlayerYellow)
    } else {
        listOf(PlayerRed, PlayerGreen, PlayerBlue, PlayerYellow)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceBg)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ══════════════════════════════════════════════════════════════════
            // HEADER & LOGO
            // ══════════════════════════════════════════════════════════════════
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSunlightMode) SunPlayerBlue.copy(alpha = 0.15f)
                        else ArenaPrimary.copy(alpha = 0.2f)
                    )
                    .border(
                        2.dp,
                        if (isSunlightMode) SunPlayerBlue else ArenaPrimary,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = null,
                    tint = if (isSunlightMode) SunPlayerBlue else ArenaPrimary,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "MEMORY ARENA",
                fontWeight = FontWeight.Black,
                fontSize = 26.sp,
                color = textPrimary,
                letterSpacing = 1.sp
            )

            Text(
                text = "Match Setup • Choose Players",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = textSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ══════════════════════════════════════════════════════════════════
            // STEP 1: SELECT NUMBER OF PLAYERS
            // ══════════════════════════════════════════════════════════════════
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. SELECT PLAYER COUNT",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = if (isSunlightMode) SunPlayerBlue else ArenaSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlayerCountPill(
                            count = 1,
                            title = "Solo",
                            subtitle = "1 Player",
                            isSelected = selectedCount == 1,
                            isSunlightMode = isSunlightMode,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCount = 1 }
                        )
                        PlayerCountPill(
                            count = 2,
                            title = "Duel",
                            subtitle = "2 Players",
                            isSelected = selectedCount == 2,
                            isSunlightMode = isSunlightMode,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCount = 2 }
                        )
                        PlayerCountPill(
                            count = 3,
                            title = "Trio",
                            subtitle = "3 Players",
                            isSelected = selectedCount == 3,
                            isSunlightMode = isSunlightMode,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCount = 3 }
                        )
                        PlayerCountPill(
                            count = 4,
                            title = "Table",
                            subtitle = "4 Players",
                            isSelected = selectedCount == 4,
                            isSunlightMode = isSunlightMode,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCount = 4 }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when (selectedCount) {
                            1 -> "🎮 1 Player Solo: Practice memorization against the clock (16 cards, 4x4)."
                            2 -> "⚔️ 2 Players 1v1: Diagonal surround battle (16 cards, 4x4)."
                            3 -> "⚡ 3 Players Clash: 3-way arena showdown (16 cards, 4x4)."
                            else -> "🔥 4 Players Tabletop: Full 4-corner arena with 2 extra rows (24 cards, 4x6)!"
                        },
                        fontSize = 11.sp,
                        color = textSecondary,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ══════════════════════════════════════════════════════════════════
            // STEP 2: EDITABLE PLAYER NAMES
            // ══════════════════════════════════════════════════════════════════
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. PLAYER NAMES (EDITABLE)",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = if (isSunlightMode) SunPlayerBlue else ArenaSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    for (i in 0 until selectedCount) {
                        val playerColor = playerColors[i % playerColors.size]
                        val defaultTag = "P${i + 1}"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Avatar Badge
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(playerColor.copy(alpha = if (isSunlightMode) 0.25f else 0.2f))
                                    .border(1.5.dp, playerColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = names.getOrNull(i)?.take(1)?.uppercase()?.ifEmpty { defaultTag } ?: defaultTag,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = playerColor
                                )
                            }

                            // Text Input Field
                            OutlinedTextField(
                                value = names.getOrElse(i) { "Player ${i + 1}" },
                                onValueChange = { newName ->
                                    if (newName.length <= 14) {
                                        if (i < names.size) {
                                            names[i] = newName
                                        }
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Player ${i + 1} Name",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = playerColor,
                                    unfocusedBorderColor = cardBorder,
                                    focusedTextColor = textPrimary,
                                    unfocusedTextColor = textPrimary,
                                    focusedLabelColor = playerColor,
                                    unfocusedLabelColor = textSecondary,
                                    cursorColor = playerColor
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ══════════════════════════════════════════════════════════════════
            // STEP 3: SUNLIGHT / OUTDOOR HIGH-CONTRAST MODE TOGGLE
            // ══════════════════════════════════════════════════════════════════
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSunlightMode) Color(0xFFFDE047).copy(alpha = 0.25f)
                                    else ArenaPrimary.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSunlightMode) Icons.Filled.WbSunny else Icons.Filled.Nightlight,
                                contentDescription = null,
                                tint = if (isSunlightMode) Color(0xFFEAB308) else ArenaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (isSunlightMode) "☀️ Outdoor Sunlight Mode" else "🌙 Cyberpunk Night Mode",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = textPrimary
                            )
                            Text(
                                text = if (isSunlightMode) "Anti-glare high-contrast palette for playing outdoors" else "Neon glow theme for standard/indoor play",
                                fontSize = 10.sp,
                                color = textSecondary,
                                lineHeight = 13.sp
                            )
                        }
                    }

                    Switch(
                        checked = isSunlightMode,
                        onCheckedChange = { isSunlightMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF0284C7),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = ArenaSurfaceLowest
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ══════════════════════════════════════════════════════════════════
            // STEP 4: START GAME ACTION BUTTON
            // ══════════════════════════════════════════════════════════════════
            val activeActionColor = if (isSunlightMode) SunPlayerBlue else ArenaSecondary
            val buttonTextColor = if (isSunlightMode) Color.White else ArenaSurfaceLowest

            Button(
                onClick = {
                    val finalNames = (0 until selectedCount).map { i ->
                        names.getOrElse(i) { "Player ${i + 1}" }.trim().ifEmpty { "Player ${i + 1}" }
                    }
                    onStartGame(selectedCount, finalNames, isSunlightMode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = activeActionColor,
                    contentColor = buttonTextColor
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ENTER ARENA MATCH",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PlayerCountPill(
    count: Int,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    isSunlightMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val activeBorder = if (isSunlightMode) SunPlayerBlue else ArenaSecondary
    val activeBg = if (isSunlightMode) SunPlayerBlue else ArenaSecondary
    val inactiveBg = if (isSunlightMode) SunSurfaceHigh else ArenaSurfaceContainer
    val activeText = if (isSunlightMode) Color.White else ArenaSurfaceLowest
    val inactiveText = if (isSunlightMode) SunOnSurface else Color.White

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) activeBg else inactiveBg)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) activeBorder else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "$count P",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = if (isSelected) activeText else inactiveText
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (isSelected) activeText.copy(alpha = 0.9f) else inactiveText.copy(alpha = 0.65f)
            )
        }
    }
}
