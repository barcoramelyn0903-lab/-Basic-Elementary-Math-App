package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GemTeal
import com.example.ui.theme.JunglePrimary
import com.example.ui.theme.StarGold

@Composable
fun JungleTopBar(
    title: String = "",
    stars: Int = 0,
    gems: Int = 0,
    streak: Int = 0,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    onParentGateClick: (() -> Unit)? = null,
    onAudioClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Back button or Mascot icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showBackButton) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF9C4))
                            .border(2.dp, Color(0xFFFFD54F), CircleShape)
                            .clickable { onBackClick() }
                            .testTag("back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Color(0xFF1B5E20)
                        )
                    }
                }

                if (title.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            // Center / Right: Currency Pills
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Streak Pill
                if (streak > 0) {
                    CurrencyPill(
                        emoji = "🔥",
                        value = "$streak",
                        bgColor = Color(0xFFFFE0B2),
                        borderColor = Color(0xFFFFB74D),
                        textColor = Color(0xFFE65100),
                        testTag = "streak_pill"
                    )
                }

                // Stars Pill
                CurrencyPill(
                    emoji = "⭐",
                    value = "$stars",
                    bgColor = Color(0xFFFFF9C4),
                    borderColor = StarGold,
                    textColor = Color(0xFFE65100),
                    testTag = "stars_pill"
                )

                // Gems Pill
                CurrencyPill(
                    emoji = "💎",
                    value = "$gems",
                    bgColor = Color(0xFFE0F2F1),
                    borderColor = GemTeal,
                    textColor = Color(0xFF00695C),
                    testTag = "gems_pill"
                )

                // Audio / Voice button
                if (onAudioClick != null) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9))
                            .border(2.dp, JunglePrimary, CircleShape)
                            .clickable { onAudioClick() }
                            .testTag("hear_again_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Hear audio instructions",
                            tint = JunglePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Parent Gate Lock
                if (onParentGateClick != null) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFEDE7F6))
                            .border(2.dp, Color(0xFF7E57C2), CircleShape)
                            .clickable { onParentGateClick() }
                            .testTag("parent_gate_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Parent Dashboard",
                            tint = Color(0xFF5E35B1),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyPill(
    emoji: String,
    value: String,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    testTag: String
) {
    Row(
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = emoji, fontSize = 15.sp)
        Text(
            text = value,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            fontSize = 14.sp
        )
    }
}
