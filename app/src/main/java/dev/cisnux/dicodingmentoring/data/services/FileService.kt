package dev.cisnux.dicodingmentoring.data.services

import android.net.Uri
import java.io.File

interface FileService {
    suspend fun uriToFile(uri: Uri): File
    suspend fun reduceImage(file: File): File
    suspend fun createFile(): File
}