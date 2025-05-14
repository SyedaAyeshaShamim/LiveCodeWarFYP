package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JigsawPiecesAdapter(
    private var items: List<CodeSnippet>,
    private val onOrderChanged: (List<CodeSnippet>) -> Unit
) : RecyclerView.Adapter<JigsawPiecesAdapter.ViewHolder>() {

    // Track current items with private setter
    var currentItems: List<CodeSnippet> = items
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

    override fun getItemCount(): Int = items.size

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        items = items.toMutableList().apply {
            val movedItem = removeAt(fromPosition)
            add(toPosition, movedItem)
            // Update current positions
            forEachIndexed { index, snippet ->
                snippet.currentPosition = index
            }
        }
        currentItems = items
        notifyItemMoved(fromPosition, toPosition)
        onOrderChanged(items)
    }

    fun updateItems(newItems: List<CodeSnippet>) {
        items = newItems
        currentItems = newItems
        notifyDataSetChanged()
    }
}