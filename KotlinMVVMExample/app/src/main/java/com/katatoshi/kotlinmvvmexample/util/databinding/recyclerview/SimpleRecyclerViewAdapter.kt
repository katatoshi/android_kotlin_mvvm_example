package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.katatoshi.kotlinmvvmexample.R

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、クリックリスナー、ロングクリックリスナーをバインドします。
 */
fun <T> RecyclerView.bind(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, onItemClick: ((T) -> Unit)?, onItemLongClick: ((T) -> Unit)?) {
    val adapter = SimpleRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, onItemClick, onItemLongClick)
    setAdapter(adapter)
}

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、クリックリスナーをバインドします。
 */
fun <T> RecyclerView.bind(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, onItemClick: (T) -> Unit) {
    val adapter = SimpleRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, onItemClick, null)
    setAdapter(adapter)
}

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペアをバインドします。
 */
fun <T> RecyclerView.bind(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair) {
    val adapter = SimpleRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, null, null)
    setAdapter(adapter)
}

/**
 * データバインディング対応 RecyclerViewAdapter。セクション分け非対応。
 */
private class SimpleRecyclerViewAdapter<T>(
        itemCollection: Collection<T>,
        private val variableLayoutPair: VariableLayoutPair,
        private val onItemClick: ((T) -> Unit)?,
        private val onItemLongClick: ((T) -> Unit)?
) : RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    private val onListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<T>>

    private val itemList: ObservableList<T>

    private var inflater: LayoutInflater? = null

    init {
        onListChangedCallback = object : ObservableList.OnListChangedCallback<ObservableList<T>>() {

            override fun onChanged(sender: ObservableList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }
        }

        if (itemCollection is ObservableList<T>) {
            itemList = itemCollection
        } else {
            itemList = ObservableArrayList()
            itemList.addAll(itemCollection)
        }
        notifyItemRangeInserted(0, itemList.size)
        itemList.addOnListChangedCallback(onListChangedCallback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        itemList.removeOnListChangedCallback(onListChangedCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent?.context)
        }

        return ViewHolder(DataBindingUtil.inflate(inflater, viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = itemList[position]
        holder?.binding?.setVariable(variableLayoutPair.variableId, item)
        holder?.binding?.root?.setTag(R.id.key_simple_recycler_view_adapter_item, item)

        if (onItemClick != null) {
            holder?.binding?.root?.setOnClickListener(this)
        }

        if (onItemLongClick != null) {
            holder?.binding?.root?.setOnLongClickListener(this)
        }

        holder?.binding?.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return variableLayoutPair.layoutId
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onClick(v: View?) {
        if (onItemClick == null) {
            return
        }

        @Suppress("UNCHECKED_CAST")
        (v?.getTag(R.id.key_simple_recycler_view_adapter_item) as? T)?.let {
            onItemClick.invoke(it)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (onItemLongClick == null) {
            return false
        }

        @Suppress("UNCHECKED_CAST")
        return (v?.getTag(R.id.key_simple_recycler_view_adapter_item) as? T)?.let {
            onItemLongClick.invoke(it)
            true
        } ?: false
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}