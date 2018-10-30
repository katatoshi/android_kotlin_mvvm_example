package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.databinding.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.katatoshi.kotlinmvvmexample.R

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、プログレスバーを表示するかどうかの ObservableBoolean、リストの末尾のアイテムが現れたときのコールバック、クリックリスナー、ロングクリックリスナーをバインドします。
 */
fun <T> RecyclerView.bindPaging(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, progressBarEnabled: ObservableBoolean, onBottomItemAppeared: () -> Unit, onItemClick: ((T) -> Unit)?, onItemLongClick: ((T) -> Unit)?) {
    val adapter = PagingRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, progressBarEnabled, onBottomItemAppeared, onItemClick, onItemLongClick)
    setAdapter(adapter)
}

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、プログレスバーを表示するかどうかの ObservableBoolean、リストの末尾のアイテムが現れたときのコールバック、クリックリスナーをバインドします。
 */
fun <T> RecyclerView.bindPaging(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, progressBarEnabled: ObservableBoolean, onBottomItemAppeared: () -> Unit, onItemClick: (T) -> Unit) {
    val adapter = PagingRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, progressBarEnabled, onBottomItemAppeared, onItemClick, null)
    setAdapter(adapter)
}

/**
 * RecyclerView にコレクション、バインドする変数名 ID とレイアウト ID のペア、プログレスバーを表示するかどうかの ObservableBoolean、リストの末尾のアイテムが現れたときのコールバックをバインドします。
 */
fun <T> RecyclerView.bindPaging(itemCollection: Collection<T>, variableLayoutPair: VariableLayoutPair, progressBarEnabled: ObservableBoolean, onBottomItemAppeared: () -> Unit) {
    val adapter = PagingRecyclerViewAdapter<T>(itemCollection, variableLayoutPair, progressBarEnabled, onBottomItemAppeared, null, null)
    setAdapter(adapter)
}

/**
 * データバインディング対応 RecyclerViewAdapter。セクション分け非対応。
 */
private class PagingRecyclerViewAdapter<T>(
        itemCollection: Collection<T>,
        private val variableLayoutPair: VariableLayoutPair,
        private val progressBarEnabled: ObservableBoolean,
        private val onBottomItemAppeared: () -> Unit,
        private val onItemClick: ((T) -> Unit)?,
        private val onItemLongClick: ((T) -> Unit)?
) : RecyclerView.Adapter<PagingRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    private val onListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<T>>

    private val itemList: ObservableList<T>

    private var inflater: LayoutInflater? = null

    private val onProgressBarEnabledPropertyChangedCallback: Observable.OnPropertyChangedCallback

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

        onProgressBarEnabledPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (progressBarEnabled.get()) {
                    // プログレスバーが有効になったので、末尾にプログレスバー表示用のアイテムを追加する。
                    notifyItemInserted(itemList.size)
                } else {
                    // プログレスバーが無効になったので、末尾からプログレスバー表示用のアイテムを削除する。
                    notifyItemRemoved(itemList.size)
                }
            }
        }
        progressBarEnabled.addOnPropertyChangedCallback(onProgressBarEnabledPropertyChangedCallback)

        if (progressBarEnabled.get()) {
            // プログレスバーが有効ならば、末尾にプログレスバー表示用のアイテムを追加する。
            notifyItemInserted(itemList.size)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        //region ページング実装
        // ref. http://stackoverflow.com/questions/36127734/detect-when-recyclerview-reaches-the-bottom-most-position-while-scrolling
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(r: RecyclerView?, dx: Int, dy: Int) {
                (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val pastVisibleItems = it.findFirstVisibleItemPosition()

                    if (totalItemCount <= pastVisibleItems + visibleItemCount) {
                        onBottomItemAppeared()
                    }
                }
            }
        })
        //endregion
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        itemList.removeOnListChangedCallback(onListChangedCallback)

        progressBarEnabled.removeOnPropertyChangedCallback(onProgressBarEnabledPropertyChangedCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }

        return ViewHolder(DataBindingUtil.inflate(inflater!!, viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < itemList.size) {
            // プログレスバー表示用のアイテム以外ならば、variable や listener などをセットする。
            val item = itemList[position]
            holder.binding.setVariable(variableLayoutPair.variableId, item)
            holder.binding.root.setTag(R.id.key_simple_recycler_view_adapter_item, item)

            if (onItemClick != null) {
                holder.binding.root.setOnClickListener(this)
            }

            if (onItemLongClick != null) {
                holder.binding.root.setOnLongClickListener(this)
            }
        }

        holder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int) = if (position < itemList.size) variableLayoutPair.layoutId else R.layout.item_progress_bar

    override fun getItemCount() = if (!progressBarEnabled.get()) itemList.size else itemList.size + 1

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