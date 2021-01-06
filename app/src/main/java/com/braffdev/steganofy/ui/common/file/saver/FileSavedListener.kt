package com.braffdev.steganofy.ui.common.file.saver

import android.net.Uri

interface FileSavedListener {

    fun onFileSaved(uri: Uri)
}