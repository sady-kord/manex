package com.baman.manex.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baman.manex.util.glide.GlideApp
import com.google.android.material.bottomnavigation.BottomNavigationView


object BindingAdapters{

    @JvmStatic
    @BindingAdapter("onNavigationItemSelected")
    fun setOnNavigationItemSelectedListener(
        view: BottomNavigationView,
        listener: BottomNavigationView.OnNavigationItemSelectedListener
    ) {
        view.setOnNavigationItemSelectedListener(listener)

    }

    @JvmStatic
    @BindingAdapter("data_adapter")
    fun setRecyclerViewBindingAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
    }


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        imageUrl?.let {
            GlideApp.with(view)
                .load(imageUrl)
                .into(view)
        }
    }


}