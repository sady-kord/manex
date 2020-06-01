package com.baman.manex.ui.home.exoVideoPlayer

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.baman.manex.R
import com.baman.manex.ui.baseClass.BaseActivity
import com.potyvideo.library.AndExoPlayerView
import javax.inject.Inject


class VideoPlayerActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val TEST_URL_MP4 =
        "https://dev3.web.manexapp.com/video.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video_player)

        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(VideoPlayerViewModel::class.java)

        val andExoPlayerView: AndExoPlayerView = findViewById(R.id.andExoPlayerView)


//        if(intent.hasExtra("url")){
//            andExoPlayerView.setSource(intent.getStringExtra("url"))
//        }
//
        andExoPlayerView.setSource(TEST_URL_MP4)

        val closeLayout: RelativeLayout = findViewById(R.id.icon_close_layout)
        closeLayout.setOnClickListener {
            onBackPressed()
        }

        val close: CardView = findViewById(R.id.icon_close)
        close.setOnClickListener {
            onBackPressed()
        }

    }
}