package com.baman.manex.data.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baman.manex.db.OperatorTypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
@TypeConverters(OperatorTypeConverters::class)
data class StoreInfoDto(

//    "id":0,
//    "partnerId":0,
//"name":"string",
//"shortName":"string",
//"branchName":"string",
//"address":"string",
//"latitude":0,
//"longitude":0,
//"imagePath":"string",
//"description":"string",
//"about":"string",
//"tips":[],
//"earnManexConditionTitle":{},
//"earnManexConditions":[],
//"colorHex":"string",
//"receiverCode":"string",
//"locationImageUrl":"string",
//"locationImageFileId":"string",
//"backgroundColor":"string",
//"statusBarColor":"string",
//"isBlackTheme":true,
//"branchType":"Offline",
//"branchSlogan":[],
//"earnManexTips":[],
//"burnManexTips":[],
//"workTimes":[],
//"searchBy":"Non",
//"burnManexConditionTitle":{},
//"burnManexConditions":[],
//"contacts":[]

    @PrimaryKey
    @SerializedName("id")
    var id: Long,

    @SerializedName("partnerId")
    var partnerId: Long,

    @SerializedName("name")
    var name: String,

    @SerializedName("shortName")
    var shortName: String,

    @SerializedName("branchName")
    var branchName: String,

    @SerializedName("address")
    var address: String,

    @SerializedName("longitude")
    var longitude: Double,

    @SerializedName("latitude")
    var latitude: Double,

    @SerializedName("imagePath")
    var imagePath: String,

    @SerializedName("siteUrl")
    var siteUrl: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("earnManexConditionTitle")
    var earnManexConditionTitle: ManexConditionsDto,

    @SerializedName("earnManexConditions")
    var earnManexConditions: List<ManexConditionsDto>,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("backgroundColor")
    var backgroundColor: String,

    @SerializedName("statusBarColor")
    var statusColor: String,

    @SerializedName("isBlackTheme")
    var isBlackTheme : Boolean,

    @SerializedName("branchSlogan")
    var branchSlogan: List<BranchSloganDto>,

    @SerializedName("earnManexTips")
    var earnManexTips: List<String>,

    @SerializedName("tips")
    var tips: List<String>,

    @SerializedName("burnManexTips")
    var burnManexTips: List<String>,

    @SerializedName("burnManexConditionTitle")
    var burnManexConditionTitle: ManexConditionsDto,

    @SerializedName("burnManexConditions")
    var burnManexConditions: List<ManexConditionsDto>,

    @SerializedName("otherBranches")
    var otherBranchs: List<OtherBranchDto>,

    @SerializedName("similarBranches")
    var similarBranches: List<OnlineStoreInsideDto>,

    @SerializedName("nearBranchs")
    var nearBranchs: List<OnlineStoreInsideDto>,

    @SerializedName("workTimes")
    var workTimes: List<WorkTimesDto>,

    @SerializedName("contacts")
    var contacts: List<ContactsDto>,

    @SerializedName("instagramUrl")
    var instagramUrl: String,

    @SerializedName("locationImageUrl")
    var locationImageUrl: String,

    @SerializedName("textColor")
    var textColor: String,

    @SerializedName("colorHex")
    var colorHex: String,

    @SerializedName("about")
    var about: String,

    @SerializedName("likeStatus")
    var likeStatus : Boolean

) : Parcelable