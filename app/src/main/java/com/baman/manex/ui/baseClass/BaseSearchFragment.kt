package com.baman.manex.ui.baseClass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.R
import com.baman.manex.controls.CustomRecyclerView
import com.baman.manex.controls.CustomViewCallBack
import com.baman.manex.controls.CustomViewScrollCallBack
import com.baman.manex.data.model.ListStatus
import com.baman.manex.data.model.ScrollDirection
import com.baman.manex.network.service.SearchTag
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.glide.GlideApp
import kotlinx.android.synthetic.main.fragment_basesearch.*


abstract class BaseSearchFragment : BaseFragment(), CustomViewScrollCallBack {

    private lateinit var customRecyclerView: CustomRecyclerView
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val QUERY_CHANGE_MIN_INTERVAL = 300L
    }

    open var hasTags = true

    lateinit var layout: View

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_basesearch, container, false)
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupCustomRecyclerView()

        getSearchControl().clearFocus()
        getSearchControl().isFocusableInTouchMode = true
        getSearchControl().setQuery("", false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


        view?.findViewById<ImageView>(R.id.search_close_btn)?.setOnClickListener {
            getSearchControl().setQuery("", false)
            onClearClick()
        }

        image_close.setOnClickListener {

            getSearchControl().clearFocus()
            getSearchControl().setQuery("", false)

            findNavController().navigateUp()
        }

        getSearchControl().setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1) {
                    getSearchControl().background =
                        resources.getDrawable(R.drawable.background_search_view)
                } else
                    getSearchControl().background =
                        resources.getDrawable(R.drawable.view_searchcontrol_background)
            }

        })

        getSearchControl().setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (hasTags) {
                    if (newText.isNullOrBlank()) {
                        getChipGroup().visibility = View.VISIBLE
                        getTagsTitle().visibility = View.VISIBLE
                        getChipDivider().visibility = View.VISIBLE
                    } else {
                        if (newText.length > 2) {
                            getChipGroup().visibility = View.GONE
                            getTagsTitle().visibility = View.GONE
                            getChipDivider().visibility = View.GONE
                        }
                    }
                }

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(
                    {
                        if (newText.isNullOrBlank()) {
                            onQueryChanged("")
                        } else {
                            if (newText.length > 2) {
                                setLoadingOnSearch(true)
                                onQueryChanged(newText)
                            }
                        }

                        setFocusOnSearchView()

                    },
                    QUERY_CHANGE_MIN_INTERVAL
                )
                return true
            }
        })

        setup()

    }

    fun setLoadingOnSearch(isLoading: Boolean) {
        val closeButtonImage: ImageView =
            layout.findViewById(R.id.search_close_btn) as ImageView

        if (isAdded) {

            var a = LinearLayout.LayoutParams(
                PublicFunction.convertDpToPixels(30f, requireContext()),
                PublicFunction.convertDpToPixels(30f, requireContext())
            )

            a.gravity = Gravity.CENTER_VERTICAL

            closeButtonImage.layoutParams = a


            if (isLoading) {
                GlideApp
                    .with(requireContext())
                    .load(R.drawable.search_loading_gif)
                    .into(closeButtonImage)
                closeButtonImage.isEnabled = false
            } else {
                closeButtonImage.setImageResource(R.drawable.ic_search_close)
                closeButtonImage.isEnabled = true
            }

        } else {
            if (isLoading) {
                closeButtonImage.setImageResource(R.drawable.ic_search_close)
                closeButtonImage.isEnabled = false
            } else {
                closeButtonImage.setImageResource(R.drawable.ic_search_close)
                closeButtonImage.isEnabled = true
            }
        }
    }

    fun setFocusOnSearchView() {
        if (getSearchControl() != null) {
            getSearchControl().onActionViewExpanded()
            getSearchControl().isIconified = false
        }
    }

    private fun setupCustomRecyclerView() {

        customRecyclerView = custom_recycler_view
        recyclerView = customRecyclerView.getRecyclerView()!!

        customRecyclerView.setCustomViewScrollCallBack(this)
        customRecyclerView.setStatus(ListStatus.UNDEFINE)

    }

    fun setupRecyclerCallback(customViewCallBack: CustomViewCallBack) {
        customRecyclerView.setCustomViewCallBack(customViewCallBack)
    }

    fun setupRecyclerSuccess(isTag: Boolean) {
        customRecyclerView.setSwipeRefreshStatus(false)
        customRecyclerView.setStatus(ListStatus.SUCCESS)
        if (!isTag) {
            getChipGroup().visibility = View.GONE
            getChipDivider().visibility = View.GONE
            getTagsTitle().visibility = View.GONE
        } else {
            getChipGroup().visibility = View.VISIBLE
            getChipDivider().visibility = View.VISIBLE
            getTagsTitle().visibility = View.VISIBLE
        }
    }

    fun setUpRecyclerNoDataFound(transaction: Boolean = false) {
        customRecyclerView.setStatus(ListStatus.SEARCH_EMPTY, transaction)
        if (!transaction) {
            getChipGroup().visibility = View.VISIBLE
            getChipDivider().visibility = View.VISIBLE
            getTagsTitle().visibility = View.VISIBLE
        }
    }

    fun setupRecyclerFailed() {
        customRecyclerView.setStatus(ListStatus.CONNECTION_FAILED)
        getChipGroup().visibility = View.GONE
        getChipDivider().visibility = View.GONE
        getTagsTitle().visibility = View.GONE
    }

    abstract fun setup()

    abstract fun onQueryChanged(query: String)

    open fun onTagSelected(key: String) {}

    open fun onClearClick() {}

    protected fun setTags(tags: List<SearchTag>, indexSelected: Int) {
        val set = mutableSetOf<String>()
        tags.forEach {
            if (it.title != null)
                set.add(it.title)
        }
        getChipGroup().setOnTagSelectedListener {
            onTagSelected(it)
        }

        getChipGroup().setup(childFragmentManager, set, indexSelected)

    }

    override fun onScrollChange(scrollDirection: ScrollDirection?) {
    }

    fun getList() = recyclerView
    fun getCustomRecycler() = customRecyclerView
    fun getTagsTitle() = text_tagstitle
    fun getChipGroup() = chipgroup
    fun getSearchControl() = searchcontrol
    fun getChipDivider() = divider_chips

}