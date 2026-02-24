package com.example.habittracker.data

import java.time.LocalDate

data class Habit(
    val id: Int,
    val name: String,
    val completedDates: MutableSet<LocalDate> = mutableSetOf()
) {
    fun isCompletedToday(): Boolean {
        return completedDates.contains(LocalDate.now())
    }

    fun getStreak(): Int {
        var streak = 0
        var date = LocalDate.now()
        while (completedDates.contains(date)) {
            streak++
            date = date.minusDays(1)
        }
        return streak
    }
}