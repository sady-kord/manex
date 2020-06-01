package com.baman.manex.ui.profile.myTransaction

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.adapter.DataBoundViewHolder
import com.baman.manex.data.dto.MyTransactionDto
import com.baman.manex.data.dto.MyTransactionGroupDto
import com.baman.manex.data.model.UserTransactionStatus
import com.baman.manex.databinding.AdapterMyTransactionBinding
import com.baman.manex.ui.common.ListHeaderViewHolder
import com.baman.manex.ui.common.UnSupportedViewTypeException
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.toPersianNumber
import timber.log.Timber

class MyTransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> ListHeaderViewHolder.getInstance(parent)
            TYPE_ITEM -> {
                val binding: AdapterMyTransactionBinding = DataBindingUtil.inflate<Nothing>(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_my_transaction, parent, false
                )
                binding.root.setOnClickListener {
                    expand(binding)
                }

                TransactionViewHolder(
                    binding
                )
            }
            else -> throw UnSupportedViewTypeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListHeaderViewHolder -> holder.bind(data[position] as String)
            else -> {
                (holder as TransactionViewHolder).bind(data[position] as MyTransactionDto)
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

    private lateinit var data: List<Any>

    fun setData(list: List<MyTransactionGroupDto>) {
        val l = mutableListOf<Any>()
        list.forEach {
            l.add(it.groupTitle)
            l.addAll(it.userTransaction)
        }
        data = l
        notifyDataSetChanged()
    }

    private fun expand(binding: AdapterMyTransactionBinding) {
        val expanded = binding.transaction?.isExpanded ?: return
        val duration = 400L
        val detail = binding.linearDetail
        val arrow = binding.imageArrow
        detail.pivotY = 0f
        if (expanded) {
            detail.animate()
                .scaleY(0f)
                .setDuration(duration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        detail.visibility = GONE
                    }
                })
                .start()
            arrow.animate()
                .rotation(270f)
                .setDuration(duration / 2)
                .start()
        } else {
            detail.visibility = VISIBLE
            detail.scaleY = 0f
            detail.animate()
                .scaleY(1f)
                .setDuration(duration)
                .setListener(null)
                .start()
            arrow.animate()
                .rotation(90f)
                .setDuration(duration / 2)
                .start()
        }
        binding.transaction?.isExpanded = !expanded
    }

    class TransactionViewHolder(val adapterBinding: AdapterMyTransactionBinding) :
        DataBoundViewHolder<AdapterMyTransactionBinding>(adapterBinding) {

        fun bind(item: MyTransactionDto) {

            adapterBinding.transaction = item

            var a = mutableListOf<Pair<CharSequence, CharSequence>>()
            var b = mutableListOf<String>()

            adapterBinding.paidAmount.text = PublicFunction.addPriceSeparator(item.paidAmount)


            if (item.burnManexAmount != 0) {
                var burn = item.burnManexAmount.toString().toPersianNumber()
                val text = binding.root.context.getString(R.string.mytransaction_burn_format, burn)
                val ssb = SpannableStringBuilder(text)
                val startingIndex = text.indexOf(burn)
                val colorSpan = ForegroundColorSpan(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.adaptertem_manexcount_textcolor
                    )
                )
                ssb.setSpan(
                    colorSpan,
                    startingIndex,
                    startingIndex + burn.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )

                a.add(item.burnAmount.toPersianNumber() to ssb)
            }


            var cc = item.earnManexAmount.toString().toPersianNumber()
            val c = binding.root.context.getString(
                R.string.mytransaction_cancel_format,
                cc,
                item.earnManexDueDate.toPersianNumber()
            )
            val cancel = SpannableStringBuilder(c)
            val startingIndexCancel = c.indexOf(cc)
            val colorSpanCancel = ForegroundColorSpan(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.adaptertem_manexcount_textcolor
                )
            )
            cancel.setSpan(
                colorSpanCancel,
                startingIndexCancel,
                startingIndexCancel + cc.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )


            var cardNumber = ""
            if (item.description.length == 16) {
                var split = item.description.split("******")
                cardNumber = split[1] + "******" + split[0]
            }else{
                cardNumber = item.description
            }

            a.add("مبلغ کل سفارش : " to PublicFunction.addPriceSeparator(item.totalAmount).toPersianNumber())
            a.add("شماره پیگیری : " to item.receiptId.toString().toPersianNumber())
            a.add("شماره کارت : " to cardNumber.toPersianNumber())


            when (UserTransactionStatus.Parse(item.type)) {
                UserTransactionStatus.Canceled -> {
                    adapterBinding.viewColor.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.cancel_color,
                            adapterBinding.root.context
                        )
                    )
                }
                UserTransactionStatus.Earn -> {
                    adapterBinding.viewColor.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.earn_color,
                            adapterBinding.root.context
                        )
                    )
                }
                UserTransactionStatus.Waiting -> {
                    adapterBinding.viewColor.setBackgroundColor(
                        PublicFunction.getColor(
                            R.color.wait_color,
                            adapterBinding.root.context
                        )
                    )
                    b.add(cancel.toString())
                }
            }

            adapterBinding.NumberTextListLabeled.setLabeledTextList(a)
            adapterBinding.NumberTextListCancel.setTextList(b)

        }

    }
}