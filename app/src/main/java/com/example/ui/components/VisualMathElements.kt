package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MathProblem
import com.example.domain.model.VisualType
import com.example.ui.theme.JunglePrimary
import com.example.ui.theme.JungleSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VisualMathContainer(
    problem: MathProblem,
    onObjectTapped: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF1F8E9))
            .border(2.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (problem.visualType) {
            VisualType.OBJECT_COUNT -> {
                InteractiveObjectGrid(
                    count = problem.operand1,
                    emoji = problem.itemEmoji,
                    onItemClick = onObjectTapped
                )
            }
            VisualType.ADDITION_GROUPS -> {
                AdditionGroupsVisual(
                    group1Count = problem.operand1,
                    group2Count = problem.operand2,
                    emoji = problem.itemEmoji
                )
            }
            VisualType.SUBTRACTION_TAKEAWAY -> {
                SubtractionTakeawayVisual(
                    totalCount = problem.operand1,
                    takeawayCount = problem.operand2,
                    emoji = problem.itemEmoji
                )
            }
            VisualType.MULTIPLICATION_GRID -> {
                MultiplicationGridVisual(
                    rows = problem.operand1,
                    columns = problem.operand2,
                    emoji = problem.itemEmoji
                )
            }
            VisualType.DIVISION_SHARING -> {
                DivisionSharingVisual(
                    totalItems = problem.operand1,
                    groups = problem.operand2,
                    itemEmoji = problem.itemEmoji,
                    animalEmoji = problem.secondaryEmoji
                )
            }
            VisualType.FRACTION_SHAPE -> {
                FractionPizzaPieVisual(
                    coloredSlices = problem.fractionColoredSlices,
                    totalSlices = problem.fractionTotalSlices
                )
            }
            VisualType.COMPARISON_SCALE -> {
                CrocodileComparisonVisual(
                    leftNum = problem.operand1,
                    rightNum = problem.operand2
                )
            }
            VisualType.WORD_STORY -> {
                WordStoryVisual(
                    emoji = problem.itemEmoji,
                    num1 = problem.operand1,
                    num2 = problem.operand2
                )
            }
            else -> {
                Text(text = problem.itemEmoji, fontSize = 48.sp)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveObjectGrid(
    count: Int,
    emoji: String,
    onItemClick: () -> Unit
) {
    val tappedIndices = remember(count, emoji) { mutableStateListOf<Int>() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tap to count: ${tappedIndices.size} / $count",
            fontWeight = FontWeight.Bold,
            color = JunglePrimary,
            fontSize = 16.sp
        )

        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 1..count) {
                val isTapped = tappedIndices.contains(i)
                val scale by animateFloatAsState(
                    targetValue = if (isTapped) 1.15f else 1.0f,
                    animationSpec = spring(dampingRatio = 0.5f),
                    label = "item_scale"
                )

                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .scale(scale)
                        .size(54.dp)
                        .shadow(if (isTapped) 4.dp else 2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(if (isTapped) Color(0xFFFFF176) else Color.White)
                        .border(
                            2.dp,
                            if (isTapped) Color(0xFFFBC02D) else Color(0xFFE0E0E0),
                            CircleShape
                        )
                        .clickable {
                            if (!isTapped) {
                                tappedIndices.add(i)
                                onItemClick()
                            }
                        }
                        .testTag("count_item_$i"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 28.sp)
                    if (isTapped) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E7D32)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${tappedIndices.indexOf(i) + 1}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdditionGroupsVisual(
    group1Count: Int,
    group2Count: Int,
    emoji: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Group 1 Crate
        JungleCrate(
            count = group1Count,
            emoji = emoji,
            label = "$group1Count $emoji",
            bgColor = Color(0xFFFFF9C4)
        )

        Text(
            text = "+",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2E7D32)
        )

        // Group 2 Crate
        JungleCrate(
            count = group2Count,
            emoji = emoji,
            label = "$group2Count $emoji",
            bgColor = Color(0xFFFFE0B2)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JungleCrate(
    count: Int,
    emoji: String,
    label: String,
    bgColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, Color(0xFFBCAAA4), RoundedCornerShape(16.dp))
            .padding(10.dp)
    ) {
        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.width((minOf(count, 3) * 36 + 20).coerceAtLeast(60).dp)
        ) {
            repeat(minOf(count, 12)) {
                Text(text = emoji, fontSize = 24.sp, modifier = Modifier.padding(2.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubtractionTakeawayVisual(
    totalCount: Int,
    takeawayCount: Int,
    emoji: String
) {
    val remaining = totalCount - takeawayCount
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Started with $totalCount  👉  Take away $takeawayCount",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037)
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 1..totalCount) {
                val isTakeaway = i > remaining
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isTakeaway) Color(0xFFFFEBEE) else Color.White)
                        .border(
                            2.dp,
                            if (isTakeaway) Color(0xFFEF9A9A) else Color(0xFFA5D6A7),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 24.sp,
                        color = if (isTakeaway) Color.Gray.copy(alpha = 0.4f) else Color.Unspecified
                    )
                    if (isTakeaway) {
                        Text(
                            text = "❌",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        Text(
            text = "$remaining $emoji left!",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun MultiplicationGridVisual(
    rows: Int,
    columns: Int,
    emoji: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "$rows groups of $columns",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFFE65100)
        )

        Column(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFF3E0))
                .border(2.dp, Color(0xFFFFCC80), RoundedCornerShape(16.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (r in 1..rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Row $r:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8D6E63)
                    )
                    for (c in 1..columns) {
                        Text(text = emoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DivisionSharingVisual(
    totalItems: Int,
    groups: Int,
    itemEmoji: String,
    animalEmoji: String
) {
    val itemsEach = totalItems / groups
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Total $totalItems $itemEmoji shared among $groups friends:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00695C)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (g in 1..groups) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE0F2F1))
                        .border(2.dp, Color(0xFF80CBC4), RoundedCornerShape(14.dp))
                        .padding(8.dp)
                ) {
                    Text(text = animalEmoji, fontSize = 26.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(itemsEach) {
                            Text(text = itemEmoji, fontSize = 18.sp)
                        }
                    }
                    Text(
                        text = "$itemsEach $itemEmoji",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40)
                    )
                }
            }
        }
    }
}

@Composable
fun FractionPizzaPieVisual(
    coloredSlices: Int,
    totalSlices: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$coloredSlices of $totalSlices slices eaten 🍕",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD84315)
        )

        Canvas(
            modifier = Modifier
                .size(140.dp)
                .padding(8.dp)
        ) {
            val sweepAngle = 360f / totalSlices
            val crustColor = Color(0xFFD7CCC8)
            val sauceColor = Color(0xFFFFE0B2)
            val highlightedColor = Color(0xFFFF7043)
            val borderColor = Color(0xFF5D4037)

            // Draw pie slices
            for (i in 0 until totalSlices) {
                val startAngle = i * sweepAngle - 90f
                val sliceColor = if (i < coloredSlices) highlightedColor else sauceColor

                drawArc(
                    color = sliceColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset.Zero,
                    size = size
                )

                // Outline line for each slice
                val rad = Math.toRadians(startAngle.toDouble())
                val endX = center.x + (size.width / 2) * cos(rad).toFloat()
                val endY = center.y + (size.height / 2) * sin(rad).toFloat()
                drawLine(
                    color = borderColor,
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 3f
                )
            }

            // Outer crust border
            drawCircle(
                color = borderColor,
                radius = size.width / 2,
                center = center,
                style = Stroke(width = 4f)
            )
        }
    }
}

@Composable
fun CrocodileComparisonVisual(
    leftNum: Int,
    rightNum: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberBubble(num = leftNum, color = Color(0xFFE1F5FE), border = Color(0xFF0288D1))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🐊",
                fontSize = 44.sp
            )
            Text(
                text = "Chomper chooses:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
        }

        NumberBubble(num = rightNum, color = Color(0xFFFFF8E1), border = Color(0xFFFFA000))
    }
}

