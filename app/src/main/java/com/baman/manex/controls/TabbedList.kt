package com.baman.manex.controls

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.baman.manex.R
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.ui.common.SimpleViewHolder
import com.baman.manex.ui.baseClass.Expandable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.control_tabbedlist.view.*
import timber.log.Timber

data class Bar(var index: Int, val adapter: DelegateAdapter.Adapter<out RecyclerView.ViewHolder>)

class TabbedList : LinearLayoutCompat , CustomViewScrollCallBack  {

    private lateinit var expandableRoot: Expandable
    private val tabMap = mutableMapOf<TabLayout.Tab, Bar>()
    private lateinit var tabLayout: TabLayout
    private lateinit var delegateAdapter: DelegateAdapter
    private var firstTimeTabSelected = false

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView

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
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.control_tabbedlist, this, true)

        orientation = VERTICAL

        tabLayout = tab_layout

        setUpList()
    }

    private fun setUpList() {
        val layoutManager = VirtualLayoutManager(context!!)

        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        tabLayout.tabMode = TabLayout.MODE_FIXED

        var selectedByPositionChange = false
        var scrollTime = 0L
        val delay = 1_000

        //recycler_view.layoutManager = layoutManager

        val viewPool = RecyclerView.RecycledViewPool()
        // recycler_view.setRecycledViewPool(viewPool)
        viewPool.setMaxRecycledViews(0, 20)

        delegateAdapter = DelegateAdapter(layoutManager, false)

        //recycler_view.adapter = delegateAdapter

       //setUp recycler
        customRecyclerView = custom_recycler_view
        recyclerView = customRecyclerView.getRecyclerView()!!


        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)

        recyclerView?.adapter = delegateAdapter
        recyclerView.layoutManager = layoutManager
        //


        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (firstTimeTabSelected) {
                        if (::expandableRoot.isInitialized) expandableRoot.expand(false)
                    } else {
                        firstTimeTabSelected = true
                    }

                    Timber.d("onTabSelected ${tab.position}" +
                            " - selected by position change: $selectedByPositionChange")
                    if (selectedByPositionChange) selectedByPositionChange = false
                    else {
                        scrollTime = SystemClock.uptimeMillis()
                        val positionForTab = getPositionForTab(tab)
                        smoothScroller.targetPosition = positionForTab
                        Timber.d("starting smooth scroll to position $positionForTab")
                        layoutManager.startSmoothScroll(smoothScroller)

                    }
                }
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {}
            override fun onScrolled(recyclerView: RecyclerView, i: Int, i2: Int) {
                if (scrollTime + delay > SystemClock.uptimeMillis()) {
                    Timber.d("onScroll - skipped")
                    return
                }
                Timber.d("onScroll - try to find appropriate tab")
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                var tabToSelect: TabLayout.Tab? = null
                tabMap.forEach {
                    val position = it.value.index
                    if (firstVisibleItemPosition > position-1) tabToSelect = it.key
                }
                tabToSelect?.let {
                    if (!it.isSelected) {
                        Timber.d("onScroll - should select tab ${it.position}")
                        selectedByPositionChange = true
                        it.select()
                    }
                }
            }
        })


    }

    fun setupRecyclerCallback(customViewCallBack: CustomViewCallBack){
        customRecyclerView.setCustomViewCallBack(customViewCallBack)
    }

    fun setupRecyclerSuccess(){
        customRecyclerView.setSwipeRefreshStatus(true)
        customRecyclerView.setStatus(ListStatus.SUCCESS)
    }

    fun setupRecyclerFailed(){
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
    }

    private fun getPositionForTab(tab: TabLayout.Tab): Int =
        tabMap[tab]?.index ?: 0

    private val dataObserver = object: RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val adaptersCount = delegateAdapter.adaptersCount
            var offsetIndex = 0
            for (i in 0 until adaptersCount) {
                when (val adapter = delegateAdapter.findAdapterByIndex(i)) {
                    is TitleAdapter -> {
                        tabMap.values.forEach {
                            if (it.adapter == adapter) {
                                it.index = offsetIndex
                            }
                        }
                        offsetIndex++
                    }
                    is ShowMoreAdapter, is DividerAdapter -> {
                        offsetIndex++
                    }
                    else -> {
                        offsetIndex += adapter.itemCount
                    }
                }
            }
        }
    }

    fun addItemGroup(
        title: String,
        adapter: DelegateAdapter.Adapter<out RecyclerView.ViewHolder>,
        addShowMoreButtonControl: Boolean = false,
        showMoreClickListener: ((View) -> Unit)? = null
    ) {
        if (delegateAdapter.adaptersCount > 0) {
            delegateAdapter.addAdapter(DividerAdapter())
        }

        val newTab = tab_layout.newTab()
        newTab.text = title
        tabLayout.addTab(newTab)

        val titleAdapter = TitleAdapter(title)

        tabMap[newTab] = Bar(delegateAdapter.itemCount, titleAdapter)

        delegateAdapter.addAdapter(titleAdapter)

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                dataObserver.onChanged()
            }
        })

        delegateAdapter.addAdapter(adapter)

        if (addShowMoreButtonControl) {
            delegateAdapter.addAdapter(ShowMoreAdapter(showMoreClickListener))
        }
    }

    fun setExpandableRoot(expandable: Expandable) {
        expandableRoot = expandable
    }

    class ShowMoreAdapter(private val clickListener: ((View) -> Unit)?)
        : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount() = 1
        override fun onCreateLayoutHelper() = SingleLayoutHelper()
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(
                R.layout.item_show_more,
                parent,
                false
            )
            clickListener?.let {
                itemView.setOnClickListener(clickListener)
            }
            return SimpleViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    }

    class DividerAdapter : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount() = 1
        override fun onCreateLayoutHelper() = SingleLayoutHelper()
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(
                R.layout.item_bigdivider,
                parent,
                false
            )
            return SimpleViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    }

    class TitleAdapter(val title: String) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount() = 1
        override fun onCreateLayoutHelper() = SingleLayoutHelper()
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(
                R.layout.adapter_tabbedlist_title,
                parent,
                false
            )
            return SimpleViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.text_title).text = title
        }
    }

    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }

}