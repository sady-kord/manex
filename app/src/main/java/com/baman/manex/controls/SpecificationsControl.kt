package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.baman.manex.R
import com.baman.manex.data.dto.ProductDetailsDto
import com.baman.manex.databinding.ControlSpecificationsBinding

class SpecificationsControl : LinearLayout {

    lateinit var binding: ControlSpecificationsBinding

    private lateinit var productDetails: List<ProductDetailsDto>

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
            .inflate(inflater, R.layout.control_specifications, this, true)

        binding.showMoreInfoRelative.setOnClickListener {
            showMoreInfo = !showMoreInfo
            showMore(showMoreInfo)
        }

    }

    fun setDetailsData(details: List<ProductDetailsDto>) {
        productDetails = details
        setUpList()
    }

    private fun setUpList() {

        if (productDetails.size > 4) {
            var a = mutableListOf<Pair<CharSequence, CharSequence>>()
            for (i in productDetails.indices) {
                if (i < 4) {

                    if(productDetails[i].value != null && productDetails[i].unit != null){
                        a.add(productDetails[i].title + " : " to productDetails[i].value + " " + productDetails[i].unit)
                    }else if (productDetails[i].value != null) {
                        a.add(productDetails[i].title!! + " : " to productDetails[i].value!!)
                    }else{
                        a.add(productDetails[i].title!! + " : " to productDetails[i].unit!!)
                    }

                }
            }

            binding.numberedText.setLabeledTextList(a)
            binding.showMoreInfoRelative.visibility = View.VISIBLE

        } else {

            var a = mutableListOf<Pair<CharSequence, CharSequence>>()
            for (i in productDetails.indices) {
                if(productDetails[i].value != null && productDetails[i].unit != null){
                    a.add(productDetails[i].title + " : " to productDetails[i].value + " " + productDetails[i].unit)
                }else if (productDetails[i].value != null) {
                    a.add(productDetails[i].title!! + " : " to productDetails[i].value!!)
                }else{
                    a.add(productDetails[i].title!! + " : " to productDetails[i].unit!!)
                }
            }

            binding.numberedText.setLabeledTextList(a)
            binding.showMoreInfoRelative.visibility = View.GONE

        }

    }

    fun showMore(show: Boolean) {
        if (show) {
            binding.arrowImage.rotation = 180f
            binding.textShowmore.text = context.getString(R.string.specifications_showless_title)

            var a = mutableListOf<Pair<CharSequence, CharSequence>>()
            for (i in productDetails.indices) {
                if(productDetails[i].value != null && productDetails[i].unit != null){
                    a.add(productDetails[i].title + " : " to productDetails[i].value + " " + productDetails[i].unit)
                }else if (productDetails[i].value != null) {
                    a.add(productDetails[i].title!! + " : " to productDetails[i].value!!)
                }else{
                    a.add(productDetails[i].title!! + " : " to productDetails[i].unit!!)
                }
            }
            binding.numberedText.setLabeledTextList(a)
        } else {
            binding.arrowImage.rotation = 0f

            binding.textShowmore.text = context.getString(R.string.specifications_showmore_title)
            var a = mutableListOf<Pair<CharSequence, CharSequence>>()
            for (i in productDetails.indices) {
                if(i<4){
                    if(productDetails[i].value != null && productDetails[i].unit != null){
                        a.add(productDetails[i].title + " : " to productDetails[i].value + " " + productDetails[i].unit)
                    }else if (productDetails[i].value != null) {
                        a.add(productDetails[i].title!! + " : " to productDetails[i].value!!)
                    }else{
                        a.add(productDetails[i].title!! + " : " to productDetails[i].unit!!)
                    }
                }
            }
            binding.numberedText.setLabeledTextList(a)
        }
    }

}