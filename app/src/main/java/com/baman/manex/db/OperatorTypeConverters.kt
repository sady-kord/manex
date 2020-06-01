package com.baman.manex.db

import androidx.room.TypeConverter
import com.baman.manex.data.dto.*
import com.baman.manex.data.model.GenderType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber


object OperatorTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun listIntToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: String): Map<String, String> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun mapToString(value: Map<String, String>?): String = toJson(value)

    @TypeConverter
    @JvmStatic
    fun stringToStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) {
            return emptyList()
        }
        return fromJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(items: List<String>?): String = toJson(items)

    @TypeConverter
    @JvmStatic
    fun stringToListStoreManexBusinessDto(value: String): List<StoreManexBusinessDto> =
        fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listStoreManexBusinessDtoToString(items: List<StoreManexBusinessDto>?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun StringToListFilterItemsDto(value: String): List<FilterItemsDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listFilterItemsDtoToString(items: List<FilterItemsDto>): String = toJson(items)

    @TypeConverter
    @JvmStatic
    fun StringToListContactsDto(value: String): List<ContactsDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listContactsDtoToString(items: List<ContactsDto>): String = toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToListStoreInfoDto(value: String): List<StoreInfoDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listStoreInfoDtoToString(items: List<StoreInfoDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToListSupportContactDto(value: String): List<SupportContactDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listSupportContactDtoToString(items: List<SupportContactDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToStoresInsideDto(value: String): StoresInsideDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun StoresInsideDtoToString(items: StoresInsideDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToOnlineInsideBurnDto(value: String): OnlineInsideBurnDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun OnlineInsideBurnDtoToString(items: OnlineInsideBurnDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToVocherInsideBurnDto(value: String): VoucherInsideBurnDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun VocherInsideBurnDtoToString(items: VoucherInsideBurnDto?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun stringToLocalStoresInsideDto(value: String): LocalStoresInsideDto= fromJson(value)

    @TypeConverter
    @JvmStatic
    fun LocalStoresInsideDtoToString(items: LocalStoresInsideDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToManexStoreInsideBurnDto(value: String): ManexStoreInsideBurnDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun ManexStoreInsideBurnDtoToString(items: ManexStoreInsideBurnDto?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun stringToListOnlineStoreInsideDto(value: String): List<OnlineStoreInsideDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listOnlineStoreInsideDtoToString(items: List<OnlineStoreInsideDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToOnlineStoreInsideDto(value: String): OnlineStoreInsideDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun OnlineStoreInsideDtoToString(items: OnlineStoreInsideDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToValidationDto(value: String): ValidationDto= fromJson(value)

    @TypeConverter
    @JvmStatic
    fun ValidationDtoToString(items: ValidationDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToValidationRuleDto(value: String): ValidationRuleDto= fromJson(value)

    @TypeConverter
    @JvmStatic
    fun ValidationRuleDtoToString(items: ValidationRuleDto?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun stringToVocherInsideDto(value: String): VoucherInsideDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun VocherInsideDtoToString(items: VoucherInsideDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToWalletDto(value: String): WalletDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun WalletDtoToString(items: WalletDto?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun stringToManexStoreInsideDto(value: String): ManexStoreInsideDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun ManexStoreInsideDtoToString(items: ManexStoreInsideDto?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun StringToGender(value : String?) : GenderType?{
        if (value.isNullOrEmpty())
            return null
        else
            return  GenderType.valueOf(value)
    }

    @TypeConverter
    @JvmStatic
    fun GenderToString(value : GenderType?) : String{
        return value?.name ?: ""
    }

    @TypeConverter
    @JvmStatic
    fun stringToManexConditionsDto(value: String): ManexConditionsDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun ManexConditionsDtoToString(items: ManexConditionsDto?): String =
        toJson(items)

    @TypeConverter
    @JvmStatic
    fun stringListManexConditionsDto(value: String): List<ManexConditionsDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun ListManexConditionsDtoToString(items: List<ManexConditionsDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringBranchSloganDto(value: String): List<BranchSloganDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun BranchSloganDtoToString(items: List<BranchSloganDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringWorkTimesDto(value: String): List<WorkTimesDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun WorkTimesDtoToString(items: List<WorkTimesDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringOtherBranchDto(value: String): List<OtherBranchDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun OtherBranchDtoToString(items: List<OtherBranchDto>?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun stringToMessageDto(value: String): MessageDto = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun MessageDtoToString(items: MessageDto?): String =
        toJson(items)


    @TypeConverter
    @JvmStatic
    fun StringToListVoucherDetailDto(value: String): List<VoucherDetailDto> = fromJson(value)

    @TypeConverter
    @JvmStatic
    fun listVoucherDetailDtoToString(items: List<VoucherDetailDto>): String = toJson(items)

    inline fun <reified T> toJson(value: T): String {
        return if (value == null) "" else Gson().toJson(value)
    }

    inline fun <reified T> fromJson(value: String): T {
        return Gson().fromJson(value, object : TypeToken<T>() {}.type)
    }

}

