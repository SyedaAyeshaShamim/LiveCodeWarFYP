package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.test.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var questionViewModel: QuestionViewModel
    private var currentQuestion: model? = null

    // Use savedInstanceState to preserve state
    private var coins: Int = 0
    private var questionCount: Int = 0
    private val maxQuestions: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state if available
        if (savedInstanceState != null) {
            coins = savedInstanceState.getInt("coins", 0)
            questionCount = savedInstanceState.getInt("questionCount", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)

        // Update UI with current state
        binding.progressBar.progress = (questionCount * 100) / maxQuestions

        binding.subButton.setOnClickListener {
            val userAnswer = binding.ansEditText.text.toString().trim()
            if (userAnswer.equals(currentQuestion?.correctAnswer, ignoreCase = true)) {
                coins += 10
                Toast.makeText(requireContext(),
                    "Correct! You earned 10 coins.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(),
                    "Incorrect answer :(",
                    Toast.LENGTH_SHORT
                ).show()
            }

            questionCount++

            if (questionCount >= maxQuestions) {
                // Navigate to result fragment with coins
                val bundle = Bundle().apply {
                    putInt("coins", coins)
                }
                findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundle)
            } else {
                // Just load next question without recreating fragment
                loadNextQuestion()
            }
        }

        loadNextQuestion()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("coins", coins)
        outState.putInt("questionCount", questionCount)
    }

    private fun loadNextQuestion() {
        questionViewModel.getRandomQuestion("C++")
            .observe(viewLifecycleOwner, Observer { questionsCplus ->
                currentQuestion = questionsCplus
                binding.quesTextView3.text = questionsCplus?.questionText
                binding.ansEditText.text?.clear()
                binding.progressBar.progress = (questionCount * 100) / maxQuestions
            })
    }
}