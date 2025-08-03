package com.practicum.playlistmaker.main.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.library.ui.activity.LibraryFragment
import com.practicum.playlistmaker.search.ui.activity.SearchFragment
import com.practicum.playlistmaker.settings.ui.activity.SettingsFragment

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
                parentFragmentManager.commit {
                    replace(R.id.fragment_container_view, SearchFragment.newInstance())
                    addToBackStack("SearchFragment")
                }
            }
        }
        binding.buttonSearch.setOnClickListener(onClickListener)

        binding.buttonLibrary.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container_view, LibraryFragment.newInstance())
                addToBackStack("LibraryFragment")
            }
        }

        binding.buttonSettings.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container_view, SettingsFragment.newInstance())
                addToBackStack("SettingsFragment")
            }
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