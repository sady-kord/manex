package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(OperatorTypeConverters::class)
@Entity
data class VoucherDetailDto(

    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0,

     @SerializedName("name")
     var name : String? = null ,

    @SerializedName("shortName")
    var shortName : String? = null ,

    @SerializedName("title")
    var title : String? = null ,

    @SerializedName("subtitle")
    var subtitle : String? = null ,

    @SerializedName("imageUrl")
    var imageUrl : String? = null ,

    @SerializedName("discount")
    var discount : Int? = null ,

    @SerializedName("expier")
    var expier : String? = null ,

    @SerializedName("manexCount")
    var manexCount : Int? = null ,

    @SerializedName("progressValue")
    var progressValue : Int? = null ,

    @SerializedName("likeStatus")
    var likeStatus : Boolean ,

    @SerializedName("userCanBuy")
    var userCanBuy : Boolean ,

    @SerializedName("manexCountNeed")
    var manexCountNeed : Int? = null ,

    @SerializedName("backgroundColor")
    var backgroundColor: String? = null ,

    @SerializedName("isBlackTheme")
    var isBlackTheme : Boolean ,

    @SerializedName("statusBarColor")
    var statusColor : String? = null ,

    @SerializedName("useVoucherConditions")
    var useVocherConditions : List<String> ,

    @SerializedName("voucherSlogans")
    var voucherSlogans: List<BranchSloganDto> ,

    @SerializedName("voucherCode")
    var voucherCode : String? = null ,

    @SerializedName("ShareLink")
    var shareLink : String? = null ,

    @SerializedName("linkText")
    var linkText : String? = null ,

    @SerializedName("link")
    var link : String? = null

) : Parcelable


