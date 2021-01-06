package com.braffdev.steganofy.service

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExecutionServiceTest {

    private val executionService = ExecutionService()

    @Test
    fun testExecuteInBackground() {
        // GIVEN
        val runnable: Runnable = mock()

        // WHEN
        executionService.executeInBackground(runnable)

        // THEN
        Thread.sleep(200)
        verify(runnable).run()
    }
}