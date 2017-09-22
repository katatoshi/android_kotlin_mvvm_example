package com.katatoshi.kotlinmvvmexample.util

/**
 * Ok か Error のどちらか一方を表す型。
 */
sealed class Result<T, E> {
    class Ok<T, E>(val ok: T) : Result<T, E>()

    class Error<T, E>(val error: E) : Result<T, E>()

    fun <U> flatMap(f: (T) -> Result<U, E>): Result<U, E> = when (this) {
        is Result.Ok -> f(this.ok)
        is Result.Error -> Result.Error<U, E>(this.error)
    }
}