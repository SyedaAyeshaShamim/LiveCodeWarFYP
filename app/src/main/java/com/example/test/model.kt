package com.example.test

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionsCplus")
data class model (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val language: String,
    val questionText: String,
    val correctAnswer: String
)
