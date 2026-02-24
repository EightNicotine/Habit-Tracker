package com.example.habittracker.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habittracker.data.Habit
import java.time.LocalDate

@Composable
fun HabitListScreen(modifier: Modifier = Modifier) {
    var habits by remember { mutableStateOf(listOf(
        Habit(1, "Beber 2L de água"),
        Habit(2, "Ler 20 páginas"),
        Habit(3, "Treinar 30 min")
    )) }

    var showAddDialog by remember { mutableStateOf(false) }
    var newHabitName by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar hábito")
            }
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Meus Hábitos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                itemsIndexed(habits) { index, habit ->
                    HabitItem(
                        habit = habit,
                        onToggle = {
                            val newHabits = habits.toMutableList()
                            val updatedHabit = habit.copy(
                                completedDates = habit.completedDates.toMutableSet().apply {
                                    if (it) add(LocalDate.now()) else remove(LocalDate.now())
                                }
                            )
                            newHabits[index] = updatedHabit
                            habits = newHabits
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Novo Hábito") },
            text = {
                OutlinedTextField(
                    value = newHabitName,
                    onValueChange = { newHabitName = it },
                    label = { Text("Nome do hábito") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newHabitName.isNotBlank()) {
                        val newId = (habits.maxOfOrNull { it.id } ?: 0) + 1
                        habits = habits + Habit(newId, newHabitName.trim())
                        newHabitName = ""
                    }
                    showAddDialog = false
                }) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun HabitItem(habit: Habit, onToggle: (Boolean) -> Unit) {
    val isChecked = habit.isCompletedToday()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Streak: ${habit.getStreak()} dias",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onToggle(it) }
            )
        }
    }
}