package com.braffdev.steganofy.ui.common.file.picker

import android.net.Uri

interface FilePickedListener {

    fun onFileChanged(uri: Uri)
}