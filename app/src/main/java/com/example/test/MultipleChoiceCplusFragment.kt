package com.example.test

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.test.databinding.FragmentMultipleChoiceCplusBinding


class MultipleChoiceCplusFragment : Fragment() {
    private var _binding: FragmentMultipleChoiceCplusBinding? = null
    private val binding get() = _binding!!

    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 150000 // 2.5 minutes = 150000 ms
    private var currentQuestionIndex = 0
    private var earnedCoins = 0
    private var correctAnswers = 0

    // List of questions with options and correct answers
    private val allQuestions = listOf(
        Question(
            code = "int a = 5;\ncout << ++a;",
            options = listOf("A. 4", "B. 5", "C. 6", "D. Error"),
            correctAnswer = 2
        ),
        Question(
            code = "int x = 3;\nint y = x++;\ncout << y;",
            options = listOf("A. 3", "B. 4", "C. 0", "D. Error"),
            correctAnswer = 0
        ),
        Question(
            code = "for(int i=0; i<3; i++)\n  cout << i;",
            options = listOf("A. 012", "B. 123", "C. 000", "D. Error"),
            correctAnswer = 0
        ),
        Question(
            code = "int arr[] = {1,2};\ncout << arr[2];",
            options = listOf("A. 0", "B. 1", "C. 2", "D. Garbage value"),
            correctAnswer = 3
        ),
        Question(
            code = "int a = 10;\nint &b = a;\nb = 20;\ncout << a;",
            options = listOf("A. 10", "B. 20", "C. 0", "D. Error"),
            correctAnswer = 1
        ),
        // Add 25 more questions below
        Question(
            code = "int x = 5;\ncout << (x > 2 ? 1 : 0);",
            options = listOf("A. 0", "B. 1", "C. 5", "D. Error"),
            correctAnswer = 1
        ),
        Question(
            code = "int a = 2;\nint b = a++ + ++a;\ncout << b;",
            options = listOf("A. 4", "B. 5", "C. 6", "D. 7"),
            correctAnswer = 2
        ),
        Question(
            code = "int x = 0;\ncout << (x && 1);",
            options = listOf("A. 0", "B. 1", "C. true", "D. false"),
            correctAnswer = 0
        ),
        Question(
            code = "int arr[5] = {1};\ncout << arr[3];",
            options = listOf("A. 0", "B. 1", "C. Garbage", "D. Error"),
            correctAnswer = 0
        ),
        Question(
            code = "int x = 3;\ncout << (x << 1);",
            options = listOf("A. 3", "B. 4", "C. 6", "D. 9"),
            correctAnswer = 2
        ),
        Question(
            code = "int a = 1;\nint b = 2;\ncout << (a = b);",
            options = listOf("A. 0", "B. 1", "C. 2", "D. Error"),
            correctAnswer = 2
        ),
        Question(
            code = "int x = 5;\ncout << sizeof(x);",
            options = listOf("A. 1", "B. 2", "C. 4", "D. 8"),
            correctAnswer = 2
        ),
        Question(
            code = "int a = 1;\nint *p = &a;\ncout << *p;",
            options = listOf("A. 0", "B. 1", "C. Address", "D. Error"),
            correctAnswer = 1
        ),
        Question(
            code = "int x = 10;\nint y = 3;\ncout << x % y;",
            options = listOf("A. 0", "B. 1", "C. 3", "D. 10"),
            correctAnswer = 1
        ),
        Question(
            code = "int a = 1;\ncout << (a++ * ++a);",
            options = listOf("A. 1", "B. 2", "C. 3", "D. 4"),
            correctAnswer = 2
        ),
        Question(
            code = "int x = 5;\nint y = x++ + x;\ncout << y;",
            options = listOf("A. 10", "B. 11", "C. 12", "D. 13"),
            correctAnswer = 1
        ),
        Question(
            code = "int a = 2;\ncout << (a == 2 ? 1 : 0);",
            options = listOf("A. 0", "B. 1", "C. 2", "D. true"),
            correctAnswer = 1
        ),
        Question(
            code = "int x = 0;\ncout << !x;",
            options = listOf("A. 0", "B. 1", "C. true", "D. false"),
            correctAnswer = 1
        ),
        Question(
            code = "int a = 3;\ncout << (a | 1);",
            options = listOf("A. 1", "B. 2", "C. 3", "D. 4"),
            correctAnswer = 2
        ),
        Question(
            code = "int x = 4;\ncout << (x & 1);",
            options = listOf("A. 0", "B. 1", "C. 4", "D. 5"),
            correctAnswer = 0
        ),
        Question(
            code = "int a = 1;\nint b = 2;\ncout << (a ^ b);",
            options = listOf("A. 0", "B. 1", "C. 2", "D. 3"),
            correctAnswer = 3
        ),
        Question(
            code = "int x = 5;\ncout << (x >> 1);",
            options = listOf("A. 1", "B. 2", "C. 3", "D. 5"),
            correctAnswer = 1
        ),
        Question(
            code = "int a = 2;\ncout << ~a;",
            options = listOf("A. -2", "B. -3", "C. 1", "D. 2"),
            correctAnswer = 1
        ),
        Question(
            code = "int x = 1;\ncout << (x << 3);",
            options = listOf("A. 1", "B. 3", "C. 8", "D. 9"),
            correctAnswer = 2
        ),
        Question(
            code = "int a = 5;\ncout << (a /= 2);",
            options = listOf("A. 2", "B. 2.5", "C. 3", "D. Error"),
            correctAnswer = 0
        ),
        Question(
            code = "int x = 3;\ncout << (x *= x);",
            options = listOf("A. 3", "B. 6", "C. 9", "D. Error"),
            correctAnswer = 2
        ),
        Question(
            code = "int a = 1;\ncout << (a += 2);",
            options = listOf("A. 1", "B. 2", "C. 3", "D. Error"),
            correctAnswer = 2
        ),
        Question(
            code = "int x = 10;\ncout << (x -= 5);",
            options = listOf("A. 5", "B. 10", "C. -5", "D. Error"),
            correctAnswer = 0
        ),
        Question(
            code = "int a = 4;\ncout << (a %= 3);",
            options = listOf("A. 0", "B. 1", "C. 3", "D. 4"),
            correctAnswer = 1
        ),
        Question(
            code = "int x = 2;\ncout << (x <<= 2);",
            options = listOf("A. 2", "B. 4", "C. 8", "D. Error"),
            correctAnswer = 2
        )
    )
    // This will hold the 5 randomly selected questions
    private lateinit var questions: List<Question>  // Changed to plural to match usage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Select 5 random questions when fragment is created
        questions = allQuestions.shuffled().take(5)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultipleChoiceCplusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize first question
        showQuestion(currentQuestionIndex)
        startTimer()

