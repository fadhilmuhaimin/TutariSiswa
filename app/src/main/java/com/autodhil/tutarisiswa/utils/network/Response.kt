package com.pajaga.utils.network

sealed class Response {
    data class Changed(val data: Any): Response()
    data class Success(val succes: String): Response()
    data class Error(val error: String): Response()
}