package com.example.ui.screens.settings

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChildProfile
import com.example.ui.components.KidButton
import com.example.ui.theme.JunglePrimary

@Composable
fun SettingsScreen(
    profile: ChildProfile,
    onSaveSettings: (sound: Boolean, voice: Boolean, speed: Float, pin: String) -> Unit,
    onResetProgress: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var soundEffects by remember { mutableStateOf(profile.soundEffectsEnabled) }
    var voiceNarration by remember { mutableStateOf(profile.voiceNarrationEnabled) }
    var speechSpeed by remember { mutableFloatStateOf(profile.ttsSpeed) }
    var parentPin by remember { mutableStateOf(profile.parentPin) }
    var showResetDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.5.dp, Color(0xFFA5D6A7), CircleShape)
                        .clickable { onBackClick() }
                        .testTag("settings_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1B5E20)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "App Settings & Controls",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Audio & Voice Section
                SettingsCard(title = "🔊 Audio & Voice Settings") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Game Sound Effects", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1B5E20))
                            Text(text = "Plays cheerful chimes and fanfare", fontSize = 12.sp, color = Color(0xFF558B2F))
                        }
                        Switch(
                            checked = soundEffects,
                            onCheckedChange = {
                                soundEffects = it
                                onSaveSettings(it, voiceNarration, speechSpeed, parentPin)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2E7D32), checkedTrackColor = Color(0xFFA5D6A7)),
                            modifier = Modifier.testTag("switch_sound")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Voice-over Instructions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1B5E20))
                            Text(text = "Speaks math questions aloud for kids", fontSize = 12.sp, color = Color(0xFF558B2F))
                        }
                        Switch(
                            checked = voiceNarration,
                            onCheckedChange = {
                                voiceNarration = it
                                onSaveSettings(soundEffects, it, speechSpeed, parentPin)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2E7D32), checkedTrackColor = Color(0xFFA5D6A7)),
                            modifier = Modifier.testTag("switch_voice")
                        )
                    }

                    if (voiceNarration) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Voice Narration Speed: ${(speechSpeed * 100).toInt()}%",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color(0xFF1B5E20)
                        )
                        Slider(
                            value = speechSpeed,
                            onValueChange = {
                                speechSpeed = it
                                onSaveSettings(soundEffects, voiceNarration, it, parentPin)
                            },
                            valueRange = 0.6f..1.2f,
                            steps = 5,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFF2E7D32), activeTrackColor = Color(0xFF2E7D32)),
                            modifier = Modifier.testTag("slider_tts_speed")
                        )
                    }
                }

                // Child Safety & Privacy Policy Card
                SettingsCard(title = "🛡️ Privacy & Child Safety (COPPA)") {
                    Text(
                        text = "• 100% Ad-Free Experience\n" +
                               "• No personal data collection or transmission\n" +
                               "• No chat, multiplayer, or public social interaction\n" +
                               "• All math learning progress is stored locally on-device\n" +
                               "• Fully compliant with COPPA and global child privacy regulations",
                        fontSize = 12.sp,
                        color = Color(0xFF33691E),
                        lineHeight = 18.sp
                    )
                }

                // Reset Progress Card
                SettingsCard(title = "⚠️ Reset Learning Data") {
                    Text(
                        text = "Reset all earned stars, unlocked rewards, and skill progress back to starting state.",
                        fontSize = 12.sp,
                        color = Color(0xFF558B2F)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    KidButton(
                        text = "Reset All Progress",
                        onClick = { showResetDialog = true },
                        gradientColors = listOf(Color(0xFFFF7043), Color(0xFFD84315)),
                        textColor = Color.White,
                        fontSize = 14,
                        testTag = "reset_progress_button"
                    )
                }
            }
        }

        // Reset Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text(text = "Reset All Learning Data?", fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20)) },
                text = { Text("Are you sure you want to reset all stars, gems, and math skill logs?", color = Color(0xFF33691E)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResetDialog = false
                            onResetProgress()
                        }
                    ) {
                        Text("Yes, Reset", color = Color(0xFFD84315), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = Color(0xFF2E7D32))
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}