@Composable
fun NumberBubble(num: Int, color: Color, border: Color) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .shadow(3.dp, CircleShape)
            .clip(CircleShape)
            .background(color)
            .border(3.dp, border, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$num",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF263238)
        )
    }
}

@Composable
fun WordStoryVisual(
    emoji: String,
    num1: Int,
    num2: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "🌴", fontSize = 32.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$num1 $emoji  &  $num2 $emoji",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2E7D32)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "🐒", fontSize = 32.sp)
    }
}

@Composable
fun InteractiveKeypad(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("DEL", "0", "GO")
        )

        for (row in rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (key in row) {
                    when (key) {
                        "DEL" -> {
                            KeypadButton(
                                text = "",
                                icon = Icons.AutoMirrored.Filled.Backspace,
                                bgColor = Color(0xFFFFEBEE),
                                borderColor = Color(0xFFEF9A9A),
                                textColor = Color(0xFFC62828),
                                onClick = onDeleteClick,
                                testTag = "keypad_del"
                            )
                        }
                        "GO" -> {
                            KeypadButton(
                                text = "GO! 🚀",
                                bgColor = Color(0xFFE8F5E9),
                                borderColor = Color(0xFFA5D6A7),
                                textColor = Color(0xFF1B5E20),
                                onClick = onSubmitClick,
                                testTag = "keypad_go"
                            )
                        }
                        else -> {
                            KeypadButton(
                                text = key,
                                bgColor = Color.White,
                                borderColor = Color(0xFFFFD54F),
                                textColor = Color(0xFF1B5E20),
                                onClick = { onDigitClick(key) },
                                testTag = "keypad_$key"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 52.dp)
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = "Delete", tint = textColor)
        } else {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
        }
    }
}
