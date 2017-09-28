package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.katatoshi.kotlinmvvmexample.R

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、並べ替えのハンドラビュー、クリックリスナー、ロングクリックリスナーをバインドします。
 */
fun <T> RecyclerView.bindSortable(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, @IdRes sortingHandlerViewId: Int, onItemClick: ((T) -> Unit)?, onItemLongClick: ((T) -> Unit)?) {
    val adapter = SortableRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, sortingHandlerViewId, onItemClick, onItemLongClick)
    setAdapter(adapter)
}

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、並べ替えのハンドラビュー、クリックリスナーをバインドします。
 */
fun <T> RecyclerView.bindSortable(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, @IdRes sortingHandlerViewId: Int, onItemClick: (T) -> Unit) {
    val adapter = SortableRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, sortingHandlerViewId, onItemClick, null)
    setAdapter(adapter)
}

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、並べ替えのハンドラビューをバインドします。
 */
fun <T> RecyclerView.bindSortable(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, @IdRes sortingHandlerViewId: Int) {
    val adapter = SortableRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, sortingHandlerViewId, null, null)
    setAdapter(adapter)
}

/**
 * データバインディング対応 RecyclerViewAdapter。セクション分け非対応。
 */
private class SortableRecyclerViewAdapter<T>(
        itemCollection: Collection<T>,
        private val variableLayoutPair: VariableLayoutPair,
        private @IdRes val sortingHandlerViewId: Int,
        private val onItemClick: ((T) -> Unit)?,
        private val onItemLongClick: ((T) -> Unit)?
) : RecyclerView.Adapter<SortableRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    private val onListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<T>>

    private val itemList: ObservableList<T>

    private var inflater: LayoutInflater? = null

    private val itemSortingHelper: ItemTouchHelper

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

        itemSortingHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            private var fromPosition = -1

            private var toPosition = -1

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                if (viewHolder == null) {
                    return false
                }

                if (target == null) {
                    return false
                }

                notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)

                toPosition = target.adapterPosition

                return true
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (viewHolder == null) {
                    return
                }

                when (actionState) {
                    ItemTouchHelper.ACTION_STATE_DRAG -> fromPosition = viewHolder.adapterPosition
                }
            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
                super.clearView(recyclerView, viewHolder)

                if (fromPosition == toPosition || toPosition < 0) {
                    return
                }

                itemList.executeWithoutCallback(onListChangedCallback) {
                    val item = itemList.removeAt(fromPosition)
                    itemList.add(toPosition, item)
                    toPosition = -1
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                // do nothing
            }

            override fun isLongPressDragEnabled(): Boolean = false
        })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)

        itemSortingHelper.attachToRecyclerView(recyclerView)
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

        holder?.binding?.root?.findViewById<View>(sortingHandlerViewId)?.let { sortingHandlerView ->
            sortingHandlerView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        itemSortingHelper.startDrag(holder)
                        true
                    }
                    else -> false
                }
            }
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

    private fun <T> ObservableList<T>.executeWithoutCallback(onListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<T>>, action: () -> Unit) {
        removeOnListChangedCallback(onListChangedCallback)
        action()
        addOnListChangedCallback(onListChangedCallback)
    }
}