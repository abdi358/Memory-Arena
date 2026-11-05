package com.example.memoryarena.ui.arena

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.memoryarena.model.MemoryCard
import com.example.memoryarena.ui.theme.ArenaPrimary
import com.example.memoryarena.ui.theme.ArenaSecondary
import com.example.memoryarena.ui.theme.ArenaSurfaceContainer
import com.example.memoryarena.ui.theme.ArenaSurfaceHigh
import com.example.memoryarena.ui.theme.ArenaSurfaceLowest
import com.example.memoryarena.ui.theme.SunPlayerBlue

@Composable
fun MemoryCardView(
    card: MemoryCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSunlightMode: Boolean = false,
    iconSize: Dp = 56.dp
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFaceUp || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        label = "cardFlip"
    )

    val scale by animateFloatAsState(
        targetValue = if (card.isFaceUp && !card.isMatched) 1.05f else 1f,
        animationSpec = tween(200),
        label = "cardScale"
    )

    val activeAura = card.matchedByColor ?: if (isSunlightMode) SunPlayerBlue else ArenaSecondary

    val cardBorderColor by animateColorAsState(
        targetValue = when {
            card.isMatched -> activeAura.copy(alpha = if (isSunlightMode) 0.9f else 0.6f)
            card.isFaceUp -> activeAura
            isSunlightMode -> Color(0xFF0F172A).copy(alpha = 0.22f)
            else -> Color.White.copy(alpha = 0.08f)
        },
        label = "cardBorderColor"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .shadow(
                elevation = if (card.isFaceUp) 8.dp else if (isSunlightMode) 4.dp else 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = if (card.isFaceUp) activeAura.copy(alpha = 0.4f) else Color.Black,
                spotColor = if (card.isFaceUp) activeAura.copy(alpha = 0.4f) else Color.Black
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = activeAura)
            ) {
                if (!card.isFaceUp && !card.isMatched) {
                    onClick()
                }
            }
            .border(
                width = if (card.isFaceUp || card.isMatched) 2.dp else 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        if (rotation <= 90f) {
            // Card Back (Dormant State)
            CardBack(isSunlightMode = isSunlightMode)
        } else {
            // Card Front (Flipped State - counter-rotate to avoid mirror)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
            ) {
                CardFront(card = card, isSunlightMode = isSunlightMode, iconSize = iconSize)
            }
        }
    }
}

@Composable
private fun CardBack(isSunlightMode: Boolean) {
    if (isSunlightMode) {
        // High-contrast deep navy back with doubled crisp cyan/white emblem for sunlight visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E293B)), // Slate-800
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.5.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    } else {
        // Neon Cyberpunk Dark Theme Back (Doubled Center Emblem)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ArenaSurfaceHigh,
                            ArenaSurfaceContainer
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.2.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = null,
                    tint = ArenaPrimary.copy(alpha = 0.55f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun CardFront(
    card: MemoryCard,
    isSunlightMode: Boolean,
    iconSize: Dp = 56.dp
) {
    val fallbackAura = if (isSunlightMode) SunPlayerBlue else ArenaSecondary
    val auraColor = card.matchedByColor ?: fallbackAura

    if (isSunlightMode) {
        // Solid white front with saturated colored emblem and crisp border for outdoor sunlight
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (card.isMatched) auraColor.copy(alpha = 0.12f) else Color.White
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = card.symbol.icon,
                contentDescription = card.symbol.label,
                tint = auraColor,
                modifier = Modifier.size(iconSize)
            )

            // Matched checkmark badge in corner
            if (card.isMatched) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(auraColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Matched",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    } else {
        // Night Neon Theme Front
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            auraColor.copy(alpha = if (card.isMatched) 0.18f else 0.28f),
                            ArenaSurfaceHigh
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = card.symbol.icon,
                contentDescription = card.symbol.label,
                tint = auraColor,
                modifier = Modifier.size(iconSize)
            )

            // Matched checkmark badge in corner
            if (card.isMatched) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(auraColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Matched",
                        tint = ArenaSurfaceLowest,
                        modifier = Modifier.size(9.dp)
                    )
                }
            }
        }
    }
}
