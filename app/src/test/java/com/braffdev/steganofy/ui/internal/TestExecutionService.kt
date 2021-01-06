package com.braffdev.steganofy.ui.internal

import com.braffdev.steganofy.service.ExecutionService

class TestExecutionService : ExecutionService() {

    override fun executeInBackground(runnable: Runnable) {
        // Run the code synchronous. This makes assertions easier and less error-prone
        runnable.run()
    }
}