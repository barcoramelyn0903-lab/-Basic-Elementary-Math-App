package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.KidAudioHelper
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.RewardItem
import com.example.data.local.entity.SkillProgress
import com.example.data.local.entity.WorldLevel
import com.example.data.repository.MathRepository
import com.example.domain.model.MathChallengeGenerator
import com.example.domain.model.MathProblem
import com.example.domain.model.MathSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    WELCOME,
    PROFILE_SETUP,
    HOME,
    MAP,
    CHALLENGE,
    TREASURE_ROOM,
    AVATAR_CUSTOMIZE,
    PROGRESS,
    PARENT_DASHBOARD,
    SETTINGS
}

data class ChallengeSessionState(
    val currentLevel: WorldLevel? = null,
    val subject: MathSubject = MathSubject.ADDITION,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val currentProblem: MathProblem? = null,
    val correctInSession: Int = 0,
    val hintsUsedInCurrentQuestion: Int = 0,
    val showingHintStep: Int = 0, // 0=none, 1=step 1, 2=step 2
    val selectedAnswer: String? = null,
    val isAnswerChecked: Boolean = false,
    val isCorrect: Boolean = false,
    val feedbackMessage: String = "",
    val isSessionComplete: Boolean = false,
    val starsEarned: Int = 0,
    val gemsEarned: Int = 0,
    val tappedCount: Int = 0 // For interactive tap-to-count objects
)

class JungleMathViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = MathRepository(database)
    val audioHelper = KidAudioHelper(application)

    val profile: StateFlow<ChildProfile?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val skillProgress: StateFlow<List<SkillProgress>> = repository.allSkills
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val worldLevels: StateFlow<List<WorldLevel>> = repository.allLevels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rewards: StateFlow<List<RewardItem>> = repository.allRewards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _challengeState = MutableStateFlow(ChallengeSessionState())
    val challengeState: StateFlow<ChallengeSessionState> = _challengeState.asStateFlow()

    init {
        // Initial setup check
        viewModelScope.launch {
            val p = repository.getProfileSync()
            if (p.totalStars == 0 && p.totalTimeMinutes == 0) {
                _currentScreen.value = AppScreen.WELCOME
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        audioHelper.playSoftClick(profile.value?.soundEffectsEnabled ?: true)
        _currentScreen.value = screen
    }

    fun startChallenge(level: WorldLevel) {
        val subject = try {
            MathSubject.valueOf(level.subject)
        } catch (e: Exception) {
            MathSubject.ADDITION
        }

        val firstProblem = MathChallengeGenerator.generateProblem(
            subject = subject,
            difficulty = getDifficultyForAge(profile.value?.age ?: 7),
            questionIndex = 0
        )

        _challengeState.value = ChallengeSessionState(
            currentLevel = level,
            subject = subject,
            currentQuestionIndex = 0,
            totalQuestions = level.targetQuestions,
            currentProblem = firstProblem,
            correctInSession = 0,
            hintsUsedInCurrentQuestion = 0,
            showingHintStep = 0,
            selectedAnswer = null,
            isAnswerChecked = false,
            isCorrect = false,
            feedbackMessage = "",
            isSessionComplete = false,
            starsEarned = 0,
            gemsEarned = 0,
            tappedCount = 0
        )

        _currentScreen.value = AppScreen.CHALLENGE

        // Speak question
        firstProblem.let { prob ->
            speakVoice(prob.voiceNarrationText)
        }
    }

    fun startQuickPractice(subject: MathSubject) {
        val firstProblem = MathChallengeGenerator.generateProblem(
            subject = subject,
            difficulty = getDifficultyForAge(profile.value?.age ?: 7),
            questionIndex = 0
        )

        _challengeState.value = ChallengeSessionState(
            currentLevel = null,
            subject = subject,
            currentQuestionIndex = 0,
            totalQuestions = 5,
            currentProblem = firstProblem,
            correctInSession = 0,
            hintsUsedInCurrentQuestion = 0,
            showingHintStep = 0,
            selectedAnswer = null,
            isAnswerChecked = false,
            isCorrect = false,
            feedbackMessage = "",
            isSessionComplete = false,
            starsEarned = 0,
            gemsEarned = 0,
            tappedCount = 0
        )

        _currentScreen.value = AppScreen.CHALLENGE
        speakVoice(firstProblem.voiceNarrationText)
    }

    fun onObjectTappedToCount() {
        val currentTapped = _challengeState.value.tappedCount + 1
        _challengeState.value = _challengeState.value.copy(tappedCount = currentTapped)
        audioHelper.playSoftClick(profile.value?.soundEffectsEnabled ?: true)
        speakVoice("$currentTapped")
    }

    fun submitAnswer(answer: String) {
        val session = _challengeState.value
        val problem = session.currentProblem ?: return
        if (session.isAnswerChecked) return

        val isCorrect = when (problem.subject) {
            MathSubject.FRACTIONS -> answer.trim() == problem.correctAnswerString.trim()
            MathSubject.COMPARISON -> answer.trim() == problem.correctAnswerString.trim()
            else -> answer.trim() == problem.correctAnswer.toString().trim()
        }

        val newCorrectCount = if (isCorrect) session.correctInSession + 1 else session.correctInSession
        val feedback = if (isCorrect) {
            problem.encouragement
        } else {
            "Almost! Let's count and try again together. 🌟"
        }

        _challengeState.value = session.copy(
            selectedAnswer = answer,
            isAnswerChecked = true,
            isCorrect = isCorrect,
            feedbackMessage = feedback,
            correctInSession = newCorrectCount
        )

        val soundOn = profile.value?.soundEffectsEnabled ?: true
        if (isCorrect) {
            audioHelper.playCorrectChime(soundOn)
        } else {
            audioHelper.playGentleTryAgain(soundOn)
        }

        speakVoice(feedback)

        // Save progress to database
        viewModelScope.launch {
            repository.recordQuestionResult(
                skillId = problem.subject.name,
                isCorrect = isCorrect,
                usedHint = session.hintsUsedInCurrentQuestion > 0
            )
        }
    }

    fun requestHint() {
        val session = _challengeState.value
        val problem = session.currentProblem ?: return
        val currentHintStep = session.showingHintStep
        val nextStep = if (currentHintStep == 0) 1 else 2

        _challengeState.value = session.copy(
            showingHintStep = nextStep,
            hintsUsedInCurrentQuestion = session.hintsUsedInCurrentQuestion + 1
        )

        val hintText = if (nextStep == 1) problem.hintStep1 else problem.hintStep2
        speakVoice(hintText)
    }

    fun hearQuestionAgain() {
        val prob = _challengeState.value.currentProblem ?: return
        val textToHear = if (_challengeState.value.isAnswerChecked) {
            _challengeState.value.feedbackMessage
        } else {
            prob.voiceNarrationText
        }
        speakVoice(textToHear)
    }

    fun nextQuestion() {
        val session = _challengeState.value
        val nextIndex = session.currentQuestionIndex + 1

        if (nextIndex >= session.totalQuestions) {
            // Level complete
            val correct = session.correctInSession
            val total = session.totalQuestions
            val stars = when {
                correct == total -> 3
                correct >= (total * 0.7) -> 2
                correct > 0 -> 1
                else -> 1
            }
            val gems = stars * 10 + (correct * 2)

            _challengeState.value = session.copy(
                isSessionComplete = true,
                starsEarned = stars,
                gemsEarned = gems
            )

            audioHelper.playStarFanfare(profile.value?.soundEffectsEnabled ?: true)
            speakVoice("Hooray! Adventure complete! You earned $stars golden stars and $gems shiny gems!")

            // Persist level completion if applicable
            session.currentLevel?.let { level ->
                viewModelScope.launch {
                    repository.completeLevel(level.levelId, stars, gems)
                }
            } ?: run {
                viewModelScope.launch {
                    val p = repository.getProfileSync()
                    repository.saveProfile(p.copy(
                        totalStars = p.totalStars + stars,
                        totalGems = p.totalGems + gems
                    ))
                }
            }
        } else {
            // Next question
            val nextProblem = MathChallengeGenerator.generateProblem(
                subject = session.subject,
                difficulty = getDifficultyForAge(profile.value?.age ?: 7),
                questionIndex = nextIndex
            )

            _challengeState.value = session.copy(
                currentQuestionIndex = nextIndex,
                currentProblem = nextProblem,
                showingHintStep = 0,
                hintsUsedInCurrentQuestion = 0,
                selectedAnswer = null,
                isAnswerChecked = false,
                isCorrect = false,
                feedbackMessage = "",
                tappedCount = 0
            )

            speakVoice(nextProblem.voiceNarrationText)
        }
    }

    fun updateProfile(name: String, age: Int, avatar: String) {
        viewModelScope.launch {
            val p = repository.getProfileSync()
            repository.saveProfile(
                p.copy(
                    name = name,
                    age = age,
                    selectedAvatar = avatar
                )
            )
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun equipReward(reward: RewardItem) {
        viewModelScope.launch {
            repository.unlockAndEquipReward(reward)
            audioHelper.playStarFanfare(profile.value?.soundEffectsEnabled ?: true)
        }
    }

    fun updateSettings(soundEnabled: Boolean, voiceEnabled: Boolean, ttsSpeed: Float, pin: String) {
        viewModelScope.launch {
            repository.updateSettings(soundEnabled, voiceEnabled, ttsSpeed, pin)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            _currentScreen.value = AppScreen.HOME
        }
    }

    private fun speakVoice(text: String) {
        val p = profile.value
        val enabled = p?.voiceNarrationEnabled ?: true
        val speed = p?.ttsSpeed ?: 0.88f
        audioHelper.speak(text, enabled, speed)
    }

    private fun getDifficultyForAge(age: Int): Int {
        return when {
            age <= 6 -> 1
            age <= 8 -> 2
            else -> 3
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioHelper.release()
    }
}
