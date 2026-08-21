package com.example.domain.model

import kotlin.random.Random

object MathChallengeGenerator {

    fun generateProblem(
        subject: MathSubject,
        difficulty: Int = 1, // 1: Ages 6, 2: Ages 7-8, 3: Ages 8-9
        questionIndex: Int = 0
    ): MathProblem {
        val random = Random.Default
        val id = "prob_${subject.name}_${System.currentTimeMillis()}_${random.nextInt(1000)}"

        return when (subject) {
            MathSubject.COUNTING -> generateCountingProblem(id, difficulty, random)
            MathSubject.ADDITION -> generateAdditionProblem(id, difficulty, random, questionIndex)
            MathSubject.SUBTRACTION -> generateSubtractionProblem(id, difficulty, random)
            MathSubject.MULTIPLICATION -> generateMultiplicationProblem(id, difficulty, random)
            MathSubject.DIVISION -> generateDivisionProblem(id, difficulty, random)
            MathSubject.FRACTIONS -> generateFractionProblem(id, difficulty, random)
            MathSubject.COMPARISON -> generateComparisonProblem(id, difficulty, random)
            MathSubject.WORD_PROBLEMS -> generateWordProblem(id, difficulty, random)
        }
    }

    private fun generateCountingProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val emojis = listOf("🍌", "🥥", "🍍", "⭐", "🦜", "🦋", "🌺", "🍉")
        val emoji = emojis.random()
        val count = when (difficulty) {
            1 -> random.nextInt(3, 8)
            2 -> random.nextInt(6, 13)
            else -> random.nextInt(10, 19)
        }
        val options = generateNumberOptions(count, 4, random)

