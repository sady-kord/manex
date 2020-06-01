package com.baman.manex.ui.baseClass

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.baman.manex.R
import com.baman.manex.controls.Toolbar
import com.baman.manex.controls.appbar.AppBarStateChangeListener
import com.baman.manex.controls.appbar.State
import com.baman.manex.di.Injectable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tuyenmonkey.mkloader.MKLoader
import kotlinx.android.synthetic.main.fragment_collapsingtoolbar.*
import kotlinx.android.synthetic.main.fragment_collapsingtoolbar.view.*
import kotlinx.android.synthetic.main.layout_no_internet_connection.view.*

abstract class CollapsingToolbarFragment : BaseFragment(), Injectable,
    Expandable {

    private var retryCallBack: RetryCallBack? = null

    open var hideToolbarWhenExpanded = true

    private var isCollapsed = false

    private val listener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            onAppBarStateChanged(appBarLayout, state)
        }
    }

    var itemView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (itemView == null) {
            itemView = inflater
                .inflate(R.layout.fragment_collapsingtoolbar, container, false)

            onCreateCollapsingLayout(inflater, container, savedInstanceState)?.let {
                itemView?.layout_collapsingcontent?.addView(it)
            }

            onCreatePinLayout(inflater, container, savedInstanceState)?.let {
                itemView?.layout_pincontent?.addView(it)
            }

            itemView?.container?.addView(
                onCreateContentView(
                    inflater,
                    container,
                    savedInstanceState
                )
            )

        }
        return itemView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.visibility = if (hideToolbarWhenExpanded) View.GONE else View.VISIBLE

        getNoNetLayout()?.again?.setOnClickListener {
            if (retryCallBack != null) retryCallBack?.onRetryClicked()
        }
    }

    fun onAppBarStateChanged(appBarLayout: AppBarLayout, state: State) {
        if (hideToolbarWhenExpanded) {
            isCollapsed = state == State.COLLAPSED

            val duration =
                resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

            if (isCollapsed) {
                toolbar.visibility = View.VISIBLE


                if (layout_pincontent.childCount != 0) {
                    layout_pincontent.visibility = View.VISIBLE
                    pin_toolbar_layout.visibility = View.VISIBLE
                } else {
                    layout_pincontent.visibility = View.GONE
                    pin_toolbar_layout.visibility = View.GONE
                }

                toolbar.alpha = 0f
                toolbar.animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setListener(null)
                    .start()
            } else {

                layout_pincontent.visibility = View.GONE
                pin_toolbar_layout.visibility = View.GONE

                toolbar.animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .setListener(if (state != State.EXPANDED) null else
                        object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                toolbar?.visibility = View.GONE
                            }
                        })
                    .start()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        getAppBar().addOnOffsetChangedListener(listener)
        if (isCollapsed)
            toolbar.visibility = View.VISIBLE
        //onAppBarStateChanged(getAppBar(), State.COLLAPSED)

    }

    override fun onStop() {
        super.onStop()

        getAppBar().removeOnOffsetChangedListener(listener)
    }

    open fun onCreateCollapsingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    open fun onCreatePinLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    abstract fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    fun setContentScrimRes(@ColorRes contentScrim: Int) {
        setContentScrim(ContextCompat.getColor(context!!, contentScrim))
    }

    fun setContentScrim(@ColorInt contentScrim: Int) {
        collapsing_layout.contentScrim = ColorDrawable(contentScrim)
    }

    fun getToolbar(): Toolbar = toolbar

    fun getAppBar(): AppBarLayout = app_bar

    fun getNestedScroll(): NestedScrollView = container

    fun getTabLayout(): TabLayout = tablayout

    fun getNoNetLayout(): View? = no_internet_layout

    fun getButtonSingleText(): AppCompatTextView = text_layout
    fun getButtonSingleLoading(): MKLoader = loading

    fun setButtonSingle(
        title: String, isEnable: Boolean = true,
        clickListener: ((View) -> Unit)?
    ) {
        double_button_layout.visibility = View.GONE
        button_single.visibility = View.VISIBLE
        text_layout.text = title

        if (isEnable)
            text_layout.setOnClickListener(clickListener)
        else
            text_layout.setOnClickListener(null)

        button_single.isEnabled = isEnable
        button_single.isClickable = isEnable
        button_single.isFocusable = isEnable

    }

    fun setButtonDouble(
        clickListenerCall: ((View) -> Unit)?,
        clickListenerDirection: ((View) -> Unit)?
    ) {
        double_button_layout.visibility = View.VISIBLE
        button_single.visibility = View.GONE
        button_call.setOnClickListener(clickListenerCall)
        button_direction.setOnClickListener(clickListenerDirection)
    }

    fun setStatusBarColor(color: Int) {
        (activity as BaseActivity).setStatusBarColor(color)
    }

    override fun expand(expand: Boolean) {
        app_bar.setExpanded(expand, true)
    }


    fun setBackButtonSingleEnable(backEnable: Boolean) {

        if (backEnable)
            button_single.setBackgroundDrawable(
                resources.getDrawable(
                    R.drawable.background_button_common
                )
            )
        else
            button_single.setBackgroundDrawable(
                resources.getDrawable(
                    R.drawable.background_button_common_disable
                )
            )
    }

    fun setRetryCallBack(retryCallBack: RetryCallBack) {
        this.retryCallBack = retryCallBack
    }


}

interface RetryCallBack {
    fun onRetryClicked()
}
