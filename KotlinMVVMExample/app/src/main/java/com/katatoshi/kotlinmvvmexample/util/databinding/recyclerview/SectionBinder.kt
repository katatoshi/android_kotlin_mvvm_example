package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import java.lang.ref.WeakReference

class SectionBinder<T, U>(
        val section: Section<T, U>,
        val headerVariableLayoutPair: VariableLayoutPair,
        val itemVariableLayoutPair: VariableLayoutPair
) {

    private var onListChangedCallback: OnListChangedCallback? = null

    fun addOnListChangedCallback(adapter: RecyclerView.Adapter<*>) {
        if (onListChangedCallback != null) {
            return
        }

        section.addOnListChangedCallback(OnListChangedCallback(adapter))
    }

    fun removeOnListChangedCallback() {
        if (onListChangedCallback == null) {
            return
        }

        section.removeOnListChangedCallback()
    }

    private class OnListChangedCallback(adapter: RecyclerView.Adapter<*>) : ObservableList.OnListChangedCallback<ObservableList<*>>() {

        private val adapterWeakReference = WeakReference(adapter)

        override fun onChanged(sender: ObservableList<*>?) {
            adapterWeakReference.get()?.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<*>?, positionStart: Int, itemCount: Int) {
            adapterWeakReference.get()?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<*>?, positionStart: Int, itemCount: Int) {
            adapterWeakReference.get()?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<*>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            adapterWeakReference.get()?.notifyItemMoved(fromPosition, toPosition) // TODO itemCount != 1 のときもこれでいいのか考える
        }

        override fun onItemRangeRemoved(sender: ObservableList<*>?, positionStart: Int, itemCount: Int) {
            adapterWeakReference.get()?.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }
}