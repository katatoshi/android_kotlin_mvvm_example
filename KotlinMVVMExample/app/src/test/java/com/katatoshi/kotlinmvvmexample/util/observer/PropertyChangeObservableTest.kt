package com.katatoshi.kotlinmvvmexample.util.observer

import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PropertyChangeObservableTest {

    /**
     * protected な PropertyChangeObservable#firePropertyChange を呼ぶための PropertyChangeObservable のサブクラス。
     */
    private class ClassUnderTest : PropertyChangeObservable() {

        var propertyUnderTest: Int = 0
            set(value) {
                field = value
                firePropertyChange(ClassUnderTest::propertyUnderTest.name)
            }
    }

    private lateinit var sut: ClassUnderTest

    /**
     * プロパティの変更を観測すひと。検証用。
     */
    private lateinit var propertyChangeObserver: PropertyChangeObserver

    /**
     * プロパティ名から観測回数へのマップ。検証用。
     */
    private lateinit var propertyNameToCountPropertyChangeMap: MutableMap<String, Int>

    @Before
    fun setUp() {
        sut = ClassUnderTest()

        propertyNameToCountPropertyChangeMap = mutableMapOf()

        propertyChangeObserver = PropertyChangeObserver { propertyName ->
            propertyNameToCountPropertyChangeMap[propertyName]?.let {
                // 変更のあったプロパティ名がすでにマップに存在するので、観測回数を 1 増やす。
                propertyNameToCountPropertyChangeMap[propertyName] = it + 1
            } ?: {
                // 変更のあったプロパティ名がマップに存在しない、すなわち最初の観測なので、観測回数を 1 とする。
                propertyNameToCountPropertyChangeMap[propertyName] = 1
            }()
        }

        sut.addPropertyChangeObserver(propertyChangeObserver)
    }

    @Test
    fun propertyUnderTestプロパティに値を1回セットするとpropertyUnderTestプロパティの変更を1回観測する() {
        sut.propertyUnderTest = 1

        assertThat(propertyNameToCountPropertyChangeMap[ClassUnderTest::propertyUnderTest.name], `is`(1))
    }

    @Test
    fun propertyUnderTestプロパティに値を2回セットするとpropertyUnderTestプロパティの変更を2回観測する() {
        sut.propertyUnderTest = 1
        sut.propertyUnderTest = 2

        assertThat(propertyNameToCountPropertyChangeMap[ClassUnderTest::propertyUnderTest.name], `is`(2))
    }

    @Test
    fun propertyUnderTestプロパティに値を3回セットするとpropertyUnderTestプロパティの変更を3回観測する() {
        sut.propertyUnderTest = 1
        sut.propertyUnderTest = 2
        sut.propertyUnderTest = 3

        assertThat(propertyNameToCountPropertyChangeMap[ClassUnderTest::propertyUnderTest.name], `is`(3))
    }

    @After
    fun tearDown() {
        sut.removePropertyChangeObserver(propertyChangeObserver)
    }
}