package com.baman.manex.ui.profile.setting.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundViewHolder
import com.baman.manex.databinding.AdapterCityBinding
import com.baman.manex.ui.common.Section
import com.baman.manex.ui.common.UnSupportedViewTypeException
import timber.log.Timber

class CityDto(val id: Int, val title: String, val isSelected: Boolean, val enable: Boolean)

class CitiesAdapter(val listener: (CityDto) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var data: List<Any>

    private var selected: Int = 0

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> CityListHeaderViewHolder.getInstance(
                parent
            )
            TYPE_ITEM -> {
                val binding: AdapterCityBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_city, parent, false
                )

                binding.root.setOnClickListener {
                    if (binding.city!!.enable) {
                        clicked(binding)
                        listener.invoke(binding.city!!)
                    }
                }

                CityViewHolder(
                    binding
                )
            }
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CityListHeaderViewHolder -> {
                holder.bind(data[position] as String, position)
            }
            else -> {
                (holder as CityViewHolder).bind(data[position] as CityDto, position,selected)
            }
        }
    }

    override fun getItemCount(): Int {
        val count = if (::data.isInitialized) data.size else 0
        Timber.d("getItemCount: $count")
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val type = if (data[position] is String) TYPE_TITLE
        else TYPE_ITEM
        Timber.d("getItemViewType: $type")
        return type
    }

    fun setData(list: List<Section<CityDto>>) {
        val l = mutableListOf<Any>()
        list.forEach {
            l.add(it.title)
            l.addAll(it.list)
        }
        data = l
        notifyDataSetChanged()
    }

    fun setSelected(selectedItem: Int) {
        selected = selectedItem
        notifyDataSetChanged()
    }

    private fun clicked(binding: AdapterCityBinding) {
        binding.cityItemControl.setClick()
        setSelected(binding.city!!.id)
    }

    class CityViewHolder(val a: AdapterCityBinding) :
        DataBoundViewHolder<AdapterCityBinding>(a) {

        fun bind(
            item: CityDto,
            position: Int,
            selectedId : Int
        ) {

            a.city = item

            a.cityItemControl.setValue(item.title)

            if (position < 3) {
                a.cityItemControl.setActiveCity(true)
            } else {
                a.cityItemControl.setActiveCity(false)
            }

            if (item.id == selectedId) {
                a.cityItemControl.setClick()
            } else {
                a.cityItemControl.setUnClick()
            }

        }

    }


}