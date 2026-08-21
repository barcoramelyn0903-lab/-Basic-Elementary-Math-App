package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class KidAudioHelper(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "ToneGenerator init error", e)
        }

        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true
                    tts?.setPitch(1.15f) // cheerful slightly higher pitch for kids
                    tts?.setSpeechRate(0.88f) // slightly slower, clear cadence for kids ages 6-9
                }
            }
        }
    }

    fun speak(text: String, enabled: Boolean = true, speed: Float = 0.88f) {
        if (!enabled || !isTtsReady) return
        try {
            tts?.setSpeechRate(speed)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "kid_math_speech_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "TTS speak failed", e)
        }
    }

    fun stopSpeaking() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "TTS stop failed", e)
        }
    }

    fun playCorrectChime(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 200)
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "Tone failed", e)
        }
    }

    fun playStarFanfare(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_DTMF_D, 350)
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "Tone failed", e)
        }
    }

    fun playSoftClick(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 70)
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "Tone failed", e)
        }
    }

    fun playGentleTryAgain(soundEnabled: Boolean = true) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 150)
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "Tone failed", e)
        }
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
            toneGenerator?.release()
        } catch (e: Exception) {
            Log.e("KidAudioHelper", "Release error", e)
        }
    }
}
