package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class ConfettiParticle(
    val initialX: Float,
    val initialY: Float,
    val speedY: Float,
    val speedX: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiRewardEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 45
) {
    val progress = remember { Animatable(0f) }

    val colors = listOf(
        Color(0xFFFFD54F),
        Color(0xFFFF7043),
        Color(0xFF4CAF50),
        Color(0xFF29B6F6),
        Color(0xFFAB47BC),
        Color(0xFFFF4081)
    )

    val particles = remember {
        List(particleCount) {
            val rand = Random.Default
            ConfettiParticle(
                initialX = rand.nextFloat(),
                initialY = rand.nextFloat() * -0.5f,
                speedY = rand.nextFloat() * 1.2f + 0.8f,
                speedX = (rand.nextFloat() - 0.5f) * 0.4f,
                color = colors.random(),
                size = rand.nextFloat() * 16f + 8f,
                rotationSpeed = (rand.nextFloat() - 0.5f) * 720f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2800, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val p = progress.value

        particles.forEach { particle ->
            val x = (particle.initialX + particle.speedX * p) * w
            val y = (particle.initialY + particle.speedY * p) * h

            if (y in 0f..h) {
                drawRect(
                    color = particle.color.copy(alpha = (1f - p * 0.3f).coerceIn(0f, 1f)),
                    topLeft = Offset(x, y),
                    size = Size(particle.size, particle.size * 0.6f)
                )
            }
        }
    }
}
