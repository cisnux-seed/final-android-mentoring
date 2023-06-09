package dev.cisnux.dicodingmentoring.ui.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable

@Composable
fun rememberGalleryLauncher(
    onSuccess: (Uri) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                onSuccess(selectedImg)
            }
        }
    )
}