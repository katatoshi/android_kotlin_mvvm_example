package com.katatoshi.kotlinmvvmexample.util.observer

import java.beans.PropertyChangeEvent

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class PropertyChangeObserverTest {

    @Test
    fun propertyChangeメソッドのテスト() {
        val propertyName: String = "propertyName"

        val sut = PropertyChangeObserver { assertThat(it, `is`(propertyName)) }

        sut.propertyChange(PropertyChangeEvent(Unit, propertyName, null, null))
    }
}