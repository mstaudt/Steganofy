package com.braffdev.steganofy.ui.common.file.saver

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.FileService
import org.apache.commons.io.IOUtils

class FileSaverViewModel(val applicationContext: Context, val fileService: FileService, val executionService: ExecutionService) {

    val liveDataIntentForResult = MutableLiveData<Intent>()
    val liveDataIntent = MutableLiveData<Intent>()

    lateinit var temporaryFileUri: Uri
    lateinit var mimeType: String
    var buttonTextRes: Int = -1

    fun initialize(temporaryFileUri: Uri, mimeType: String, @StringRes buttonTextRes: Int) {
        this.temporaryFileUri = temporaryFileUri
        this.mimeType = mimeType
        this.buttonTextRes = buttonTextRes
    }

    fun saveFileClicked() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.type = mimeType
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_TITLE, "output." + fileService.getFileExtension(mimeType))
        liveDataIntentForResult.value = Intent.createChooser(intent, applicationContext.getString(buttonTextRes))
    }

    fun shareFileClicked() {
        executionService.executeInBackground {
            val intentShareFile = Intent(Intent.ACTION_SEND)
            intentShareFile.type = mimeType
            intentShareFile.putExtra(Intent.EXTRA_STREAM, temporaryFileUri)
            val intentChooser = Intent.createChooser(intentShareFile, applicationContext.getString(R.string.file_share))

            // Grant permissions to URI
            val resInfoList = applicationContext.packageManager.queryIntentActivities(intentChooser, PackageManager.MATCH_DEFAULT_ONLY)
            val permissions = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                applicationContext.grantUriPermission(packageName, temporaryFileUri, permissions)
            }

            // Send intent
            liveDataIntent.postValue(intentChooser)
        }
    }

    fun onFileUriChanged(uri: Uri) {
        writeFile(uri)
    }

    private fun writeFile(uri: Uri) {
        executionService.executeInBackground {
            val contentResolver = applicationContext.contentResolver
            val inputStream = contentResolver.openInputStream(temporaryFileUri)
            val outputStream = contentResolver.openOutputStream(uri)

            inputStream.use { outputStream.use { IOUtils.copy(inputStream, outputStream) } }
        }
    }
}
