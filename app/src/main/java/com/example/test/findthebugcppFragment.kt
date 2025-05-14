package com.example.test

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.test.databinding.FragmentFindthebugcppBinding

class findthebugcppFragment : Fragment() {
    private var _binding: FragmentFindthebugcppBinding? = null
    private val binding get() = _binding!!

    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 150000 // 2.5 minutes
    private var currentQuestionIndex = 0
    private var earnedCoins = 0
    private var correctAnswers = 0

    // Master list of all bug-finding questions
    private val allQuestions = listOf(
        BugQuestion(
            questionCode = """
                int main() {
                    int x = 5;
                    int y = 0;
                    cout << x / y;
                    return 0;
                }
            """.trimIndent(),
            options = listOf(
                "Missing semicolon after return",
                "Division by zero error",
                "Variable x not declared",
                "Missing #include <iostream>"
            ),
            correctAnswer = 1 // Division by zero
        ),
        BugQuestion(
            questionCode = """
                #include <iostream>
                using namespace std;
                
                int main() {
                    for(int i=0; i<10 i++) {
                        cout << i << " ";
                    }
                    return 0;
                }
            """.trimIndent(),
            options = listOf(
                "Missing semicolon in for loop",
                "Incorrect namespace usage",
                "Missing curly brace",
                "Incorrect cout syntax"
            ),
            correctAnswer = 0 // Missing semicolon in for loop
        ),
        BugQuestion(
            questionCode = """
        #include <iostream>
        using namespace std;
        
        int main() {
            int* ptr = new int(5);
            cout << *ptr;
            return 0;
        }
    """.trimIndent(),
            options = listOf(
                "Missing semicolon",
                "Memory leak - no delete",
                "Incorrect pointer syntax",
                "Missing include statement"
            ),
            correctAnswer = 1
        ),BugQuestion(
            questionCode = """
        #include <iostream>
        using namespace std;
        
        int main() {
            int i = 0;
            while(i < 10) {
                cout << i << " ";
            }
            return 0;
        }
    """.trimIndent(),
            options = listOf(
                "Missing semicolon",
                "Infinite loop - no increment",
                "Wrong comparison operator",
                "Missing break statement"
            ),
            correctAnswer = 1
        ),
        BugQuestion(
            questionCode = """
        #include <iostream>
        using namespace std;
        
        int main() {
            int x;
            cout << x;
            return 0;
        }
    """.trimIndent(),
            options = listOf(
                "Variable not declared",
                "Uninitialized variable",
                "Missing return value",
                "Incorrect cout usage"
            ),
            correctAnswer = 1
        ),
        BugQuestion(
            questionCode = """
        #include <iostream>
        using namespace std;
        
        int main() {
            char str[3] = {'a', 'b', 'c'};
            cout << str;
            return 0;
        }
    """.trimIndent(),
            options = listOf(
                "Missing null terminator",
                "Array size too small",
                "Incorrect char initialization",
                "Missing string header"
            ),
            correctAnswer = 0
        ),
        // Add more bug questions here...
        BugQuestion(
            questionCode = """
                #include <iostream>
                using namespace std;
                
                int main() {
                    int arr[3] = {1, 2, 3};
                    cout << arr[5];
                    return 0;
                }
            """.trimIndent(),
            options = listOf(
                "Array size too small",
                "Array index out of bounds",
                "Missing array values",
                "Incorrect return type"
            ),
            correctAnswer = 1 // Array index out of bounds
        )


    )

    // Current set of 5 random questions
    private lateinit var questions: List<BugQuestion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Select 5 random questions
        questions = allQuestions.shuffled().take(5)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindthebugcppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize first question
        showQuestion(currentQuestionIndex)
        startTimer()

        // Set click listeners for all bug options
        binding.codeTextView1.setOnClickListener { checkAnswer(0) }
        binding.codeTextView2.setOnClickListener { checkAnswer(1) }
        binding.codeTextView3.setOnClickListener { checkAnswer(2) }
        binding.codeTextView4.setOnClickListener { checkAnswer(3) }
    }

    private fun showQuestion(index: Int) {
        if (index < questions.size) {
            val question = questions[index]
            binding.codeTextView.text = question.questionCode

            // Set options text
            binding.codeTextView1.text = question.options[0]
            binding.codeTextView2.text = question.options[1]
            binding.codeTextView3.text = question.options[2]
            binding.codeTextView4.text = question.options[3]

            // Update question counter
            binding.tvQuestion.text = "Find the Bug (${index + 1}/${questions.size})"
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                navigateToResults()
            }
        }.start()
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun checkAnswer(selectedAnswerIndex: Int) {
        if (selectedAnswerIndex == questions[currentQuestionIndex].correctAnswer) {
            correctAnswers++
            earnedCoins += 10
            binding.tvCoins.text = "Coins: $earnedCoins"
            Toast.makeText(context, "Correct! +10 coins", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                context,
                "Wrong! The correct bug was: ${questions[currentQuestionIndex].options[questions[currentQuestionIndex].correctAnswer]}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Move to next question or finish
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showQuestion(currentQuestionIndex)
        } else {
            navigateToResults()
        }
    }

    private fun navigateToResults() {
        timer.cancel()

        val bundle = Bundle().apply {
            putInt("earnedCoins", earnedCoins)
            putInt("correctAnswers", correctAnswers)
            putInt("totalQuestions", questions.size)
        }

        findNavController().navigate(
            R.id.action_findthebugcppFragment_to_resultFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        _binding = null
    }

    // Data class for bug questions
    data class BugQuestion(
        val questionCode: String,
        val options: List<String>,
        val correctAnswer: Int // Index of correct option (0-based)
    )
}