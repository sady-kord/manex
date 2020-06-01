package com.baman.manex.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.baman.manex.AppExecutors
import com.baman.manex.controls.AdapterCallBack

abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
    appExecutors: AppExecutors,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundViewHolder<V>>(
    AsyncDifferConfig.Builder<T>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
        checkScrollChanged(itemCount, position)
    }

    protected abstract fun bind(binding: V, item: T)

    var lastPosition = -1

    private var adapterCallBack: AdapterCallBack? = null

    fun setAdapterCallBack(myCustomAdapterCallBack: AdapterCallBack?) {
        this.adapterCallBack = myCustomAdapterCallBack
    }

    protected fun checkScrollChanged(adapterSize: Int, position: Int): Int {
        try {
            if (lastPosition >= position) {
                return lastPosition
            }
            lastPosition = position
            if (lastPosition == adapterSize - 1) { // adapterSize - 1 for zero base array
                if (adapterCallBack != null) {
                    adapterCallBack!!.richToEnd()
                }
            }
            return lastPosition
        } catch (ex: Exception) {
            Log.e(
                "ScrollChanged",
                "checkScrollChanged() called with: Exception = " + ex.message + " - Position = [" + position + "]"
            )
            return lastPosition
        }
    }



}
