package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.AppExecutors

import com.baman.manex.R
import com.baman.manex.adapter.DataBoundListAdapter
import com.baman.manex.data.dto.FilterItemsDto
import com.baman.manex.databinding.AdapterItemFilterBinding

import kotlinx.android.synthetic.main.layout_checkbox_list.view.*

class CheckBoxList : FrameLayout {

    private lateinit var adapter: CheckBoxItemAdapter
    private var selectedItems = mutableListOf<Int>()
    private var selectedArgsData  = mutableListOf<String>()

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_checkbox_list, this, true)
    }

    fun setItems(
        executors: AppExecutors,
        items: List<FilterItemsDto>,
        itemClickCallBack: ((List<FilterItemsDto>) -> Unit),
        selectedArgs : List<String>
    ) {}

    fun setup(
        executors: AppExecutors,
        itemClickCallBack: ((List<Int>) -> Unit)
    ) {
        adapter = CheckBoxItemAdapter(executors, itemClickCallBack)
        filter_list.adapter = adapter
    }

    fun setItems(
        items: List<FilterItemsDto>
    ) {
        adapter.submitList(items)
    }

    fun setSelectedItems(selectedItems: List<Int>){
        this.selectedItems.clear()
        this.selectedItems.addAll(selectedItems)

        onChange()
    }

    private fun onChange() {
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    inner class CheckBoxItemAdapter(
        appExecutors: AppExecutors,
        private val itemClickCallBack: ((List<Int>) -> Unit)?
    ) : DataBoundListAdapter<FilterItemsDto, AdapterItemFilterBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<FilterItemsDto>() {
            override fun areItemsTheSame(
                oldItem: FilterItemsDto,
                newItem: FilterItemsDto
            ): Boolean {

                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FilterItemsDto,
                newItem: FilterItemsDto
            ): Boolean {
                return oldItem.order == newItem.order
                        && oldItem.key == newItem.key
            }
        }
    ) {
        override fun createBinding(parent: ViewGroup): AdapterItemFilterBinding {
            val binding = DataBindingUtil.inflate<AdapterItemFilterBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_item_filter,
                parent,
                false
            )

            binding.filterLayoutMain.setOnClickListener {
                binding.filterItem?.let {
                    val key = it.key
                    if (selectedItems.indexOf(key) == -1) selectedItems.add(key)
                    else selectedItems.remove(key)

                    notifyDataSetChanged()

                    itemClickCallBack?.invoke(selectedItems)
                }
            }
            return binding
        }

        override fun bind(binding: AdapterItemFilterBinding, item: FilterItemsDto) {
            binding.filterItem = item
            binding.filterTextTitle.text = item.title

            if (selectedItems.indexOf(item.key) == -1) {
                binding.filterLayoutMain.background = ContextCompat.getDrawable(context, R.drawable.background_item_filter_unselected)
                binding.filterTextTitle.setTextColor(ContextCompat.getColor(context, R.color.filterfragment_text_item))
                binding.filterCheckbox.isChecked = false
            } else {
                binding.filterLayoutMain.background = ContextCompat.getDrawable(context, R.drawable.background_item_filter_selected)
                binding.filterTextTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                binding.filterCheckbox.isChecked = true
            }
        }
    }

    fun setListLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        filter_list.layoutManager = layoutManager
    }
}
