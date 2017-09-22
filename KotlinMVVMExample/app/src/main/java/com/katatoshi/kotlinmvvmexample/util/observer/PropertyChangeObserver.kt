package com.katatoshi.kotlinmvvmexample.util.observer

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * PropertyChangeObservable を継承したクラスのプロパティの変更を観測するひと。
 */
class PropertyChangeObserver(private val onPropertyChanged: (String) -> Unit) : PropertyChangeListener {
    override fun propertyChange(evt: PropertyChangeEvent?) {
        evt?.let { onPropertyChanged(it.propertyName) }
    }
}
