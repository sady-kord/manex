package com.baman.manex.ui.burn.burnManexStore.imageGalleryViewer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.airbnb.lottie.LottieAnimationView
import com.baman.manex.R
import com.baman.manex.util.PublicFunction
import com.veinhorn.scrollgalleryview.ScrollGalleryView
import com.veinhorn.scrollgalleryview.builder.GallerySettings
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader
import kotlinx.android.synthetic.main.fragment_servicedetail.*
import ogbe.ozioma.com.glideimageloader.dsl.DSL.image
import javax.inject.Inject


class ImageGalleryViewerActivity : AppCompatActivity() {

    private var iconLike: LottieAnimationView? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery_viewer)


        var likeIcon: ImageView = findViewById(R.id.icon_like)
        var shareIcon = findViewById<ImageView>(R.id.icon_share)
        var counterText = findViewById<AppCompatTextView>(R.id.counter_text)

        iconLike = findViewById(R.id.anim_like)
        findViewById<CardView>(R.id.icon_close).setOnClickListener {
            finish()
        }

        shareIcon.setOnClickListener {
            PublicFunction.shareLinkDialog(this, "www.google.com")
        }

        likeIcon.setOnClickListener {
            like()
        }


        var items = intent.getStringArrayListExtra(KEY_BANNER_ITEMS)

        var galleryView = ScrollGalleryView
            .from(findViewById(R.id.scroll_gallery_view))
            .settings(
                GallerySettings
                    .from(supportFragmentManager)
                    .thumbnailSize(100)
                    .enableZoom(true)
                    .build()
            )
            .onPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    var counter = position + 1
                    counterText.text = "${items.size} / $counter"
                }

                override fun onPageSelected(position: Int) {

                }
                override fun onPageScrollStateChanged(state: Int) {}
            })


        for (o: String in items) {
            galleryView.add(image(o))
        }

        galleryView.build()
    }

    private fun like() {
        var isLike = true
        var animate = true
        if (isLike) {
            val color = ContextCompat.getColor(this, R.color.like_icon)
            iconLike?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
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
            val color = ContextCompat.getColor(this, R.color.like_icon_disable)
            iconLike?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }


    companion object {
        const val KEY_BANNER_ITEMS = "banner_items"
    }
}
