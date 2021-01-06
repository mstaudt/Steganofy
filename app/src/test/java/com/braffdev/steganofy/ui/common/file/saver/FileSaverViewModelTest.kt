package com.braffdev.steganofy.ui.common.file.saver

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.Observer
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import java.io.ByteArrayInputStream

class FileSaverViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: FileSaverViewModel

    @Test
    fun testInitialize() {
        viewModel.initialize(mock(), "image/jpeg", 1)
    }

    @Test
    fun testSaveFileClicked() {
        // GIVEN
        testInitialize()
        val observer: Observer<Any> = mock()
        viewModel.liveDataIntentForResult.observeForever(observer)

        // WHEN
        viewModel.saveFileClicked()

        // THEN
        verify(observer).onChanged(anyOrNull())
    }

    @Test
    fun testShareFileClicked() {
        // GIVEN
        testInitialize()
        val observer: Observer<Any> = mock()
        viewModel.liveDataIntent.observeForever(observer)
        `when`(applicationContext.packageManager).thenReturn(mock())

        // WHEN
        viewModel.shareFileClicked()

        // THEN
        verify(observer).onChanged(anyOrNull())
    }

    @Test
    fun testOnFileUriChanged() {
        // GIVEN
        testInitialize()
        val uri: Uri = mock()
        val contentResolver: ContentResolver = mock()
        `when`(applicationContext.contentResolver).thenReturn(contentResolver)
        `when`(contentResolver.openOutputStream(any())).thenReturn(mock())
        `when`(contentResolver.openInputStream(any())).thenReturn(ByteArrayInputStream("Test".toByteArray()))

        // WHEN
        viewModel.onFileUriChanged(uri)

        // THEN
        verify(contentResolver).openOutputStream(any())
        verify(contentResolver).openInputStream(any())
    }


}