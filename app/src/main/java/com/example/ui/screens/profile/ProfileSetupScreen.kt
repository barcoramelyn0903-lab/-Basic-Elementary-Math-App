package com.example.ui.screens.profile

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.theme.JunglePrimary
import com.example.ui.theme.StarGold

data class AvatarOption(val id: String, val name: String, val emoji: String, val desc: String)

@Composable
fun ProfileSetupScreen(
    initialName: String = "Leo",
    initialAge: Int = 7,
    initialAvatar: String = "monkey_pip",
    onSaveProfile: (name: String, age: Int, avatar: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(initialName) }
    var age by remember { mutableIntStateOf(initialAge) }
    var selectedAvatar by remember { mutableStateOf(initialAvatar) }

    val avatars = listOf(
        AvatarOption("monkey_pip", "Pip the Monkey", "🐒", "Loves banana math!"),
        AvatarOption("lion_leo", "Leo the Lion", "🦁", "Brave number king!"),
        AvatarOption("toucan_tiki", "Tiki the Toucan", "🦜", "Fraction flyer!"),
        AvatarOption("elephant_ella", "Ella Elephant", "🐘", "Super memory!"),
        AvatarOption("tiger_toby", "Toby the Tiger", "🐅", "Speed multiplier!")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF1F8E9),
                        Color(0xFFE8F5E9),
                        Color(0xFFDCEDC8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            JungleTopBar(
                title = "Explorer Profile",
                showBackButton = true,
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Name Input Card
                SectionCard(title = "1. What is your Explorer Name?") {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { if (it.length <= 15) name = it },
                        placeholder = { Text("e.g. Leo") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JunglePrimary,
                            unfocusedBorderColor = Color(0xFFA5D6A7),
                            focusedContainerColor = Color(0xFFF1F8E9),
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("child_name_input")
                    )
                }

                // Age Selector Card
                SectionCard(title = "2. How old are you?") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(6, 7, 8, 9).forEach { a ->
                            val isSelected = age == a
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .shadow(if (isSelected) 4.dp else 1.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color(0xFFFFF9C4) else Color.White)
                                    .border(
                                        2.5.dp,
                                        if (isSelected) Color(0xFFFFD54F) else Color(0xFFA5D6A7),
                                        CircleShape
                                    )
                                    .clickable { age = a }
                                    .testTag("age_button_$a"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$a",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) Color(0xFF1B5E20) else Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = "yrs",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFE65100) else Color(0xFF558B2F)
                                    )
                                }
                            }
                        }
                    }
                }

                // Avatar Chooser Card
                SectionCard(title = "3. Choose your Animal Guide!") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        avatars.forEach { avatar ->
                            val isSelected = selectedAvatar == avatar.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(18.dp))
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (isSelected) Color(0xFFFFF9C4) else Color.White)
                                    .border(
                                        2.5.dp,
                                        if (isSelected) Color(0xFFFFD54F) else Color(0xFFA5D6A7),
                                        RoundedCornerShape(18.dp)
                                    )
                                    .clickable { selectedAvatar = avatar.id }
                                    .padding(12.dp)
                                    .testTag("avatar_${avatar.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color(0xFFFFF9C4) else Color(0xFFE8F5E9))
                                        .border(1.5.dp, if (isSelected) Color(0xFFFFD54F) else Color(0xFFA5D6A7), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = avatar.emoji, fontSize = 30.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = avatar.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = avatar.desc,
                                        fontSize = 13.sp,
                                        color = Color(0xFF558B2F)
                                    )
                                }

                                if (isSelected) {
                                    Text(text = "✅", fontSize = 22.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Save Button
            KidButton(
                text = "SAVE & START EXPLORING! 🌴",
                onClick = {
                    val finalName = if (name.isBlank()) "Explorer" else name.trim()
                    onSaveProfile(finalName, age, selectedAvatar)
                },
                gradientColors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
                textColor = Color.White,
                fontSize = 19,
                modifier = Modifier.fillMaxWidth(),
                testTag = "save_profile_button"
            )
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B5E20)
            )
            content()
        }
    }
}
