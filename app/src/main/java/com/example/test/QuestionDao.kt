package com.example.test

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<model>)

    @Query("SELECT * FROM questionsCplus WHERE language = :lang ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuestion(lang: String): LiveData<model>
}

