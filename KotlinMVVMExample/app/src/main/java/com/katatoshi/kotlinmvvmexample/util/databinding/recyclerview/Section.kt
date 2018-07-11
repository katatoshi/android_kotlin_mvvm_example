package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.databinding.ObservableList

class Section<T, U>(val header: T, private val itemList: ObservableList<U>) {

    var onListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<U>>? = null

    fun getItemList() = itemList

    fun addOnListChangedCallback(onListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<*>>) {
        this.onListChangedCallback = object : ObservableList.OnListChangedCallback<ObservableList<U>>() {

            override fun onChanged(sender: ObservableList<U>?) {
                onListChangedCallback.onChanged(sender)
            }

            override fun onItemRangeChanged(sender: ObservableList<U>?, positionStart: Int, itemCount: Int) {
                onItemRangeChanged(sender, positionStart, itemCount)
            }

            override fun onItemRangeInserted(sender: ObservableList<U>?, positionStart: Int, itemCount: Int) {
                onItemRangeInserted(sender, positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<U>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                onItemRangeMoved(sender, fromPosition, toPosition, itemCount)
            }

            override fun onItemRangeRemoved(sender: ObservableList<U>?, positionStart: Int, itemCount: Int) {
                onItemRangeRemoved(sender, positionStart, itemCount)
            }
        }
        itemList.addOnListChangedCallback(this.onListChangedCallback)
    }

    fun removeOnListChangedCallback() {
        itemList.removeOnListChangedCallback(onListChangedCallback)
        onListChangedCallback = null
    }
}
