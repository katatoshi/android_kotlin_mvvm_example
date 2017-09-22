package com.katatoshi.kotlinmvvmexample.util.observer

import java.beans.PropertyChangeSupport

/**
 * プロパティの変更を観測されうるひと。
 */
open class PropertyChangeObservable {

    private val propertyChangeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeObserver(propertyChangeObserver: PropertyChangeObserver) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeObserver)
    }

    fun removePropertyChangeObserver(propertyChangeObserver: PropertyChangeObserver) {
        propertyChangeSupport.removePropertyChangeListener(propertyChangeObserver)
    }

    protected fun firePropertyChange(propertyName: String) {
        propertyChangeSupport.firePropertyChange(propertyName, null, null)
    }
}
