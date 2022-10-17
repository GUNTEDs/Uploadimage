package com.example.uploadimage

data class UploadResponse(
    val error: Boolean,
    val message: String,
    val image: String
)