        binding.btnradio.setOnClickListener {
            checkAnswer()
        }
    }

    private fun showQuestion(index: Int) {
        if (index < questions.size) {
            val question = questions[index]
            binding.tvCppCode.text = question.code
            binding.rgOptions.clearCheck()

            // Set options text
            binding.rbOptionA.text = question.options[0]
            binding.rbOptionB.text = question.options[1]
            binding.rbOptionC.text = question.options[2]
            binding.rbOptionD.text = question.options[3]

            // Update question counter
            binding.scoreText.text = "Questions: ${index + 1}/5"
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                // Time's up - navigate to results
                navigateToResults()
            }
        }.start()
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun checkAnswer() {
        val selectedId = binding.rgOptions.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton = view?.findViewById<RadioButton>(selectedId)
            val selectedAnswerIndex = when (selectedId) {
                R.id.rb_option_a -> 0
                R.id.rb_option_b -> 1
                R.id.rb_option_c -> 2
                R.id.rb_option_d -> 3
                else -> -1
            }

            if (selectedAnswerIndex == questions[currentQuestionIndex].correctAnswer) {
                correctAnswers++
                earnedCoins += 10
                binding.tvCoins.text = "Coins: $earnedCoins"
                Toast.makeText(context, "Correct! +10 coins", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Wrong! Correct answer was: ${questions[currentQuestionIndex].options[questions[currentQuestionIndex].correctAnswer]}",
                    Toast.LENGTH_SHORT).show()
            }

            // Move to next question or finish
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                showQuestion(currentQuestionIndex)
            } else {
                navigateToResults()
            }
        } else {
            Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToResults() {
        timer.cancel()

        // Pass data to results fragment
        val bundle = Bundle().apply {
            putInt("earnedCoins", earnedCoins)
            putInt("correctAnswers", correctAnswers)
            putInt("totalQuestions", questions.size)
        }

        findNavController().navigate(
            R.id.action_multipleChoiceCplusFragment_to_resultFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        _binding = null
    }

    // Data class for questions
    data class Question(
        val code: String,
        val options: List<String>,
        val correctAnswer: Int // Index of correct option (0-based)
    )
}