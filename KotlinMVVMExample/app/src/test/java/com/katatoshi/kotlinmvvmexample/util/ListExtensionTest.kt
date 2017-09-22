package com.katatoshi.kotlinmvvmexample.util

import com.nhaarman.mockito_kotlin.*
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class ListExtensionTest {

    @RunWith(Enclosed::class)
    class replace関数のテスト {

        class 置き換え先のリストのサイズが置き換え元のリストのサイズよりも小さい場合 {

            private lateinit var dstList: MutableList<String>

            private lateinit var srcList: List<String>

            @Before
            fun setUp() {
                dstList = mutableListOf("apple", "banana", "cherry")
                srcList = listOf("Taro", "Jiro", "Saburo", "Shiro", "Goro")
            }

            @Test
            fun 実行後の置き換え先のリストのサイズは置き換え元のリストのサイズと等しい() {
                dstList.replace(srcList)

                assertThat(dstList, hasSize(5))
            }

            @Test
            fun 実行後の置き換え先のリストの要素は置き換え元のリストのサイズと順番も含めて等しい() {
                dstList.replace(srcList)

                assertThat(dstList, contains("Taro", "Jiro", "Saburo", "Shiro", "Goro"))
            }

            @Test
            fun 実行中の置き換え先のリストのsetメソッドの呼び出し回数は3回でaddメソッドの呼び出し回数は2回でremoveAtメソッドは一度も呼び出されない() {
                val mock = mock<MutableList<String>> {
                    on { size } doReturn 3
                }

                mock.replace(srcList)

                verify(mock, times(3)).set(any(), any())
                verify(mock, times(2)).add(any())
                verify(mock, never()).removeAt(any())
            }
        }

        class 置き換え先のリストのサイズが置き換え元のリストのサイズよりも大きい場合 {

            private lateinit var dstList: MutableList<String>

            private lateinit var srcList: List<String>

            @Before
            fun setUp() {
                dstList = mutableListOf("apple", "banana", "cherry", "durian", "elderberry")
                srcList = listOf("Taro", "Jiro", "Saburo")
            }

            @Test
            fun 実行後の置き換え先のリストのサイズは置き換え元のリストのサイズと等しい() {
                dstList.replace(srcList)

                assertThat(dstList, hasSize(3))
            }

            @Test
            fun 実行後の置き換え先のリストの要素は置き換え元のリストのサイズと順番も含めて等しい() {
                dstList.replace(srcList)

                assertThat(dstList, contains("Taro", "Jiro", "Saburo"))
            }

            @Test
            fun 実行中の置き換え先のリストのsetメソッドの呼び出し回数は3回でremoveAtメソッドの呼び出し回数は2回でaddメソッドは一度も呼び出されない() {
                val mock = mock<MutableList<String>> {
                    on { size } doReturn 5
                }

                mock.replace(srcList)

                verify(mock, times(3)).set(any(), any())
                verify(mock, times(2)).removeAt(any())
                verify(mock, never()).add(any())
            }
        }

        class 置き換え先のリストのサイズが置き換え元のリストのサイズよりと等しい場合 {

            private lateinit var dstList: MutableList<String>

            private lateinit var srcList: List<String>

            @Before
            fun setUp() {
                dstList = mutableListOf("apple", "banana", "cherry", "durian", "elderberry")
                srcList = listOf("Taro", "Jiro", "Saburo", "Shiro", "Goro")
            }

            @Test
            fun 実行後の置き換え先のリストのサイズは置き換え元のリストのサイズと等しい() {
                dstList.replace(srcList)

                assertThat(dstList, hasSize(5))
            }

            @Test
            fun 実行後の置き換え先のリストの要素は置き換え元のリストのサイズと順番も含めて等しい() {
                dstList.replace(srcList)

                assertThat(dstList, contains("Taro", "Jiro", "Saburo", "Shiro", "Goro"))
            }

            @Test
            fun 実行中の置き換え先のリストのsetメソッドの呼び出し回数は5回でaddメソッドとremoveAtメソッドは一度も呼び出されない () {
                val mock = mock<MutableList<String>> {
                    on { size } doReturn 5
                }

                mock.replace(srcList)

                verify(mock, times(5)).set(any(), any())
                verify(mock, never()).add(any())
                verify(mock, never()).removeAt(any())
            }
        }
    }
}