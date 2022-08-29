package com.yallina.myapplication.domain.model

sealed class Result<out R> {
    class Success<out T>() : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
