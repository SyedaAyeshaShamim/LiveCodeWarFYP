package com.example.test

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [model::class], version = 1, exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    val questions = listOf(
                                        model(language = "C++", questionText = "What is the correct syntax to output 'Hello World' in C++?", correctAnswer = "cout << 'Hello World';"),
                                        model(language = "C++", questionText = "Which header file is needed to use 'std::cout'?", correctAnswer = "<iostream>"),
                                        model(language = "C++", questionText = "What is the correct syntax to declare a variable of type integer?", correctAnswer = "int x;"),
                                        model(language = "C++", questionText = "Which operator is used to assign a value to a variable?", correctAnswer = "="),
                                        model(language = "C++", questionText = "What is the correct syntax to read input from the user?", correctAnswer = "std::cin >> x;"),
                                        model(language = "C++", questionText = "Which operator is used to compare two values for equality?", correctAnswer = "=="),
                                        model(language = "C++", questionText = "What is the correct syntax to define a function in C++?", correctAnswer = "returnType functionName(parameters) { }"),
                                        model(language = "C++", questionText = "Which keyword is used to define a constant value in C++?", correctAnswer = "const"),
                                        model(language = "C++", questionText = "What is the correct syntax to declare an array of 5 integers?", correctAnswer = "int arr[5];"),
                                        model(language = "C++", questionText = "Which loop is used when the number of iterations is known beforehand?", correctAnswer = "for"),
                                        model(language = "C++", questionText = "Which loop is used when the number of iterations is not known beforehand?", correctAnswer = "while"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a class in C++?", correctAnswer = "class ClassName { };"),
                                        model(language = "C++", questionText = "Which keyword is used to create an object of a class?", correctAnswer = "new"),
                                        model(language = "C++", questionText = "Which operator is used to access members of a class or structure through a pointer?", correctAnswer = "->"),
                                        model(language = "C++", questionText = "What is the correct syntax to declare a pointer to an integer?", correctAnswer = "int* ptr;"),
                                        model(language = "C++", questionText = "Which operator is used to allocate memory dynamically?", correctAnswer = "new"),
                                        model(language = "C++", questionText = "Which operator is used to deallocate memory that was previously allocated with new?", correctAnswer = "delete"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a constructor in a class?", correctAnswer = "ClassName() { }"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a destructor in a class?", correctAnswer = "~ClassName() { }"),
                                        model(language = "C++", questionText = "Which keyword is used to inherit a class in C++?", correctAnswer = "public"),
                                        model(language = "C++", questionText = "What is the correct syntax to create a derived class?", correctAnswer = "class Derived : public Base { };"),
                                        model(language = "C++", questionText = "Which keyword is used to prevent a function from being overridden in a derived class?", correctAnswer = "final"),
                                        model(language = "C++", questionText = "What is the correct syntax to declare a reference to an integer?", correctAnswer = "int& ref = x;"),
                                        model(language = "C++", questionText = "Which operator is used to access members of a class or structure?", correctAnswer = "."),
                                        model(language = "C++", questionText = "What is the correct syntax to define a function template?", correctAnswer = "template <typename T> T functionName(T arg) { }"),
                                        model(language = "C++", questionText = "Which keyword is used to define a constant value that cannot be changed?", correctAnswer = "const"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a constant pointer?", correctAnswer = "int* const ptr;"),
                                        model(language = "C++", questionText = "Which operator is used to compare two values for inequality?", correctAnswer = "!="),
                                        model(language = "C++", questionText = "What is the correct syntax to define a function with default arguments?", correctAnswer = "void functionName(int x = 5) { }"),
                                        model(language = "C++", questionText = "Which keyword is used to define a function that does not return a value?", correctAnswer = "void"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a function that returns a reference?", correctAnswer = "int& functionName() { }"),
                                        model(language = "C++", questionText = "Which operator is used to dereference a pointer?", correctAnswer = "*"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a function that returns a pointer?", correctAnswer = "int* functionName() { }"),
                                        model(language = "C++", questionText = "Which operator is used to access members of a class or structure through a pointer?", correctAnswer = "->"),
                                        model(language = "C++", questionText = "What is the correct syntax to define a function that takes a pointer as an argument?", correctAnswer = "void functionName(int* ptr) { }"),
                                        model(language = "C++", questionText = "Which operator is used to allocate memory dynamically for an array?", correctAnswer = "new[]"),
                                        model(language = "C++", questionText = "What is the correct syntax to deallocate memory that was previously allocated for an array?", correctAnswer = "delete[]"),
                                        model(language = "C++", questionText = "Which keyword is used to define a constant pointer to a constant value?", correctAnswer = "const int* const ptr;")
                                    )

                                    database.questionDao().insertAll(questions)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
