package com.practicum.playlistmaker.main.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClickListener: OnClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
            }
        }
        binding.buttonSearch.setOnClickListener(onClickListener)

        binding.buttonLibrary.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_libraryFragment)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}