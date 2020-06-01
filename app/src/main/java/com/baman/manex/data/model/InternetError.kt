package com.baman.manex.data.model

enum class InternetError(val value: String) {

    NoInternet ("عدم اتصال به اینترنت");

    companion object {

        fun Parse(value: String?): InternetError {
            if (value == null) {
                return NoInternet
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return NoInternet
        }

    }
}