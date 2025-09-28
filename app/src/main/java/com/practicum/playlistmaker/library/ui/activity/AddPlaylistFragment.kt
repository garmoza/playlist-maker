package com.practicum.playlistmaker.library.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.common.ui.dpToPx
import com.practicum.playlistmaker.databinding.FragmentAddPlaylistBinding
import java.io.File
import java.io.FileOutputStream

class AddPlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistLabelUri: Uri? = null

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

        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                playlistLabelUri = uri
                Glide.with(this)
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(dpToPx(8F, requireContext())))
                    .into(binding.playlistLabel)
            } else {
                Log.i("AddPlaylistFragment", "No media selected")
            }
        }

        binding.playlistLabel.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreate.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString()
            if (playlistLabelUri != null && playlistName.isNotBlank()) {
                saveImageToAppPrivateStorage(
                    uri = playlistLabelUri!!,
                    playlistName = playlistName
                )
            }
        }
    }

    private fun saveImageToAppPrivateStorage(uri: Uri, playlistName: String) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "${playlistName}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}