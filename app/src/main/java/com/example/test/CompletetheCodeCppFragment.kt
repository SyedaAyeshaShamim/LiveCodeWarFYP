package com.example.test

import android.content.ClipData
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.test.databinding.FragmentCompletetheCodeCppBinding


class CompletetheCodeCppFragment : Fragment() {
    private var _binding: FragmentCompletetheCodeCppBinding? = null
    private val binding get() = _binding!!

    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 150000 // 2.5 minutes
    private var currentQuestionIndex = 0
    private var earnedCoins = 0
    private var correctAnswers = 0
    private var hintsUsed = 0

    // Sound effects
    private var dragSound: MediaPlayer? = null
    private var dropSound: MediaPlayer? = null

    // Question data (5 questions)
    private val allQuestions = listOf(
        CodeQuestion(
            partialCode = """
                void main() {
                    int x = 5;
                    ░░░░░░░░░
                    cout << x;
                }
            """.trimIndent(),
            correctAnswer = "x++;",
            options = listOf("x++;", "x--;", "x = 0;")
        ),
        CodeQuestion(
            partialCode = """
                int main() {
                    int arr[3] = {1, 2, 3};
                    int sum = 0;
                    for(int i=0; i<3; i++) {
                        ░░░░░░░░░
                    }
                    cout << sum;
                }
            """.trimIndent(),
            correctAnswer = "sum += arr[i];",
            options = listOf("sum += arr[i];", "sum = arr[i];", "arr[i]++;")
        ),
        CodeQuestion(
            partialCode = """
                #include <iostream>
                using namespace std;
                
                int main() {
                    for(int i=0; i<5; ░░░░░░░░░) {
                        cout << i << " ";
                    }
                }
            """.trimIndent(),
            correctAnswer = "i++",
            options = listOf("i++", "i--", "i+2")
        ),
        CodeQuestion(
            partialCode = """
                int main() {
                    int a = 5, b = 10;
                    ░░░░░░░░░
                    cout << "Max: " << max;
                }
            """.trimIndent(),
            correctAnswer = "int max = (a > b) ? a : b;",
            options = listOf(
                "int max = (a > b) ? a : b;",
                "int max = a + b;",
                "int max = a;"
            )
        ),
        CodeQuestion(
            partialCode = """
                int factorial(int n) {
                    if(n == 0) return 1;
                    ░░░░░░░░░
                }
            """.trimIndent(),
            correctAnswer = "return n * factorial(n-1);",
            options = listOf(
                "return n * factorial(n-1);",
                "return factorial(n);",
                "return n;"
            )
        )
    )

    private lateinit var questions: List<CodeQuestion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questions = allQuestions.shuffled().take(5) // Random 5 questions
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletetheCodeCppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize sounds
        dragSound = MediaPlayer.create(context, R.raw.buttonclick)
        dropSound = MediaPlayer.create(context, R.raw.drop)

        showQuestion(currentQuestionIndex)
        startTimer()

        // Set up drag listeners
        setDragListeners()

        binding.btnSubmit.setOnClickListener { checkSolution() }
        binding.btnReset.setOnClickListener { resetCode() }

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

    private fun showQuestion(index: Int) {
        if (index < questions.size) {
            val question = questions[index]
            binding.codeSnippet.text = question.partialCode

            // Set options for the 3 buttons
            val shuffledOptions = question.options.shuffled()
            binding.codeBlock1.text = shuffledOptions[0]
            binding.codeBlock1.tag = shuffledOptions[0]
            binding.codeBlock2.text = shuffledOptions[1]
            binding.codeBlock2.tag = shuffledOptions[1]
            binding.codeBlock3.text = shuffledOptions[2]
            binding.codeBlock3.tag = shuffledOptions[2]

            binding.scoreText.text = "Questions: ${index + 1}/${questions.size}"
        }
    }

    private fun setDragListeners() {
        // Make all 3 code blocks draggable
        listOf(binding.codeBlock1, binding.codeBlock2, binding.codeBlock3).forEach { block ->
            block.setOnLongClickListener { v ->
                dragSound?.start()
                val item = ClipData.Item(v.tag as? CharSequence)
                val dragData = ClipData(
                    v.tag as? CharSequence ?: "DRAG_DATA",
                    arrayOf("text/plain"),
                    item
                )
                v.startDragAndDrop(
                    dragData,
                    View.DragShadowBuilder(v),
                    null,
                    0
                )
                true
            }
        }

        // Set up drop target
        binding.codeSnippet.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.setBackgroundResource(R.drawable.drop_target_active)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.setBackgroundResource(R.drawable.code_background)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    dropSound?.start()
                    val clipData = event.clipData
                    val draggedText = clipData.getItemAt(0).text
                    val currentCode = binding.codeSnippet.text.toString()
                    val placeholder = "░░░░░░░░░"
                    binding.codeSnippet.text = currentCode.replace(placeholder, draggedText.toString())
                    v.setBackgroundResource(R.drawable.code_background)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.setBackgroundResource(R.drawable.code_background)
                    true
                }
                else -> false
            }
        }
    }

    private fun checkSolution() {
        val currentCode = binding.codeSnippet.text.toString()
        val isCorrect = currentCode.contains(questions[currentQuestionIndex].correctAnswer)

        if (isCorrect) {
            correctAnswers++
            earnedCoins += 10 - (hintsUsed * 2) // Deduct 2 coins per hint used
            hintsUsed = 0
            binding.tvCoins.text = "Coins: $earnedCoins"
            Toast.makeText(context, "Correct! +${10 - (hintsUsed * 2)} coins", Toast.LENGTH_SHORT).show()

            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                showQuestion(currentQuestionIndex)
            } else {
                navigateToResults()
            }
        } else {
            Toast.makeText(context, "Incorrect! Try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetCode() {
        showQuestion(currentQuestionIndex)
        hintsUsed = 0
    }



    private fun flashView(view: View) {
        // Store the original background
        val originalBackground = view.background

        // Set temporary yellow background
        view.setBackgroundColor(Color.YELLOW)

        // Return to original background after 2 seconds
        view.postDelayed({
            view.background = originalBackground
        }, 2000)
    }

    private fun navigateToResults() {
        timer.cancel()
        val bundle = Bundle().apply {
            putInt("earnedCoins", earnedCoins)
            putInt("correctAnswers", correctAnswers)
            putInt("totalQuestions", questions.size)
        }
        findNavController().navigate(
            R.id.action_completetheCodeCppFragment_to_resultFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        dragSound?.release()
        dropSound?.release()
        _binding = null
    }

    data class CodeQuestion(
        val partialCode: String,
        val correctAnswer: String,
        val options: List<String> // Should have exactly 3 options (1 correct + 2 incorrect)
    )
}