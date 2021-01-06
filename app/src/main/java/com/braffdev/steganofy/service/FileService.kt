package com.braffdev.steganofy.service

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.braffdev.steganofy.R
import org.apache.commons.io.IOUtils
import java.io.BufferedOutputStream
import java.io.File

class FileService(private val applicationContext: Context) {

    /**
     * Retrieves the file name of the file denoted by the given uri
     *
     * @param uri the uri to get the file name from
     * @return the file name
     */
    fun getFileName(uri: Uri): String {
        val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)!!
        return cursor.use {
            cursor.moveToFirst()
            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }

    /**
     * Retrieves the file size of the file denoted by the given uri
     *
     * @param uri the uri to get the file size from
     * @return the file size in bytes
     */
    fun getFileSize(uri: Uri): Long {
        val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)!!
        return cursor.use {
            cursor.moveToFirst()
            cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
        }
    }

    /**
     * Retrieves the file mime type of the file denoted by the given uri
     *
     * @param uri the uri to get the file mime type from
     * @return the file mime type, if found
     */
    fun getFileType(uri: Uri): String? {
        return applicationContext.contentResolver.getType(uri)
    }

    /**
     * Retrieves the file extension of the file denoted by the given uri
     *
     * @param uri the uri to get the file extension from
     * @return the file extension, such as "jpg"
     */
    fun getFileExtension(mimeType: String): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

    /**
     * Creates a human readable text that contains the file mime type and size
     *
     * @param uri the uri to get the file info from
     * @return the file info
     */
    fun formatFileInfo(uri: Uri): String {
        val fileType = getFileType(uri)
        val fileSize = formatFileSize(uri)
        return formatFileInfo(fileType!!, fileSize)
    }

    /**
     * Creates a human readable text that contains the file mime type and size
     *
     * @param fileType the mime type
     * @param fileSize the size in bytes
     * @return the file info
     */
    fun formatFileInfo(fileType: String, fileSize: String): String {
        return applicationContext.getString(R.string.file_info, fileType, fileSize)
    }

    /**
     * Reads the bytes of the file denoted by the given uri.
     *
     * @param uri the file to read
     * @return the bytes of the file
     */
    fun getByteArray(uri: Uri): ByteArray {
        val stream = applicationContext.contentResolver.openInputStream(uri)
        return stream.use { IOUtils.toByteArray(stream) }
    }

    /**
     * Creates a new file in the internal storage of this app. The file is created in a folder that allows the file to be shared with other apps.
     *
     * @param mimeType the mime type of the file to create
     * @param bytes the content of the file to create
     * @return a uri denoting the file that was created
     */
    fun createTemporaryFile(mimeType: String, bytes: ByteArray): Uri {
        val outputDir = File(applicationContext.filesDir, "external_files")
        outputDir.mkdirs()

        val outputFile = File.createTempFile("output", "." + getFileExtension(mimeType), outputDir)
        val temporaryFileUri =
            FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".provider", outputFile)

        val temporaryFileOutputStream = BufferedOutputStream(applicationContext.contentResolver.openOutputStream(temporaryFileUri))
        temporaryFileOutputStream.use { temporaryFileOutputStream.write(bytes) }

        return temporaryFileUri
    }

    /**
     * Deletes the specified file
     *
     * @param uri the file to delete. The file has to be placed in the internal storage of this app.
     */
    fun deleteTemporaryFile(uri: Uri) {
        applicationContext.contentResolver.delete(uri, null, null)
    }

    private fun formatFileSize(uri: Uri): String {
        return formatFileSize(getFileSize(uri))
    }

    private fun formatFileSize(fileSizeInBytes: Long): String {
        return Formatter.formatShortFileSize(applicationContext, fileSizeInBytes)
    }
}
