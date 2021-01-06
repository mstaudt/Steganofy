package com.braffdev.steganofy.ui.common.file.picker

import android.net.Uri
import androidx.lifecycle.Observer
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`

class FilePickerViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: FilePickerViewModel

    @Test
    fun testInitialize() {
        // GIVEN
        val mimeType = "image/jpeg"
        val pickerTextRes = 1

        // WHEN
        viewModel.initialize(mimeType, pickerTextRes)
    }

    @Test
    fun testSelectFileClicked() {
        // GIVEN
        testInitialize()
        val observer: Observer<Any> = mock()
        viewModel.liveDataIntent.observeForever(observer)

        // WHEN
        viewModel.selectFileClicked()

        // THEN
        verify(observer).onChanged(anyOrNull())
    }

    @Test
    fun testOnFileUriChanged() {
        // GIVEN
        val fileUri: Uri = mock()
        val fileName = "test.jpeg"
        val fileInfo = "image/jpeg, 123 KB"
        `when`(fileService.getFileName(any())).thenReturn(fileName)
        `when`(fileService.formatFileInfo(any())).thenReturn(fileInfo)


        // WHEN
        viewModel.onFileUriChanged(fileUri)

        // THEN
        assertThat(viewModel.liveDataFileName.value).isEqualTo(fileName)
        assertThat(viewModel.liveDataFileInfo.value).isEqualTo(fileInfo)
    }
}