package com.nascenia.marketupdate.networking

sealed class ApiResponse<out T> {
    data object Loading : ApiResponse<Nothing>()
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
}