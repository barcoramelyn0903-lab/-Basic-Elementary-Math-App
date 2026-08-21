package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.ChildProfile
import com.example.domain.model.MathSubject
import com.example.ui.components.ParentGateDialog
import com.example.ui.screens.avatar.AvatarCustomizationScreen
import com.example.ui.screens.challenge.MathChallengeScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.map.JungleMapScreen
import com.example.ui.screens.parent.ParentDashboardScreen
import com.example.ui.screens.profile.ProfileSetupScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.rewards.TreasureRoomScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.welcome.WelcomeScreen
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.JungleMathViewModel

@Composable
fun MainScreen(
    viewModel: JungleMathViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val skills by viewModel.skillProgress.collectAsStateWithLifecycle()
    val levels by viewModel.worldLevels.collectAsStateWithLifecycle()
    val rewards by viewModel.rewards.collectAsStateWithLifecycle()
    val challengeSession by viewModel.challengeState.collectAsStateWithLifecycle()

    var showParentGate by remember { mutableStateOf(false) }

    // Intercept back button for smooth kid-friendly back navigation
    BackHandler(enabled = currentScreen != AppScreen.HOME && currentScreen != AppScreen.WELCOME) {
        when (currentScreen) {
            AppScreen.SETTINGS -> viewModel.navigateTo(AppScreen.PARENT_DASHBOARD)
            AppScreen.PARENT_DASHBOARD -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.CHALLENGE -> viewModel.navigateTo(AppScreen.MAP)
            else -> viewModel.navigateTo(AppScreen.HOME)
        }
    }

    val childProfile = profile ?: ChildProfile()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (currentScreen) {
            AppScreen.WELCOME -> {
                WelcomeScreen(
                    onStartAdventure = { viewModel.navigateTo(AppScreen.HOME) },
                    onSetupProfile = { viewModel.navigateTo(AppScreen.PROFILE_SETUP) },
                    modifier = screenModifier
                )
            }

            AppScreen.PROFILE_SETUP -> {
                ProfileSetupScreen(
                    initialName = childProfile.name,
                    initialAge = childProfile.age,
                    initialAvatar = childProfile.selectedAvatar,
                    onSaveProfile = { name, age, avatar ->
                        viewModel.updateProfile(name, age, avatar)
                    },
                    onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = screenModifier
                )
            }

            AppScreen.HOME -> {
                HomeScreen(
                    profile = childProfile,
                    onPlayTodayAdventure = {
                        // Play the first available unlocked uncompleted level, or quick practice
                        val nextLevel = levels.firstOrNull { it.isUnlocked && !it.isCompleted }
                            ?: levels.firstOrNull { it.isUnlocked }
                        if (nextLevel != null) {
                            viewModel.startChallenge(nextLevel)
                        } else {
                            viewModel.startQuickPractice(MathSubject.ADDITION)
                        }
                    },
                    onOpenMap = { viewModel.navigateTo(AppScreen.MAP) },
                    onOpenTreasureRoom = { viewModel.navigateTo(AppScreen.TREASURE_ROOM) },
                    onOpenAvatarCustomize = { viewModel.navigateTo(AppScreen.AVATAR_CUSTOMIZE) },
                    onOpenProgress = { viewModel.navigateTo(AppScreen.PROGRESS) },
                    onOpenParentGate = { showParentGate = true },
                    onSubjectQuickPlay = { subject ->
                        viewModel.startQuickPractice(subject)
                    },
                    onAudioClick = {
                        viewModel.audioHelper.speak(
                            "Welcome to Jungle Math, ${childProfile.name}! Tap Play to start your quest!",
                            childProfile.voiceNarrationEnabled,
                            childProfile.ttsSpeed
                        )
                    },
                    modifier = screenModifier
                )
            }

            AppScreen.MAP -> {
                JungleMapScreen(
                    profile = childProfile,
                    levels = levels,
                    onSelectLevel = { level ->
                        viewModel.startChallenge(level)
                    },
                    onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                    onAudioClick = {
                        viewModel.audioHelper.speak(
                            "Choose an island on the map to explore and earn stars!",
                            childProfile.voiceNarrationEnabled,
                            childProfile.ttsSpeed
                        )
                    },
                    modifier = screenModifier
                )
            }

            AppScreen.CHALLENGE -> {
                MathChallengeScreen(
                    profile = childProfile,
                    sessionState = challengeSession,
                    onAnswerSelected = { answer ->
                        viewModel.submitAnswer(answer)
                    },
                    onObjectTapped = {
                        viewModel.onObjectTappedToCount()
                    },
                    onRequestHint = {
                        viewModel.requestHint()
                    },
                    onHearAgain = {
                        viewModel.hearQuestionAgain()
                    },
                    onNextQuestion = {
                        viewModel.nextQuestion()
                    },
                    onBackClick = { viewModel.navigateTo(AppScreen.MAP) },
                    modifier = screenModifier
                )
            }

            AppScreen.TREASURE_ROOM -> {
                TreasureRoomScreen(
                    profile = childProfile,
                    rewards = rewards,
                    onEquipReward = { reward ->
                        viewModel.equipReward(reward)
                    },
                    onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                    onAudioClick = {
                        viewModel.audioHelper.speak(
                            "Welcome to the Treasure Room! Use your shiny gems to unlock cool hats, cloaks, and companion pets!",
                            childProfile.voiceNarrationEnabled,
                            childProfile.ttsSpeed
                        )
                    },
                    modifier = screenModifier
                )
            }

            AppScreen.AVATAR_CUSTOMIZE -> {
                AvatarCustomizationScreen(
                    profile = childProfile,
                    rewards = rewards,
                    onEquipItem = { reward ->
                        viewModel.equipReward(reward)
                    },
                    onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                    onAudioClick = {
                        viewModel.audioHelper.speak(
                            "Dress up your jungle explorer! Pick your favorite hat, outfit, and pet buddy!",
                            childProfile.voiceNarrationEnabled,
                            childProfile.ttsSpeed
                        )
                    },
                    modifier = screenModifier
                )
            }

            AppScreen.PROGRESS -> {
                ProgressScreen(
                    profile = childProfile,
                    skills = skills,
                    levels = levels,
                    onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                    onAudioClick = {
                        viewModel.audioHelper.speak(
                            "Check out all your math badges and power levels! You are doing great!",
                            childProfile.voiceNarrationEnabled,
                            childProfile.ttsSpeed
                        )
                    },
                    modifier = screenModifier
                )
            }

            AppScreen.PARENT_DASHBOARD -> {
                ParentDashboardScreen(
                    profile = childProfile,
                    skills = skills,
                    onOpenSettings = { viewModel.navigateTo(AppScreen.SETTINGS) },
                    onBackToKidHome = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = screenModifier
                )
            }

            AppScreen.SETTINGS -> {
                SettingsScreen(
                    profile = childProfile,
                    onSaveSettings = { sound, voice, speed, pin ->
                        viewModel.updateSettings(sound, voice, speed, pin)
                    },
                    onResetProgress = {
                        viewModel.resetProgress()
                    },
                    onBackClick = { viewModel.navigateTo(AppScreen.PARENT_DASHBOARD) },
                    modifier = screenModifier
                )
            }
        }
    }

    // Parent Gate Security Math Dialog
    if (showParentGate) {
        ParentGateDialog(
            onDismiss = { showParentGate = false },
            onSuccess = {
                showParentGate = false
                viewModel.navigateTo(AppScreen.PARENT_DASHBOARD)
            }
        )
    }
}
