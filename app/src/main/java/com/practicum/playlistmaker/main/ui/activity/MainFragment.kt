package com.practicum.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.library.ui.activity.LibraryActivity
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingsActivity

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
                val displayIntent = Intent(context, SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }
        binding.buttonSearch.setOnClickListener(onClickListener)

        binding.buttonLibrary.setOnClickListener {
            val displayIntent = Intent(context, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        binding.buttonSettings.setOnClickListener {
            val displayIntent = Intent(context, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}