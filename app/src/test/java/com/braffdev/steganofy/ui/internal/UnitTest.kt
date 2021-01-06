package com.braffdev.steganofy.ui.internal

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.braffdev.steganofy.lib.service.HideService
import com.braffdev.steganofy.lib.service.RevealService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.service.PayloadService
import com.braffdev.steganofy.ui.koin.appModule
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class UnitTest : KoinTest {

    @Mock
    lateinit var applicationContext: Context

    @Mock
    lateinit var hideService: HideService

    @Mock
    lateinit var revealService: RevealService

    @Mock
    lateinit var fileService: FileService

    @Mock
    lateinit var payloadService: PayloadService

    @Mock
    lateinit var bitmapService: BitmapService

    @Spy
    lateinit var executionService: TestExecutionService

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(appModule) }
}