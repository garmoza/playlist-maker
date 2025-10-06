package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.ui.dpToPx
import com.practicum.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.practicum.playlistmaker.library.ui.view_model.AddPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment : Fragment() {

    private val viewModel by viewModel<AddPlaylistViewModel>()

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            binding.buttonCreate.isEnabled = state.isReadyToAdd
        }

        binding.toolbar.setNavigationOnClickListener {
            onNavigateUp()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(dpToPx(8F, requireContext())))
                    .into(binding.playlistLabel)
                viewModel.onPickPlaylistLabel(uri)
            } else {
                Log.i("AddPlaylistFragment", "No media selected")
            }
        }

        binding.playlistLabel.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreate.setOnClickListener {
            viewModel.addPlaylist()
            val playlistName = viewModel.getLiveData().value?.playlistName!!
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                PLAYLIST_CREATED_LIVE_DATA_KEY, playlistName
            )
            findNavController().navigateUp()
        }

        binding.playlistNameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }

        binding.playlistNameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onDescriptionChanged(text.toString())
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogTheme)
            .setTitle(R.string.finish_creating_the_playlist)
            .setMessage(R.string.all_unsaved_data_will_be_lost)
            .setNeutralButton(R.string.cancel) { _, _ ->
                // nothing
            }.setPositiveButton(R.string.finish) { _, _ ->
                findNavController().navigateUp()
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onNavigateUp()
                }
            }
        )
    }

    private fun onNavigateUp() {
        if (viewModel.getLiveData().value?.isStartedFilling == true) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PLAYLIST_CREATED_LIVE_DATA_KEY = "playlist_created"
    }
}