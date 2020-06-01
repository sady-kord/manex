package com.baman.manex.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FriendDto(

    @SerializedName("title")
    var title: String,

    @SerializedName("code")
    var code: String,

    @SerializedName("sharedUrl")
    var sharedUrl: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("inviteesCount")
    var inviteesCount: Int,

    @SerializedName("verifiedInviteesCount")
    var verifiedInviteesCount: Int,

    @SerializedName("earnedManex")
    var earnedManex: Int


):Parcelable