package com.practicum.playlistmaker.library.ui.view_model

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.library.domain.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.model.AddPlaylistState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class AddPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val application: Application
) : ViewModel() {

    private val liveData = MutableLiveData(EMPTY_STATE)

    fun getLiveData(): LiveData<AddPlaylistState> = liveData

    fun addPlaylist() {
        if (liveData.value?.isReadyToAdd == true) {
            val storageUri = saveImageToAppPrivateStorage(
                uri = liveData.value?.playlistLabelUri!!,
                playlistName = liveData.value?.playlistName!!
            )
            viewModelScope.launch {
                playlistInteractor.addPlaylist(
                    Playlist(
                        name = liveData.value?.playlistName!!,
                        description = liveData.value?.playlistDescription!!,
                        labelUri = storageUri.toString()
                    )
                )
            }
        }
    }

    fun onPickPlaylistLabel(uri: Uri) {
        liveData.value = liveData.value?.copy(playlistLabelUri = uri)
    }

    fun onNameChanged(name: String) {
        liveData.value = liveData.value?.copy(playlistName = name)
    }

    fun onDescriptionChanged(description: String) {
        liveData.value = liveData.value?.copy(playlistDescription = description)
    }

    private fun saveImageToAppPrivateStorage(uri: Uri, playlistName: String): Uri {
        val filePath = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "${playlistName}.jpg")
        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }

    companion object {
        private val EMPTY_STATE = AddPlaylistState(
            playlistName = null,
            playlistDescription = null,
            playlistLabelUri = null
        )
    }
}