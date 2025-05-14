package com.example.test

import androidx.lifecycle.LiveData

class QuestionRepository(private val questionDao: QuestionDao) {
    suspend fun insertAll(questions: List<model>) {
        questionDao.insertAll(questions)
    }

    fun getRandomQuestion(lang: String): LiveData<model> =
    questionDao.getRandomQuestion(lang)
}

