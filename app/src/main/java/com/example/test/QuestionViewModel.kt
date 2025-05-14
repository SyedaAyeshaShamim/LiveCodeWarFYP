package com.example.test
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuestionRepository

    init {
        val questiondao = AppDatabase.getDatabase(application).questionDao()
        repository = QuestionRepository(questiondao)
    }

    fun getRandomQuestion(lang: String): LiveData<model> {
        return repository.getRandomQuestion(lang)
    }

    fun insertQuestions(questions: List<model>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAll(questions)
        }
    }
}


