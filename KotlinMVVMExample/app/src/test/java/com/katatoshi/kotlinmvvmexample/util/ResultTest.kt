package com.katatoshi.kotlinmvvmexample.util

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class ResultTest {

    @Test
    fun Int型の引数を3f倍した値のOkを返す関数で値が2のOkをflatMapすると値が6fのOkである() {
        val result = Result.Ok<Int, String>(2).flatMap { x -> Result.Ok<Float, String>(3f * x) }

        when (result) {
            is Result.Ok -> assertThat(result.ok, `is`(6f))
            is Result.Error -> fail()
        }
    }

    @Test
    fun Errorを返す関数でOkをflatMapすると関数の返すErrorの値でErrorである() {
        val result = Result.Ok<Int, String>(2).flatMap { x -> Result.Error<Float, String>("B") }

        when (result) {
            is Result.Ok -> fail()
            is Result.Error -> assertThat(result.error, `is`("B"))
        }
    }

    @Test
    fun Okを返す関数でErrorをflatMapすると最初のErrorの値でErrorである() {
        val result = Result.Error<Int, String>("A").flatMap { x -> Result.Ok<Float, String>(3f * x) }

        when (result) {
            is Result.Ok -> fail()
            is Result.Error -> assertThat(result.error, `is`("A"))
        }
    }

    @Test
    fun Errorを返す関数でErrorをflatMapすると最初のErrorの値でErrorである() {
        val result = Result.Error<Int, String>("A").flatMap { x -> Result.Error<Float, String>("B") }

        when (result) {
            is Result.Ok -> fail()
            is Result.Error -> assertThat(result.error, `is`("A"))
        }
    }
}