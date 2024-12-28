package com.example.taskit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val title: String,
    val datetime: String,
    val day: String,
    val room: String,
    val teacher: String,
    val todo: String,
    val deadlines: String,
    val notes: String
)
