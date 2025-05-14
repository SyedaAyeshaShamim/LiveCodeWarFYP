package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.test.databinding.FragmentLanguageBinding

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fillintheblank.setOnClickListener {
            findNavController().navigate(R.id.action_languageFragment_to_gameFragment)
        }
        binding.findthebug.setOnClickListener{
            findNavController().navigate(R.id.action_languageFragment_to_findthebugcppFragment)
        }

        binding.completethecode.setOnClickListener{
            findNavController().navigate(R.id.action_languageFragment_to_completetheCodeCppFragment)
        }
        binding.multiplechoice.setOnClickListener{
            findNavController().navigate(R.id.action_languageFragment_to_multipleChoiceCplusFragment)
        }
        binding.codejigsaw.setOnClickListener{
            findNavController().navigate(R.id.action_languageFragment_to_codeJigsawCppFragment)
        }
        binding.predictthecode.setOnClickListener{
            findNavController().navigate(R.id.action_languageFragment_to_predictTheOutputCppFragment)
        }
        // You can add listeners for other buttons too here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}