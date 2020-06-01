package com.baman.manex.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.baman.manex.R
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import kotlinx.android.synthetic.main.layout_custom_recycler.view.*
import kotlinx.android.synthetic.main.layout_no_data_found.view.*
import kotlinx.android.synthetic.main.layout_no_internet_connection.view.*


class CustomRecyclerView : RelativeLayout, View.OnClickListener, OnRefreshListener {

    private var customViewCallBack: CustomViewCallBack? = null
    private var customViewScrollCallBack: CustomViewScrollCallBack? = null
    private var swipeRefreshLayout: CustomSwipeRefreshLayout? = null

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

        val view: View =
            LayoutInflater.from(context).inflate(
                R.layout.layout_custom_recycler, this,
                false
            )

        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView, newState: Int
            ) {
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (customViewScrollCallBack != null) customViewScrollCallBack!!.onScrollChange(
                        ScrollDirection.UP
                    )
                } else {
                    if (customViewScrollCallBack != null) customViewScrollCallBack!!.onScrollChange(
                        ScrollDirection.DOWN
                    )
                }
            }
        })

        swipeRefreshLayout = view.swipeHolder.findViewById(R.id.swipeHolder)
        swipeRefreshLayout?.setOnRefreshListener(this)

        view.no_connection_layout.again.setOnClickListener(this)

        addView(view)
    }

    fun setCustomViewCallBack(customViewCallBack: CustomViewCallBack) {
        this.customViewCallBack = customViewCallBack
    }

    fun setCustomViewScrollCallBack(customViewScrollCallBack: CustomViewScrollCallBack?) {
        this.customViewScrollCallBack = customViewScrollCallBack
    }

    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.again) {
            if (customViewCallBack != null) customViewCallBack?.onRetryClicked()
        }
    }

    private fun showSwipe() {
        swipeRefreshLayout!!.isRefreshing = true
    }

    private fun hideSwipe() {
        swipeRefreshLayout!!.isRefreshing = false
    }

    private fun showRecyclerView() {
        recyclerView.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        recyclerView.visibility = View.GONE
    }

    private fun showConnectionErrorLayout() {
        no_connection_layout.visibility = View.VISIBLE
    }

    private fun hideConnectionErrorLayout() {
        no_connection_layout.visibility = View.GONE
    }

    private fun showNoDataFoundLayout() {
        no_data_found_layout.visibility = View.VISIBLE
    }

    private fun hideNoDataFounLayout() {
        no_data_found_layout.visibility = View.GONE
    }

    private fun showLoadingBottom() {
        //should visible
        loading_bottom_layout.visibility = View.GONE
    }

    private fun hideLoadingBottom() {
        loading_bottom_layout.visibility = View.INVISIBLE
    }


    private fun setDataForDataNotFound(transaction: Boolean) {
        no_data_found_layout.image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_no_search_found))
        no_data_found_layout.title.text = resources.getString(R.string.no_search_result_found)
        if (transaction)
            no_data_found_layout.sub_title.text = resources.getString(R.string.no_search_found_desc_for_transaction)
        else
            no_data_found_layout.sub_title.text = resources.getString(R.string.no_search_found_desc)
    }

    private fun setDataForFirstTime() {
        no_data_found_layout.image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_search))
        no_data_found_layout.title.text = resources.getString(R.string.first_search_result)
        no_data_found_layout.sub_title.text = resources.getString(R.string.first_search_desc)
    }

    fun setSwipeRefreshStatus(enable: Boolean) {
        swipeRefreshLayout?.isEnabled = enable
    }

    fun setStatus(status: ListStatus?, transaction: Boolean = false) {

        when (status) {
            ListStatus.SUCCESS -> {
                hideConnectionErrorLayout()
                invalidate() // for redraw
                showRecyclerView()
                hideSwipe()
                hideLoadingBottom()
                hideNoDataFounLayout()
            }
            ListStatus.LOADING_BOTTOM -> {
                hideLoadingBottom()
                hideNoDataFounLayout()
                hideSwipe()
                hideConnectionErrorLayout()
                invalidate() // for redraw
                showLoadingBottom()
            }
            ListStatus.CONNECTION_FAILED -> {
                hideRecyclerView()
                invalidate() // for redraw
                showConnectionErrorLayout()
                hideSwipe()
                hideLoadingBottom()
                hideNoDataFounLayout()
            }
            ListStatus.SEARCH_EMPTY -> {
                hideRecyclerView()
                invalidate() // for redraw
                showNoDataFoundLayout()
                hideSwipe()
                hideLoadingBottom()
                setDataForDataNotFound(transaction)
                hideConnectionErrorLayout()
            }
            ListStatus.FIRST_SEARCH_STEP -> {
                hideRecyclerView()
                invalidate() // for redraw
                showNoDataFoundLayout()
                hideSwipe()
                hideLoadingBottom()
                setDataForFirstTime()
                hideConnectionErrorLayout()
            }
        }
    }

    override fun onRefresh() {
        if (customViewCallBack != null) {
            customViewCallBack!!.onRefresh(1)
        }
    }
}

interface CustomViewScrollCallBack {
    fun onScrollChange(scrollDirection: ScrollDirection?)
}


interface CustomViewCallBack {
    fun onRetryClicked()
    fun onRefresh(page: Int)
}

interface AdapterCallBack {
    fun richToEnd()
}