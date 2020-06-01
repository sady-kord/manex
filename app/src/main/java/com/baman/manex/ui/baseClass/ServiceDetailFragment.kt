package com.baman.manex.ui.baseClass

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.baman.manex.R
import com.baman.manex.databinding.FragmentServicedetailBinding
import com.baman.manex.di.Injectable
import com.baman.manex.ui.burn.burnManexStore.ImageSliderAdapter
import com.baman.manex.util.PublicFunction
import com.baman.manex.util.autoCleared
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import kotlinx.android.synthetic.main.fragment_servicedetail.*
import timber.log.Timber

abstract class ServiceDetailFragment : CollapsingToolbarFragment(), Injectable {

    private var binding by autoCleared<FragmentServicedetailBinding>()

    private lateinit var blackColorFilter: PorterDuffColorFilter

    private lateinit var disLikeIcon: Drawable

    private lateinit var skeletonScreen: ViewSkeletonScreen

    override fun onCreateCollapsingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.
            inflate(inflater ,R.layout.fragment_servicedetail, container, false )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        skeletonScreen = Skeleton.bind(binding.root)
            .load(R.layout.fragment_servicedetail_skeleton)
            .shimmer(true)
            .show()

        val upClickListener: (View) -> Unit = { findNavController().navigateUp() }
        getToolbar().showUpIcon(true, upClickListener)
        binding.imageUp.setOnClickListener(upClickListener)

        disLikeIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_filled_like)!!.mutate()

        val shareIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_share)!!.mutate()

        val colorBlack = ContextCompat.getColor(context!!, R.color.black)
        blackColorFilter = PorterDuffColorFilter(colorBlack, PorterDuff.Mode.SRC_IN)

        disLikeIcon.colorFilter = blackColorFilter
        shareIcon.colorFilter = blackColorFilter

        getToolbar().setFirstIcon(disLikeIcon, this::onLikeIconClicked)
        getToolbar().setSecondIcon(shareIcon, this::onShareIconClicked)

        getLikeIcon().setOnClickListener(this::onLikeIconClicked)
        getShareIcon().setOnClickListener(this::onShareIconClicked)

        getToolbar().setForgroundTint(ContextCompat.getColor(context!!, R.color.black))
    }

    abstract fun onLikeIconClicked(v: View)
    abstract fun onShareIconClicked(v: View)

    fun getManexCountControl() = binding.manexCountControl
    fun getIconImageView() = binding.imageIcon

    fun getTitleTextView() = binding.textTitle
    fun getDescriptionTextView() = binding.textDescription

    fun getBurnTitleTextView() = binding.burnTextTitle
    fun getBurnLinear() = binding.burnLinear
    fun getearnLinear() = binding.earnLinear
    fun getBurnDescriptionTextView() = binding.burnTextDescription

    fun getLikeIcon() = binding.iconLike
    fun getShareIcon() = binding.iconShare
    fun getManexCountText() = binding.textManexcount
    fun getSkeletonTop() = skeletonScreen
    fun getTopLayout() = binding.root

    private fun setToolbarLikedColor(isLiked: Boolean) {
        if (isLiked) {
            getToolbar().getFirstIcon()
                .setImageDrawable(PublicFunction.getDrawable(R.drawable.ic_liked, requireContext()))
        } else {
            getToolbar().getFirstIcon().setImageDrawable(
                PublicFunction.getDrawable(
                    R.drawable.ic_filled_like,
                    requireContext()
                )
            )
        }
    }

    fun setBackgroundColor(color: String) {
        try {
            var a = parseColor(color)
            root.setBackgroundColor(a)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Failed to parse color provided by remote")
        }
    }

    fun setImageUpColor(color: String) {
        val parseColor = parseColor(color)
        binding.imageUp.setColorFilter(parseColor)
    }

    private fun setForegroundColor(color: String) {
        try {
            val parseColor = parseColor(color)

            binding.textTitle.setTextColor(parseColor)
            binding.burnTextTitle.setTextColor(parseColor)
            binding.textDescription.setTextColor(parseColor)
            binding.burnTextDescription.setTextColor(parseColor)
            binding.textManexcount.setTextColor(parseColor)
            val coinDrawable =
                ContextCompat.getDrawable(context!!, R.drawable.ic_manex_coin_small)!!.mutate()
            coinDrawable.colorFilter = PorterDuffColorFilter(parseColor, PorterDuff.Mode.SRC_IN)
            binding.textManexcount.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, coinDrawable, null
            )
            binding.imageUp.setColorFilter(parseColor)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Failed to parse color provided by remote")
        }
    }

    fun setCollapsingTextColor(blackTheme: Boolean) {
        if (blackTheme) {
            setForegroundColor("000000")
        } else
            setForegroundColor("ffffff")
    }

    fun hideManexCoinOfDescryption() {
        binding.textDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    fun setLikeVisibility(isLike: Boolean, animate: Boolean = false) {

        setToolbarLikedColor(isLike)

        if (isLike) {
            val color = ContextCompat.getColor(context!!, R.color.like_icon)
            binding.iconLike.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            if (animate) {
                anim_like.visibility = View.VISIBLE
                anim_like.playAnimation()
                anim_like.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        anim_like.visibility = View.GONE
                    }
                })
            }
        } else {
            val color = ContextCompat.getColor(context!!, R.color.like_icon_disable)
            binding.iconLike.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    fun setImageSlider(items: List<String>) {
        binding.imageIconCard.visibility = View.GONE
        binding.viewpagerSlider.visibility = View.VISIBLE
        binding.viewpagerCounter.visibility = View.VISIBLE
        root.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            PublicFunction.convertDpToPixels(300f, requireContext())
        )
        setImageSliderAdapter(items)
    }

    private fun setImageSliderAdapter(items: List<String>) {

        var array = arrayListOf<String>()
        for (item: String in items)
            array.add(item)
        val imageSliderAdapter = ImageSliderAdapter(childFragmentManager,array)

        imageSliderAdapter.setData(items.reversed())

        if (binding.viewpagerSlider.adapter != imageSliderAdapter) {
            binding.viewpagerSlider.adapter = imageSliderAdapter
        }

        binding.viewpagerCounter.text = 1.toString() + "/" + items.size


        for (i in 0 until imageSliderAdapter.count)
            root?.setOnClickListener {

            }

        binding.viewpagerSlider.setCurrentItem(items.size - 1, false)

        binding.viewpagerSlider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.viewpagerCounter.text = (items.size - position).toString() + "/" + items.size
            }

        })
    }

    fun likeInvisible() {
        binding.cardLike.visibility = View.GONE
        getToolbar().setFirstIconVisibility(false)
    }


    fun showNoInternetLayout(){
        getTopLayout().visibility = View.GONE
        getNoNetLayout()?.visibility = View.VISIBLE
        getAppBar().visibility = View.INVISIBLE
        getNestedScroll().visibility = View.GONE
    }

    fun hideNoInternetLayout(){
        getTopLayout().visibility = View.VISIBLE
        getNoNetLayout()?.visibility = View.GONE
        getAppBar().visibility = View.VISIBLE
        getNestedScroll().visibility = View.VISIBLE
    }

}


