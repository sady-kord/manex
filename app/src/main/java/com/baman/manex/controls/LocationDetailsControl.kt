package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.baman.manex.R
import com.baman.manex.data.dto.StoreInfoDto
import com.baman.manex.data.dto.WorkTimesDto
import com.baman.manex.data.model.ContactType
import com.baman.manex.databinding.ControlLocationDetailsBinding
import com.baman.manex.util.glide.GlideApp
import com.baman.manex.util.toPersianNumber
import kotlinx.android.synthetic.main.contacts_location_layout.view.*

class LocationDetailsControl : LinearLayout {

    lateinit var binding: ControlLocationDetailsBinding

    private lateinit var timeList: List<WorkTimesDto>

    var showMoreInfo: Boolean = false


    constructor(context: Context?) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = DataBindingUtil
            .inflate(inflater, R.layout.control_location_details, this, true)

        binding.showMoreInfoRelative.setOnClickListener {
            showMoreInfo = !showMoreInfo
            showMore(showMoreInfo)
            if (onShowMoreClickListener != null) {
                onShowMoreClickListener!!.onClick()
            }
        }

    }

    fun setLocationData(storeInfoDto: StoreInfoDto) {
        binding.item = storeInfoDto
        timeList = storeInfoDto.workTimes
        setUpInfoList(storeInfoDto)
        setUpTimeList()
    }

    private fun setUpInfoList(storeInfoDto: StoreInfoDto) {

        val inflater = LayoutInflater.from(context)

        if (storeInfoDto.contacts.isNotEmpty()) {
            storeInfoDto.contacts.forEach { res ->
                val itemView =
                    inflater.inflate(R.layout.contacts_location_layout, this, false)

                var value = itemView.findViewById<AppCompatTextView>(R.id.value_text)
                value.text = res.value.toPersianNumber()
                var icon = itemView.findViewById<AppCompatImageView>(R.id.icon_image)
                GlideApp.with(context).load(res.imageUrl).into(icon)

                when (ContactType.Parse(res.contactType)) {
                    ContactType.Phone -> {
                        value.layoutDirection = View.LAYOUT_DIRECTION_LTR
                        value.textDirection = View.TEXT_DIRECTION_LTR
                    }
                }

                itemView.setOnClickListener {
                    if (onShowMoreClickListener != null) {
                        onShowMoreClickListener!!.onContactClick(res.contactType, res.value)
                    }
                }

                binding.informationLinear.addView(itemView)

            }
        }
    }

    private fun setUpTimeList() {
        if (::timeList.isInitialized) {
            timeList.forEach {
                val workTimesControl = WorkTimesControl(context, null)
                workTimesControl.setTime(it.status)
                workTimesControl.setTitle(it.title)
                binding.workTimeLinear.addView(workTimesControl)
            }
        }
    }

    fun showMore(show: Boolean) {
        if (show) {
            binding.informationLinear.visibility = View.VISIBLE
            binding.worktimeData.visibility = View.VISIBLE
            binding.arrowImage.rotation = 180f
            binding.textShowmore.text = context.getString(R.string.locationdetails_showless_title)
        } else {
            binding.informationLinear.visibility = View.GONE
            binding.worktimeData.visibility = View.GONE
            binding.arrowImage.rotation = 0f
            binding.textShowmore.text = context.getString(R.string.locationdetails_showmore_title)
        }


    }

    protected var onShowMoreClickListener: OnShowMoreLocationClick? = null

    interface OnShowMoreLocationClick {
        fun onClick()
        fun onContactClick(contactType: String, value: String)
    }

    fun setOnClickListener(listener: OnShowMoreLocationClick?) {
        onShowMoreClickListener = listener!!
    }
}