package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.ChildProfileDao
import com.example.data.local.dao.RewardItemDao
import com.example.data.local.dao.SkillProgressDao
import com.example.data.local.dao.WorldLevelDao
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.RewardItem
import com.example.data.local.entity.SkillProgress
import com.example.data.local.entity.WorldLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChildProfile::class,
        SkillProgress::class,
        WorldLevel::class,
        RewardItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childProfileDao(): ChildProfileDao
    abstract fun skillProgressDao(): SkillProgressDao
    abstract fun worldLevelDao(): WorldLevelDao
    abstract fun rewardItemDao(): RewardItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jungle_math_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            // Profile
            database.childProfileDao().insertProfile(
                ChildProfile(
                    id = 1,
                    name = "Leo",
                    age = 7,
                    selectedAvatar = "monkey_pip",
                    hatId = "safari_hat",
                    outfitId = "explorer_vest",
                    companionPetId = "baby_sloth",
                    currentLevel = 1,
                    totalStars = 14,
                    totalGems = 65,
                    currentStreak = 4,
                    lastPlayedDate = System.currentTimeMillis(),
                    totalTimeMinutes = 35
                )
            )

            // Initial Skill Progress
            database.skillProgressDao().insertAll(
                listOf(
                    SkillProgress("COUNTING", "Counting & Objects", 24, 23, 1, 95, 8, System.currentTimeMillis(), "Mastered"),
                    SkillProgress("ADDITION", "Jungle Addition", 35, 30, 3, 85, 5, System.currentTimeMillis(), "Mastered"),
                    SkillProgress("SUBTRACTION", "Coconut Subtraction", 28, 20, 5, 71, 3, System.currentTimeMillis(), "Developing"),
                    SkillProgress("MULTIPLICATION", "Animal Groups", 18, 10, 4, 55, 2, System.currentTimeMillis(), "Practicing"),
                    SkillProgress("DIVISION", "Fair Share Division", 12, 6, 3, 50, 1, System.currentTimeMillis(), "Practicing"),
                    SkillProgress("FRACTIONS", "Tropical Fractions", 15, 8, 4, 53, 2, System.currentTimeMillis(), "Practicing"),
                    SkillProgress("COMPARISON", "Croc River Comparison", 20, 18, 1, 90, 6, System.currentTimeMillis(), "Mastered"),
                    SkillProgress("WORD_PROBLEMS", "Jungle Story Problems", 14, 9, 3, 64, 2, System.currentTimeMillis(), "Developing")
                )
            )

            // Initial World Levels
            val levels = listOf(
                // World 1: Banana Grove (Counting & Addition)
                WorldLevel(1, "banana_grove", "Banana Grove", 1, "Banana Counting", "COUNTING", 5, 3, true, true, "🍌", "Count fresh sweet bananas for Pip!"),
                WorldLevel(2, "banana_grove", "Banana Grove", 2, "Monkey Addition (+5)", "ADDITION", 5, 3, true, true, "🐒", "Add bananas together up to 5!"),
                WorldLevel(3, "banana_grove", "Banana Grove", 3, "Jungle Addition (+10)", "ADDITION", 5, 2, true, true, "🌴", "Combine fruit baskets up to 10!"),
                WorldLevel(4, "banana_grove", "Banana Grove", 4, "Big Banana Sums (+20)", "ADDITION", 5, 0, true, false, "🧺", "Solve bigger sums with double digits!"),

                // World 2: Coconut Falls (Subtraction)
                WorldLevel(5, "coconut_falls", "Coconut Falls", 1, "Falling Coconuts (-5)", "SUBTRACTION", 5, 3, true, true, "🥥", "Count coconuts that fell from the palm tree!"),
                WorldLevel(6, "coconut_falls", "Coconut Falls", 2, "River Subtraction (-10)", "SUBTRACTION", 5, 2, true, true, "🌊", "Subtract objects floating down the stream!"),
                WorldLevel(7, "coconut_falls", "Coconut Falls", 3, "Waterfall Takeaways (-20)", "SUBTRACTION", 5, 0, true, false, "💧", "Subtract tricky numbers inside the mist!"),

                // World 3: Animal Savanna (Multiplication & Division)
                WorldLevel(8, "animal_savanna", "Animal Savanna", 1, "Animal Pairs (×2, ×3)", "MULTIPLICATION", 5, 1, true, false, "🐾", "Count animal paws and groups of 2 and 3!"),
                WorldLevel(9, "animal_savanna", "Animal Savanna", 2, "Tiger Multiplication (×4, ×5)", "MULTIPLICATION", 5, 0, false, false, "🐅", "Multiply groups of 4 and 5 striped friends!"),
                WorldLevel(10, "animal_savanna", "Animal Savanna", 3, "Fair Feast Division (÷2, ÷3)", "DIVISION", 5, 0, false, false, "🍍", "Share pineapples equally among animal friends!"),

                // World 4: Toucan's Fraction Feast (Fractions)
                WorldLevel(11, "fraction_feast", "Fraction Feast", 1, "Pizza Halves & Fourths", "FRACTIONS", 5, 0, false, false, "🍕", "Slice delicious jungle pies into 1/2 and 1/4!"),
                WorldLevel(12, "fraction_feast", "Fraction Feast", 2, "Watermelon Slices (1/3, 2/3)", "FRACTIONS", 5, 0, false, false, "🍉", "Discover thirds and colorful fruit wedges!"),
                WorldLevel(13, "fraction_feast", "Fraction Feast", 3, "Fraction Matching Feast", "FRACTIONS", 5, 0, false, false, "🦜", "Match fractions to visual shapes and pies!"),

                // World 5: Croc River (Comparison & Sequences)
                WorldLevel(14, "croc_river", "Croc River", 1, "Croc Hungry Mouth (> < =)", "COMPARISON", 5, 2, true, true, "🐊", "Help Chomper choose the bigger pile of snacks!"),
                WorldLevel(15, "croc_river", "Croc River", 2, "Stepping Stone Sequences", "COUNTING", 5, 1, true, false, "🪨", "Find the missing numbers along the river stones!"),
                WorldLevel(16, "croc_river", "Croc River", 3, "Jungle Story Quest", "WORD_PROBLEMS", 5, 0, false, false, "📜", "Solve funny animal word puzzles!"),

                // World 6: Treasure Temple (Mastery)
                WorldLevel(17, "treasure_temple", "Treasure Temple", 1, "Golden Vault Mixed Math", "ADDITION", 5, 0, false, false, "🏛️", "Unlock ancient temple doors with mixed math!"),
                WorldLevel(18, "treasure_temple", "Treasure Temple", 2, "Grand Math Champion Quest", "MULTIPLICATION", 5, 0, false, false, "👑", "The ultimate jungle math adventure challenge!")
            )
            database.worldLevelDao().insertAll(levels)

            // Initial Rewards & Unlockables
            val rewards = listOf(
                // Hats
                RewardItem("safari_hat", "Explorer Safari Hat", "HAT", "🤠", 0, true, true, "Classic explorer hat for sunny jungle treks.", "Starting item"),
                RewardItem("flower_crown", "Tropical Flower Crown", "HAT", "🌸", 20, true, false, "Woven with rare orchids and hibiscus.", "Earn 10 stars"),
                RewardItem("pirate_bandana", "Jungle Pirate Bandana", "HAT", "🏴‍☠️", 35, false, false, "For bold adventurers seeking treasure.", "35 Gems"),
                RewardItem("golden_crown", "Math Champion Crown", "HAT", "👑", 60, false, false, "Shines brightly when you master math skills!", "60 Gems"),
                RewardItem("astronaut_helmet", "Star Explorer Helmet", "HAT", "🚀", 100, false, false, "For out-of-this-world math achievements.", "100 Gems"),

                // Outfits
                RewardItem("explorer_vest", "Khaki Explorer Vest", "OUTFIT", "🦺", 0, true, true, "Lots of pockets for math gadgets and snacks.", "Starting item"),
                RewardItem("leaf_cloak", "Jungle Leaf Cloak", "OUTFIT", "🍃", 25, true, false, "Stay cool and hidden in the dense jungle.", "Earn 15 stars"),
                RewardItem("royal_cape", "Champion Golden Cape", "OUTFIT", "🦸", 50, false, false, "Swishes with every solved math problem.", "50 Gems"),
                RewardItem("wizard_robe", "Math Magician Robe", "OUTFIT", "🧙‍♂️", 80, false, false, "Cast magical mathematical spells.", "80 Gems"),

                // Companion Pets
                RewardItem("baby_sloth", "Sammy the Sloth", "PET", "🦥", 0, true, true, "Slow and steady, loves counting leaves.", "Starting buddy"),
                RewardItem("parrot_pip", "Polly the Parrot", "PET", "🦜", 30, true, false, "Squawks out encouraging cheers when you succeed!", "30 Gems"),
                RewardItem("baby_dino", "Rex the Baby Dino", "PET", "🦖", 75, false, false, "Friendly gentle herbivore that loves numbers.", "75 Gems"),
                RewardItem("chameleon", "Cam the Chameleon", "PET", "🦎", 50, false, false, "Changes colors to match your math mood.", "50 Gems"),

                // Badges
                RewardItem("badge_adder", "Addition Ace", "BADGE", "⭐", 0, true, false, "Mastered adding numbers up to 10!", "Level 3 Complete"),
                RewardItem("badge_counter", "Super Counter", "BADGE", "🎯", 0, true, false, "Counted over 50 jungle objects perfectly!", "20 Problems solved"),
                RewardItem("badge_subtractor", "Coconut Cracker", "BADGE", "🥥", 0, false, false, "Subtracted like a pro in Coconut Falls.", "Complete World 2"),
                RewardItem("badge_multiplication", "Multiplier Master", "BADGE", "🐾", 0, false, false, "Multiplied 10 animal groups without a mistake.", "Complete World 3"),
                RewardItem("badge_fraction", "Pizza Chef", "BADGE", "🍕", 0, false, false, "Sliced and matched 10 tropical fractions!", "Complete World 4"),
                RewardItem("badge_streak", "7-Day Explorer", "BADGE", "🔥", 0, false, false, "Practiced math 7 days in a row!", "7 Day Streak")
            )
            database.rewardItemDao().insertAll(rewards)
        }
    }
}
