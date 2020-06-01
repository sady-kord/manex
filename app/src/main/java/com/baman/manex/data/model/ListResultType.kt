package com.baman.manex.data.model

import android.content.Context
import com.baman.manex.R

enum class ListResultType(val value: String, val code: Int) {

    undef("تعین نشده",0),
    Basic("Basic",1),
    SearchByAddress("SearchByAddress",2),
    SearchByName ("SearchByName",3);

    fun getTypeName (context : Context) : String{
        return when (this) {
            undef -> context.resources.getString(R.string.undef)
            Basic -> context.resources.getString(R.string.list_result_basic)
            SearchByAddress -> context.resources.getString(R.string.offlinestoresearch_listheader_address)
            SearchByName -> context.resources.getString(R.string.offlinestoresearch_listheader_name)
        }
    }

    companion object {

        fun Parse(value: String?): ListResultType {
            if (value == null) {
                return ListResultType.undef
            }
            val `arr$` = ListResultType.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return ListResultType.undef
        }

        fun Parse(code: Int): ListResultType {
            if (code == 0) {
                return ListResultType.undef
            }
            val `arr$` = ListResultType.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return ListResultType.undef
        }
    }
}