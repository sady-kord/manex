package com.baman.manex.ui.burn.burnManexStore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.baman.manex.R
import com.baman.manex.ui.burn.burnManexStore.imageGalleryViewer.ImageGalleryViewerActivity
import com.baman.manex.util.glide.GlideApp


class ImageSliderAdapter(fragmentManger: FragmentManager?, var array: ArrayList<String>) :
    FragmentStatePagerAdapter(
        fragmentManger!!,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    private lateinit var data: List<String>
    override fun getItem(position: Int) =
        ImageSliderFragment.newInstance(
            data[position],
            array
        )

    override fun getCount() = if (::data.isInitialized) data.size else 0
    fun setData(items: List<String>) {
        data = items
        notifyDataSetChanged()
    }
}

class ImageSliderFragment : Fragment() {
    companion object {
        private const val KEY_BANNER_ITEM = "banner item"
        private const val KEY_BANNER_ITEMS = "banner_items"
        fun newInstance(item: String, array: ArrayList<String>): ImageSliderFragment {
            val fragment =
                ImageSliderFragment()
            val args = Bundle()
            args.putString(KEY_BANNER_ITEM, item)
            args.putStringArrayList(KEY_BANNER_ITEMS, array)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val itemView = inflater.inflate(R.layout.adapter_image_slider, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.banner_image)
        val item: String = arguments?.get(KEY_BANNER_ITEM) as String
        val items: ArrayList<String> = arguments?.get(KEY_BANNER_ITEMS) as ArrayList<String>
        GlideApp
            .with(this)
            .load(item)
            .into(imageView)
        imageView.setOnClickListener {
            var s = "test"
            val i = Intent(requireActivity(), ImageGalleryViewerActivity::class.java)
            i.putStringArrayListExtra(ImageGalleryViewerActivity.KEY_BANNER_ITEMS, items)
            startActivity(i)
        }
        return itemView
    }
}