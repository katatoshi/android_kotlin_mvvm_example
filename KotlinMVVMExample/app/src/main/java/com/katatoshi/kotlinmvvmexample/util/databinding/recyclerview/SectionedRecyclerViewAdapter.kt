package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun <T> RecyclerView.bindSectionBinderList(sectionBinderList: List<SectionBinder<T, *>>) {
    val adapter = SectionedRecyclerViewAdapter(sectionBinderList)
    setAdapter(adapter)
}

private class SectionedRecyclerViewAdapter<T>(
        private var sectionBinderList: List<SectionBinder<T, *>>
) : RecyclerView.Adapter<SectionedRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    init {
        setSectionBinderList(sectionBinderList)
    }

    private var inflater: LayoutInflater? = null

    fun setSectionBinderList(sectionBinderList: List<SectionBinder<T, *>>) {
        if (this.sectionBinderList == sectionBinderList) {
            return
        }

        sectionBinderList.forEach { it.removeOnListChangedCallback() }
        notifyItemRangeRemoved(0, sectionBinderList.map { 1 + it.section.getItemList().size }.sum())

        this.sectionBinderList = sectionBinderList
        notifyItemRangeInserted(0, this.sectionBinderList.map { 1 + it.section.getItemList().size }.sum())
        this.sectionBinderList.forEach { it.addOnListChangedCallback(this) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        sectionBinderList.forEach { it.removeOnListChangedCallback() }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent?.context)
        }

        return ViewHolder(DataBindingUtil.inflate(inflater, viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var headerPosition = 0
        for (i in 0 until sectionBinderList.size) {
            if (position == headerPosition) {
                holder?.binding?.setVariable(sectionBinderList[i].headerVariableLayoutPair.variableId, sectionBinderList[i].section.header)
                holder?.binding?.executePendingBindings()
                return
            }

            if (headerPosition + 1 <= position && position < headerPosition + 1 + sectionBinderList[i].section.getItemList().size) {
                holder?.binding?.setVariable(sectionBinderList[i].itemVariableLayoutPair.variableId, sectionBinderList[i].section.getItemList()[position - (headerPosition + 1)])
                holder?.binding?.executePendingBindings()
                return
            }

            headerPosition += 1 + sectionBinderList[i].section.getItemList().size
        }

        throw IllegalStateException()
    }

    override fun getItemViewType(position: Int): Int {
        var headerPosition = 0
        for (i in 0 until sectionBinderList.size) {
            if (position == headerPosition) {
                return sectionBinderList[i].headerVariableLayoutPair.layoutId
            }

            if (headerPosition + 1 <= position && position < headerPosition + 1 + sectionBinderList[i].section.getItemList().size) {
                return sectionBinderList[i].itemVariableLayoutPair.layoutId
            }

            headerPosition += 1 + sectionBinderList[i].section.getItemList().size
        }

        throw IllegalStateException()
    }

    override fun getItemCount() = sectionBinderList.map { 1 + it.section.getItemList().size }.sum()

    override fun onClick(v: View?) {
    }

    override fun onLongClick(v: View?): Boolean {
        return false
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}

