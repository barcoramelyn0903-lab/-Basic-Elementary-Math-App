package com.example.domain.model

enum class MathSubject(val displayName: String, val iconEmoji: String) {
    COUNTING("Counting & Objects", "🎯"),
    ADDITION("Jungle Addition", "➕"),
    SUBTRACTION("Coconut Subtraction", "➖"),
    MULTIPLICATION("Animal Groups", "✖️"),
    DIVISION("Fair Sharing", "➗"),
    FRACTIONS("Tropical Fractions", "🍕"),
    COMPARISON("Croc Comparison", "🐊"),
    WORD_PROBLEMS("Story Puzzles", "📜")
}

enum class VisualType {
    OBJECT_COUNT,
    ADDITION_GROUPS,
    SUBTRACTION_TAKEAWAY,
    MULTIPLICATION_GRID,
    DIVISION_SHARING,
    FRACTION_SHAPE,
    COMPARISON_SCALE,
    SEQUENCE_STONES,
    WORD_STORY
}

enum class InteractionType {
    MULTIPLE_CHOICE,
    TAP_TO_COUNT,
    DRAG_DROP_FEED,
    NUMBER_KEYPAD
}

data class MathProblem(
    val id: String,
    val subject: MathSubject,
    val visualType: VisualType,
    val interactionType: InteractionType = InteractionType.MULTIPLE_CHOICE,
    val questionText: String,
    val voiceNarrationText: String,
    val operand1: Int,
    val operand2: Int = 0,
    val operatorSymbol: String = "",
    val correctAnswer: Int,
    val correctAnswerString: String = correctAnswer.toString(),
    val options: List<String>,
    val itemEmoji: String = "🍌",
    val secondaryEmoji: String = "🐒",
    val fractionNumerator: Int = 1,
    val fractionDenominator: Int = 2,
    val fractionTotalSlices: Int = 4,
    val fractionColoredSlices: Int = 1,
    val sequenceNumbers: List<Int?> = emptyList(), // e.g. [2, 4, null, 8]
    val hintStep1: String,
    val hintStep2: String,
    val encouragement: String,
    val difficultyLevel: Int = 1 // 1=Age 6 (intro), 2=Age 7-8 (standard), 3=Age 8-9 (advanced)
)
