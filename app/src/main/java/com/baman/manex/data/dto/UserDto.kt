package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.data.model.GenderType
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@TypeConverters(OperatorTypeConverters::class)
@Parcelize
@Entity
data class UserDto (

    @Expose
    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0,

    @Expose
    @SerializedName("displayName")
    var displayName : String? = null ,

    @Expose
    @SerializedName("lastLoginDate")
    var lastLoginDate: String? = null,

    @Expose
    @SerializedName("registerDate")
    var registerDate: String? = null,

    @Expose
    @SerializedName("userId")
    var userId: Int = 0,

    @Expose
    @SerializedName("mobile")
    var mobile: String? = null,

    @Expose
    @SerializedName("lastName")
    var lastName: String? = null,

    @Expose
    @SerializedName("firstName")
    var firstName: String? = null,

    @Expose
    @SerializedName("latitude")
    var latitude : Double ? = null,

    @Expose
    @SerializedName("longitude")
    var longitude : Double? = null,

    @Expose
    @SerializedName("totalManex")
    var totalManex : Int? = null,

    @Expose
    @SerializedName("userManex")
    var userManex : Int? = null,

    @Expose
    @SerializedName("watingManex")
    var watingManex : Int? = null,

    @Expose
    @SerializedName("userHasCard")
    var userHasCard : Boolean ? = false,

    @Expose
    @SerializedName("gender")
    var gender : Int? = null,

    @Expose
    @SerializedName("jobTitle")
    var jobTitle : String? = null,

    @Expose
    @SerializedName("day")
    var day : Int ? = null,

    @Expose
    @SerializedName("month")
    var month : Int ? = null,

    @Expose
    @SerializedName("year")
    var year : Int? = null,

    @Expose
    @SerializedName("email")
    var email : String? = null,

    @Expose
    @SerializedName("postalCode")
    var postalCode : String? = null,

    @Expose
    @SerializedName("address")
    var address : String? = null,

    @Expose
    @SerializedName("userProfileImageUrl")
    var userProfileImageUrl : String? = null ,

    @Expose
    @SerializedName("profileImageFileId")
    var profileImageFileId : Long = 0 ,

    @Expose
    @SerializedName("profileImageFileUrl")
    var profileImageFileUrl : String? = null

) : Parcelable
