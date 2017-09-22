package com.katatoshi.kotlinmvvmexample.util.databinding

import android.databinding.ObservableList

class OnLeaderListChangedCallback<R, F>(
        private val followerList: MutableList<F>,
        private val onChangedMapper: (R, F) -> F,
        private val onInsertedMapper: (R) -> F
) : ObservableList.OnListChangedCallback<ObservableList<R>>() {

    constructor(
            followerList: MutableList<F>,
            onInsertedMapper: (R) -> F
    ) : this(followerList, { r, _ -> onInsertedMapper(r) }, onInsertedMapper)

    override fun onChanged(sender: ObservableList<R>?) {
        // do nothing
    }

    override fun onItemRangeChanged(sender: ObservableList<R>?, positionStart: Int, itemCount: Int) {
        if (sender == null) {
            throw IllegalStateException()
        }

        if (followerList.size != sender.size) {
            followerList.clear()
            followerList.addAll(sender.map(onInsertedMapper))
            return
        }

        for (i in positionStart until positionStart + itemCount) {
            followerList[i] = onChangedMapper(sender[i], followerList[i])
        }
    }

    override fun onItemRangeInserted(sender: ObservableList<R>?, positionStart: Int, itemCount: Int) {
        if (sender == null) {
            throw IllegalStateException()
        }

        if (followerList.size + itemCount != sender.size) {
            followerList.clear()
            followerList.addAll(sender.map(onInsertedMapper))
            return
        }

        for (i in positionStart until positionStart + itemCount) {
            followerList.add(i, onInsertedMapper(sender[i]))
        }
    }

    override fun onItemRangeMoved(sender: ObservableList<R>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
        throw UnsupportedOperationException()
    }

    override fun onItemRangeRemoved(sender: ObservableList<R>?, positionStart: Int, itemCount: Int) {
        if (sender == null) {
            throw IllegalStateException()
        }

        if (followerList.size - itemCount != sender.size) {
            followerList.clear()
            followerList.addAll(sender.map(onInsertedMapper))
            return
        }

        repeat(itemCount) {
            followerList.removeAt(positionStart)
        }
    }
}
