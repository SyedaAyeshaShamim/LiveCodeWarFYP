package com.example.test

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.FragmentCodeJigsawCppBinding
import com.google.android.flexbox.FlexboxLayoutManager

class CodeJigsawCppFragment : Fragment() {
    private var _binding: FragmentCodeJigsawCppBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: JigsawPiecesAdapter

    // Game state
    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 150000 // 2.5 minutes
    private var currentQuestionIndex = 0
    private var earnedCoins = 0
    private var correctAnswers = 0
    private var currentPuzzleSolved = false

    // Questions
    private val allPuzzles = listOf(
        listOf(
            Snip(1, "#include <iostream>", 0, 0),
            Snip(2, "using namespace std;", 1, 1),
            Snip(3, "int main() {", 2, 2),
            Snip(4, "    cout << \"Hello World!\";", 3, 3),
            Snip(5, "    return 0;", 4, 4),
            Snip(6, "}", 5, 5)
        ),
        listOf(
            Snip(1, "#include <iostream>", 0, 0),
            Snip(2, "using namespace std;", 1, 1),
            Snip(3, "int main() {", 2, 2),
            Snip(4, "    int x = 5;", 3, 3),
            Snip(5, "    int y = 10;", 4, 4),
            Snip(6, "    cout << x + y;", 5, 5),
            Snip(7, "    return 0;", 6, 6),
            Snip(8, "}", 7, 7)
        )
    )
    private lateinit var puzzles: List<List<Snip>>

    // Helper classes
    private inner class JigsawPiecesAdapter(
        private var items: List<Snip>,
        private val onOrderChanged: (List<Snip>) -> Unit
    ) : RecyclerView.Adapter<JigsawPiecesAdapter.ViewHolder>() {

        var currentItems: List<Snip> = items
            private set

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.codeSnippetText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.jigsaw_piece_bg, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = items[position].text
        }

        override fun getItemCount() = items.size

        fun onItemMoved(from: Int, to: Int) {
            items = items.toMutableList().apply {
                val movedItem = removeAt(from)
                add(to, movedItem)
                forEachIndexed { index, snip -> snip.currentPosition = index }
            }
            currentItems = items
            notifyItemMoved(from, to)
            onOrderChanged(items)
        }
    }

    private inner class DragCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                0
            )
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        override fun isLongPressDragEnabled() = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        puzzles = allPuzzles.shuffled().take(5)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeJigsawCppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPuzzle(puzzles[currentQuestionIndex])
        startTimer()

        binding.checkButton.setOnClickListener { checkSolution() }
        binding.submitButton.setOnClickListener {
            if (currentPuzzleSolved) proceedToNextQuestion()
            else Toast.makeText(context, "Solve correctly first!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupPuzzle(puzzle: List<Snip>) {
        currentPuzzleSolved = false
        val shuffled = puzzle.shuffled().mapIndexed { i, snip -> snip.copy(currentPosition = i) }

        adapter = JigsawPiecesAdapter(shuffled) { updated ->
            binding.codePreview.text = updated.sortedBy { it.currentPosition }
                .joinToString("\n") { it.text }
        }

        binding.jigsawPiecesRecycler.apply {
            layoutManager = FlexboxLayoutManager(requireContext())
            adapter = this@CodeJigsawCppFragment.adapter
            ItemTouchHelper(DragCallback()).attachToRecyclerView(this)
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millis: Long) {
                timeLeftInMillis = millis
                binding.timerText.text = "%02d:%02d".format(millis/1000/60, millis/1000%60)
            }
            override fun onFinish() = navigateToResults()
        }.start()
    }

    private fun checkSolution() {
        val isCorrect = adapter.currentItems.all {
            it.currentPosition == it.correctPosition
        }

        if (isCorrect) {
            currentPuzzleSolved = true
            correctAnswers++
            earnedCoins += 10
            binding.tvCoin.text = "$earnedCoins💰"
            Toast.makeText(context, "Correct! +10 coins", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Incorrect! Try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun proceedToNextQuestion() {
        if (++currentQuestionIndex < puzzles.size) {
            setupPuzzle(puzzles[currentQuestionIndex])
        } else {
            navigateToResults()
        }
    }

    private fun showHint() {
        val firstLine = puzzles[currentQuestionIndex].first { it.correctPosition == 0 }.text
        Toast.makeText(context, "Start with: $firstLine", Toast.LENGTH_LONG).show()
    }

    private fun navigateToResults() {
        findNavController().navigate(
            R.id.action_codeJigsawCppFragment_to_resultFragment,
            Bundle().apply {
                putInt("coins", earnedCoins)
                putInt("correct", correctAnswers)
                putInt("total", puzzles.size)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        _binding = null
    }

    data class Snip(
        val id: Int,
        val text: String,
        val correctPosition: Int,
        var currentPosition: Int
    )
}