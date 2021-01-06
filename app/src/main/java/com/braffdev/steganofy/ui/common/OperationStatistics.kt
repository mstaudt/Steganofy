package com.braffdev.steganofy.ui.common

data class OperationStatistics(
    val totalTimeInMs: Long,
    val operationInMs: Long,
    val imageProcessingInMs: Long = totalTimeInMs - operationInMs,
)
