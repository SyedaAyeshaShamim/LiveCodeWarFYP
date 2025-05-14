package com.example.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.droidsonroids.gif.GifImageButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoadingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loading, container, false)

        // 🎯 Find the GifImageButton
        val gifButton = view.findViewById<GifImageButton>(R.id.gifImageButton)

        // 🎯 Set click listener
        gifButton.setOnClickListener {
            // Navigate to MiniGamesFragment
            findNavController().navigate(R.id.action_loadingFragment_to_minigameFragment)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoadingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
