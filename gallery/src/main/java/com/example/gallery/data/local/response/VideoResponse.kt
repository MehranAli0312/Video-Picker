package com.example.gallery.data.local.response

sealed class VideoResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T> : VideoResponse<T>()
    class Success<T>(data: T?) : VideoResponse<T>(data = data)
    class Error<T>(errorMessage: String) : VideoResponse<T>(errorMessage = errorMessage)
}