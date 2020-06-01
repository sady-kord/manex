package com.baman.manex.data.dto

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OnBoardingSlideDto(

    val id: String,

    val imagePath: Int,

    val title: String,

    val subtitle: String

): Parcelable
