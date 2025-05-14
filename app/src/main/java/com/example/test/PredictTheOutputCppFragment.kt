package com.example.test

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.test.databinding.FragmentPredictTheOutputCppBinding

class PredictTheOutputCppFragment : Fragment() {
    private var _binding: FragmentPredictTheOutputCppBinding? = null
    private val binding get() = _binding!!

    // Game state
    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis = 150000L // 2.5 minutes
    private var currentQuestionIndex = 0
    private var earnedCoins = 0
    private var correctAnswers = 0
    private var currentScore = 100 // Starts at 100, decreases over time

    // Questions
    private val questions = listOf(
        Question(
            code = """
                for (int i = 0; i < 3; i++) {
                    cout << i;
                }
            """.trimIndent(),
            answer = "012",
            explanation = "Loop prints numbers 0, 1, 2 sequentially"
        ),
        Question(
            code = """
                int x = 5;
                cout << x++ << ++x;
            """.trimIndent(),
            answer = "57",
            explanation = "x++ returns 5 then increments, ++x increments then returns 7"
        ),
        Question(
            code = """
                int arr[] = {1, 2, 3};
                cout << arr[1] + arr[2];
            """.trimIndent(),
            answer = "5",
            explanation = "arr[1] is 2 and arr[2] is 3, sum is 5"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictTheOutputCppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuestion()
        startTimer()
        startBlurAnimation()

        binding.btnSubmit.setOnClickListener {
            checkAnswer()
        }
    }

    private fun setupQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            binding.tvCodeSnippet.text = question.code
            binding.tvCorrectOutput.text = question.answer
            binding.etUserGuess.text?.clear()
            currentScore = 100
            updateTimerDisplay()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                currentScore = maxOf(0, currentScore - 2) // Lose 2 points per second
                updateTimerDisplay()
            }

            override fun onFinish() {
                binding.timerText.text = "Time's up!"
                checkAnswer()
            }
        }.start()
    }

    private fun updateTimerDisplay() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
        binding.tvTimer.text = "Score: $currentScore"
    }

    private fun startBlurAnimation() {
        binding.blurOverlay.alpha = 1f
        binding.blurOverlay.animate()
            .alpha(0f)
            .setDuration(15000) // 15 seconds to fully reveal
            .start()
    }

    private fun checkAnswer() {
        timer.cancel()

        val userAnswer = binding.etUserGuess.text.toString().trim()
        val correctAnswer = binding.tvCorrectOutput.text.toString()

        if (userAnswer == correctAnswer) {
            handleCorrectAnswer()
        } else {
            handleIncorrectAnswer()
        }
    }

    private fun handleCorrectAnswer() {
        correctAnswers++
        val coinsEarned = currentScore / 10
        earnedCoins += coinsEarned

        Toast.makeText(
            requireContext(),
            "Correct! +$coinsEarned coins\n${questions[currentQuestionIndex].explanation}",
            Toast.LENGTH_LONG
        ).show()

        moveToNextQuestion()
    }

    private fun handleIncorrectAnswer() {
        Toast.makeText(
            requireContext(),
            "Incorrect! Correct answer was: ${binding.tvCorrectOutput.text}\n${questions[currentQuestionIndex].explanation}",
            Toast.LENGTH_LONG
        ).show()

        // Allow retry with same question
        timeLeftInMillis = 150000L
        setupQuestion()
        startTimer()
        startBlurAnimation()
    }

    private fun moveToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            timeLeftInMillis = 150000L
            setupQuestion()
            startTimer()
            startBlurAnimation()
        } else {
            navigateToResults()
        }
    }

    private fun navigateToResults() {
        val bundle = Bundle().apply {
            putInt("earnedCoins", earnedCoins)
            putInt("correctAnswers", correctAnswers)
            putInt("totalQuestions", questions.size)
        }
        findNavController().navigate(
            R.id.action_predictTheOutputCppFragment_to_resultFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        _binding = null
    }

    data class Question(
        val code: String,
        val answer: String,
        val explanation: String
    )
}