        return MathProblem(
            id = id,
            subject = MathSubject.COUNTING,
            visualType = VisualType.OBJECT_COUNT,
            interactionType = if (count <= 10) InteractionType.TAP_TO_COUNT else InteractionType.MULTIPLE_CHOICE,
            questionText = "How many $emoji can you count?",
            voiceNarrationText = "How many items can you count in the jungle? Tap them or pick the number!",
            operand1 = count,
            correctAnswer = count,
            options = options.map { it.toString() },
            itemEmoji = emoji,
            hintStep1 = "Touch each item one by one to count them out loud!",
            hintStep2 = "There are $count items altogether.",
            encouragement = "Super counting eyes! You found all $count $emoji! 🎉",
            difficultyLevel = difficulty
        )
    }

    private fun generateAdditionProblem(id: String, difficulty: Int, random: Random, index: Int): MathProblem {
        val (num1, num2) = when (difficulty) {
            1 -> Pair(random.nextInt(1, 6), random.nextInt(1, 5)) // Up to 10
            2 -> Pair(random.nextInt(4, 10), random.nextInt(3, 10)) // Up to 20
            else -> Pair(random.nextInt(9, 25), random.nextInt(7, 25)) // Up to 50
        }
        val sum = num1 + num2
        val fruitEmojis = listOf("🍌", "🥥", "🥭", "🍍", "🍉", "🍓")
        val emoji = fruitEmojis.random()
        val options = generateNumberOptions(sum, 4, random)

        val interaction = if (difficulty == 1 && index % 2 == 0) InteractionType.DRAG_DROP_FEED else InteractionType.MULTIPLE_CHOICE

        return MathProblem(
            id = id,
            subject = MathSubject.ADDITION,
            visualType = VisualType.ADDITION_GROUPS,
            interactionType = interaction,
            questionText = "$num1 + $num2 = ?",
            voiceNarrationText = "What is $num1 plus $num2? How many are there altogether?",
            operand1 = num1,
            operand2 = num2,
            operatorSymbol = "+",
            correctAnswer = sum,
            options = options.map { it.toString() },
            itemEmoji = emoji,
            hintStep1 = "Start with $num1, then count forward $num2 more!",
            hintStep2 = "$num1 plus $num2 makes $sum.",
            encouragement = "Awesome addition adventurer! $num1 + $num2 = $sum! 🌟",
            difficultyLevel = difficulty
        )
    }

    private fun generateSubtractionProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val (total, takeaway) = when (difficulty) {
            1 -> {
                val t = random.nextInt(4, 10)
                Pair(t, random.nextInt(1, t))
            }
            2 -> {
                val t = random.nextInt(8, 16)
                Pair(t, random.nextInt(2, t - 1))
            }
            else -> {
                val t = random.nextInt(15, 30)
                Pair(t, random.nextInt(4, t - 2))
            }
        }
        val diff = total - takeaway
        val options = generateNumberOptions(diff, 4, random)
        val emojis = listOf("🥥", "🍌", "🍎", "🪵", "🌰")
        val emoji = emojis.random()

        return MathProblem(
            id = id,
            subject = MathSubject.SUBTRACTION,
            visualType = VisualType.SUBTRACTION_TAKEAWAY,
            interactionType = InteractionType.MULTIPLE_CHOICE,
            questionText = "$total - $takeaway = ?",
            voiceNarrationText = "We had $total $emoji, and $takeaway fell down! How many are left?",
            operand1 = total,
            operand2 = takeaway,
            operatorSymbol = "-",
            correctAnswer = diff,
            options = options.map { it.toString() },
            itemEmoji = emoji,
            hintStep1 = "Take away $takeaway from the $total $emoji.",
            hintStep2 = "$total minus $takeaway leaves exactly $diff.",
            encouragement = "Brilliant subtraction! You saved $diff $emoji! 🥥✨",
            difficultyLevel = difficulty
        )
    }

    private fun generateMultiplicationProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val (groups, inEach) = when (difficulty) {
            1 -> Pair(random.nextInt(2, 4), random.nextInt(2, 5)) // 2x2, 2x3, 3x4
            2 -> Pair(random.nextInt(2, 6), random.nextInt(3, 7)) // 4x4, 5x6
            else -> Pair(random.nextInt(4, 9), random.nextInt(4, 9)) // 6x7, 7x8
        }
        val product = groups * inEach
        val options = generateNumberOptions(product, 4, random)
        val animalEmojis = listOf("🐒", "🐅", "🦜", "🐘", "🐸", "🦁")
        val emoji = animalEmojis.random()

        return MathProblem(
            id = id,
            subject = MathSubject.MULTIPLICATION,
            visualType = VisualType.MULTIPLICATION_GRID,
            interactionType = InteractionType.MULTIPLE_CHOICE,
            questionText = "$groups × $inEach = ?",
            voiceNarrationText = "Look! There are $groups groups with $inEach $emoji in each group. How many animals altogether?",
            operand1 = groups,
            operand2 = inEach,
            operatorSymbol = "×",
            correctAnswer = product,
            options = options.map { it.toString() },
            itemEmoji = emoji,
            hintStep1 = "Think of it as $groups groups of $inEach: " + (1..groups).joinToString(" + ") { "$inEach" },
            hintStep2 = "$groups times $inEach is $product!",
            encouragement = "Roaring success! $groups times $inEach is $product! 🐾🎉",
            difficultyLevel = difficulty
        )
    }

    private fun generateDivisionProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val (monkeys, eachGets) = when (difficulty) {
            1 -> Pair(random.nextInt(2, 4), random.nextInt(2, 4))
            2 -> Pair(random.nextInt(2, 5), random.nextInt(2, 6))
            else -> Pair(random.nextInt(3, 6), random.nextInt(3, 8))
        }
        val totalFruits = monkeys * eachGets
        val options = generateNumberOptions(eachGets, 4, random)

        return MathProblem(
            id = id,
            subject = MathSubject.DIVISION,
            visualType = VisualType.DIVISION_SHARING,
            interactionType = InteractionType.MULTIPLE_CHOICE,
            questionText = "$totalFruits ÷ $monkeys = ?",
            voiceNarrationText = "Share $totalFruits pineapples equally between $monkeys friendly monkeys. How many does each monkey get?",
            operand1 = totalFruits,
            operand2 = monkeys,
            operatorSymbol = "÷",
            correctAnswer = eachGets,
            options = options.map { it.toString() },
            itemEmoji = "🍍",
            secondaryEmoji = "🐒",
            hintStep1 = "If $monkeys monkeys share $totalFruits fruits equally, divide $totalFruits into $monkeys equal parts.",
            hintStep2 = "Each monkey gets $eachGets pineapples!",
            encouragement = "Fair feast! Every monkey gets $eachGets pineapples! 🍍🐒",
            difficultyLevel = difficulty
        )
    }

    private fun generateFractionProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val fractionTypes = when (difficulty) {
            1 -> listOf(Pair(1, 2), Pair(1, 4), Pair(2, 4), Pair(1, 3))
            2 -> listOf(Pair(1, 3), Pair(2, 3), Pair(3, 4), Pair(2, 4), Pair(1, 6))
            else -> listOf(Pair(3, 8), Pair(5, 8), Pair(2, 5), Pair(3, 5), Pair(2, 6))
        }
        val (num, den) = fractionTypes.random()
        val correctString = "$num/$den"

        val allFractions = listOf("1/2", "1/3", "2/3", "1/4", "3/4", "1/6", "2/5", "3/8", "5/8")
        val wrongFractions = allFractions.filter { it != correctString }.shuffled().take(3)
        val options = (wrongFractions + correctString).shuffled()

        return MathProblem(
            id = id,
            subject = MathSubject.FRACTIONS,
            visualType = VisualType.FRACTION_SHAPE,
            interactionType = InteractionType.MULTIPLE_CHOICE,
            questionText = "What fraction of the pizza is sliced?",
            voiceNarrationText = "What fraction is shown in the tropical pizza? Count the highlighted slices!",
            operand1 = num,
            operand2 = den,
            fractionNumerator = num,
            fractionDenominator = den,
            fractionTotalSlices = den,
            fractionColoredSlices = num,
            correctAnswer = num,
            correctAnswerString = correctString,
            options = options,
            itemEmoji = "🍕",
            hintStep1 = "The top number (numerator) is the colored slices: $num. The bottom number is the total slices: $den.",
            hintStep2 = "It represents $num out of $den parts, written as $num/$den.",
            encouragement = "Magnificent math chef! You mastered $num/$den! 🍕🌟",
            difficultyLevel = difficulty
        )
    }

    private fun generateComparisonProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val (n1, n2) = when (difficulty) {
            1 -> {
                val a = random.nextInt(1, 12)
                val b = if (random.nextBoolean()) a else random.nextInt(1, 12)
                Pair(a, b)
            }
            2 -> {
                val a = random.nextInt(10, 50)
                val b = if (random.nextInt(4) == 0) a else random.nextInt(10, 50)
                Pair(a, b)
            }
            else -> {
                val a = random.nextInt(30, 99)
                val b = if (random.nextInt(4) == 0) a else random.nextInt(30, 99)
                Pair(a, b)
            }
        }
        val symbol = when {
            n1 > n2 -> ">"
            n1 < n2 -> "<"
            else -> "="
        }

        return MathProblem(
            id = id,
            subject = MathSubject.COMPARISON,
            visualType = VisualType.COMPARISON_SCALE,
            interactionType = InteractionType.MULTIPLE_CHOICE,
            questionText = "$n1 [ ? ] $n2",
            voiceNarrationText = "Compare the numbers: is $n1 greater than, less than, or equal to $n2? Chomper the croc loves the bigger number!",
            operand1 = n1,
            operand2 = n2,
            correctAnswer = if (n1 > n2) 1 else if (n1 < n2) 2 else 3,
            correctAnswerString = symbol,
            options = listOf(">", "<", "="),
            itemEmoji = "🐊",
            hintStep1 = "The crocodile's mouth opens toward the BIGGER number.",
            hintStep2 = "$n1 is ${if (n1 > n2) "greater than" else if (n1 < n2) "less than" else "equal to"} $n2, so we write $n1 $symbol $n2.",
            encouragement = "Chomper is happy! $n1 $symbol $n2 is correct! 🐊💚",
            difficultyLevel = difficulty
        )
    }

    private fun generateWordProblem(id: String, difficulty: Int, random: Random): MathProblem {
        val wordTemplates = listOf(
            WordProblemTemplate(
                "Pip the monkey picked {a} sweet yellow bananas in the morning, and Koko found {b} more bananas in the afternoon.",
                "How many bananas did they pick altogether?",
                "Pip picked {a} bananas and Koko found {b} more. How many bananas in total?",
                MathSubject.ADDITION,
                "🍌"
            ),
            WordProblemTemplate(
                "There were {a} juicy coconuts high on a tall palm tree. A playful breeze made {b} coconuts drop to the soft sand.",
                "How many coconuts are still up on the tree?",
                "There were {a} coconuts and {b} fell down. How many coconuts are left?",
                MathSubject.SUBTRACTION,
                "🥥"
            ),
            WordProblemTemplate(
                "Leo the lion spotted {a} friendly safari trucks. Each truck was carrying {b} explorer children.",
                "How many children are exploring the jungle in total?",
                "There are {a} safari trucks with {b} explorer children in each truck. How many children altogether?",
                MathSubject.MULTIPLICATION,
                "🚙"
            )
        )

        val template = wordTemplates.random()
        val (a, b, ans) = when (template.mathType) {
            MathSubject.ADDITION -> {
                val n1 = if (difficulty == 1) random.nextInt(2, 6) else random.nextInt(4, 15)
                val n2 = if (difficulty == 1) random.nextInt(2, 5) else random.nextInt(3, 12)
                Triple(n1, n2, n1 + n2)
            }
            MathSubject.SUBTRACTION -> {
                val n1 = if (difficulty == 1) random.nextInt(6, 12) else random.nextInt(12, 25)
                val n2 = if (difficulty == 1) random.nextInt(1, n1 - 1) else random.nextInt(3, n1 - 2)
                Triple(n1, n2, n1 - n2)
            }
            else -> {
                val n1 = random.nextInt(2, 4)
                val n2 = random.nextInt(2, 5)
                Triple(n1, n2, n1 * n2)
            }
        }

        val storyText = template.story.replace("{a}", "$a").replace("{b}", "$b")
        val question = template.question
        val fullVoice = template.voice.replace("{a}", "$a").replace("{b}", "$b")
        val options = generateNumberOptions(ans, 4, random)

        return MathProblem(
            id = id,
            subject = MathSubject.WORD_PROBLEMS,
            visualType = VisualType.WORD_STORY,
            interactionType = InteractionType.MULTIPLE_CHOICE,
            questionText = "$storyText\n\n$question",
            voiceNarrationText = fullVoice,
            operand1 = a,
            operand2 = b,
            correctAnswer = ans,
            options = options.map { it.toString() },
            itemEmoji = template.emoji,
            hintStep1 = "Look at the numbers: $a and $b. Are we combining them or taking some away?",
            hintStep2 = "The answer is $ans ${template.emoji}.",
            encouragement = "Brilliant jungle detective! You solved the mystery with $ans! 📜✨",
            difficultyLevel = difficulty
        )
    }

    private data class WordProblemTemplate(
        val story: String,
        val question: String,
        val voice: String,
        val mathType: MathSubject,
        val emoji: String
    )

    private fun generateNumberOptions(correct: Int, count: Int, random: Random): List<Int> {
        val options = mutableSetOf(correct)
        var offset = 1
        while (options.size < count) {
            val delta = (if (random.nextBoolean()) offset else -offset)
            val candidate = (correct + delta).coerceAtLeast(0)
            options.add(candidate)
            offset++
            if (offset > 15) {
                options.add(random.nextInt(0, correct + 10))
            }
        }
        return options.toList().shuffled()
    }
}
