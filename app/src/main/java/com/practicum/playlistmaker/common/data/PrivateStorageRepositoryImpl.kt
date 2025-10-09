package com.practicum.playlistmaker.common.data

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.common.domain.PrivateStorageRepository
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PrivateStorageRepositoryImpl(
    private val application: Application
) : PrivateStorageRepository {
    override fun savePlaylistLabel(uri: Uri, playlistName: String): Uri {
        val filePath = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val uuid = UUID.randomUUID()
        val file = File(filePath, "$uuid.jpg")
        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }
}