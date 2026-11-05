package com.example.memoryarena.ui.arena

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memoryarena.model.Player
import com.example.memoryarena.ui.theme.ArenaSurfaceContainer
import com.example.memoryarena.ui.theme.ArenaSurfaceHigh
import com.example.memoryarena.ui.theme.ArenaSurfaceLowest
import com.example.memoryarena.ui.theme.SunOnSurface
import com.example.memoryarena.ui.theme.SunOnSurfaceVariant
import com.example.memoryarena.ui.theme.SunSurfaceContainer
import com.example.memoryarena.ui.theme.SunSurfaceHigh

enum class CornerPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}

@Composable
fun PlayerCornerPod(
    player: Player,
    isActive: Boolean,
    turnProgress: Float,
    isLeader: Boolean,
    position: CornerPosition,
    isSunlightMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Pulse animation for active turn
    val infiniteTransition = rememberInfiniteTransition(label = "podPulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "podPulseGlow"
    )

    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.04f else 0.98f,
        animationSpec = tween(300),
        label = "podScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else if (isSunlightMode) 0.85f else 0.72f,
        animationSpec = tween(300),
        label = "podAlpha"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isActive) player.auraColor else if (isSunlightMode) Color.Black.copy(alpha = 0.15f) else player.auraColor.copy(alpha = 0.25f),
        label = "podBorder"
    )

    val isRightAligned = (position == CornerPosition.TOP_RIGHT || position == CornerPosition.BOTTOM_RIGHT)

    val containerBg = when {
        isSunlightMode && isActive -> SunSurfaceHigh
        isSunlightMode -> SunSurfaceContainer
        isActive -> ArenaSurfaceHigh
        else -> ArenaSurfaceContainer
    }

    val isTopPosition = (position == CornerPosition.TOP_LEFT || position == CornerPosition.TOP_RIGHT)

    Box(
        modifier = modifier
            .graphicsLayer {
                if (isTopPosition) {
                    rotationZ = 180f
                }
            }
            .scale(scale)
            .alpha(alpha)
            .shadow(
                elevation = if (isActive) 12.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isActive) player.auraColor.copy(alpha = pulseGlow) else Color.Transparent,
                spotColor = if (isActive) player.auraColor.copy(alpha = pulseGlow) else Color.Transparent
            )
            .clip(RoundedCornerShape(16.dp))
            .background(containerBg)
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isRightAligned) {
                PlayerInfoBlock(
                    player = player,
                    isActive = isActive,
                    alignEnd = true,
                    isSunlightMode = isSunlightMode
                )
                PlayerAvatarWithTimer(
                    player = player,
                    isActive = isActive,
                    turnProgress = turnProgress,
                    isLeader = isLeader,
                    isSunlightMode = isSunlightMode
                )
            } else {
                PlayerAvatarWithTimer(
                    player = player,
                    isActive = isActive,
                    turnProgress = turnProgress,
                    isLeader = isLeader,
                    isSunlightMode = isSunlightMode
                )
                PlayerInfoBlock(
                    player = player,
                    isActive = isActive,
                    alignEnd = false,
                    isSunlightMode = isSunlightMode
                )
            }
        }
    }
}

@Composable
private fun PlayerAvatarWithTimer(
    player: Player,
    isActive: Boolean,
    turnProgress: Float,
    isLeader: Boolean,
    isSunlightMode: Boolean
) {
    Box(
        modifier = Modifier.size(38.dp),
        contentAlignment = Alignment.Center
    ) {
        // Countdown timer ring when active
        if (isActive) {
            CircularProgressIndicator(
                progress = { turnProgress },
                modifier = Modifier.fillMaxSize(),
                color = player.auraColor,
                trackColor = player.auraColor.copy(alpha = if (isSunlightMode) 0.35f else 0.2f),
                strokeWidth = 2.5.dp
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        1.dp,
                        if (isSunlightMode) player.auraColor.copy(alpha = 0.5f) else player.auraColor.copy(alpha = 0.35f),
                        CircleShape
                    )
            )
        }

        // Avatar Core
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(player.auraColor.copy(alpha = if (isSunlightMode) 0.25f else 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.initial,
                color = player.auraColor,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }

        // Crown badge for leader
        if (isLeader && player.score > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(13.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB95F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = "Leader",
                    tint = ArenaSurfaceLowest,
                    modifier = Modifier.size(9.dp)
                )
            }
        }
    }
}

@Composable
private fun PlayerInfoBlock(
    player: Player,
    isActive: Boolean,
    alignEnd: Boolean,
    isSunlightMode: Boolean
) {
    val primaryTextColor = if (isSunlightMode) SunOnSurface else Color(0xFFE4E0F5)
    val secondaryTextColor = if (isSunlightMode) SunOnSurfaceVariant else Color(0xFFCBC3D7)

    Column(
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            if (alignEnd && isActive) {
                TurnBadge(player.auraColor)
            }

            Text(
                text = player.name,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = primaryTextColor
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(player.auraColor.copy(alpha = if (isSunlightMode) 0.25f else 0.18f))
                    .padding(horizontal = 3.dp, vertical = 0.5.dp)
            ) {
                Text(
                    text = player.tag,
                    color = player.auraColor,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }

            if (!alignEnd && isActive) {
                TurnBadge(player.auraColor)
            }
        }

        Text(
            text = "${player.pairsFound} Pairs • ${player.score} pts",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isActive) player.auraColor else secondaryTextColor
        )
    }
}

@Composable
private fun TurnBadge(auraColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(auraColor)
            .padding(horizontal = 4.dp, vertical = 1.dp)
    ) {
        Text(
            text = "TURN",
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black
        )
    }
}
