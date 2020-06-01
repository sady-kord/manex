package com.baman.manex.data.model

import android.content.Context
import com.baman.manex.R

enum class GenderType(val value: String, val code: Int) {
    undef("تعین نشده",0),
    Male("مرد",1),
    Female ("زن",2);

    fun getGenderName (context : Context) : String{
        return when (this) {
            undef -> context.resources.getString(R.string.undef)
            Male -> context.resources.getString(R.string.male)
            Female -> context.resources.getString(R.string.female)
        }
    }

    companion object {

        fun Parse(value: String?): GenderType {
            if (value == null) {
                return GenderType.undef
            }
            val `arr$` = GenderType.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return GenderType.undef
        }

        fun Parse(code: Int): GenderType {
            if (code == 0) {
                return GenderType.undef
            }
            val `arr$` = GenderType.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return GenderType.undef
        }
    }
}