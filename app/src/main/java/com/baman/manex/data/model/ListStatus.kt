package com.baman.manex.data.model

enum class ListStatus(val value: String, val code: Int) {

    UNDEFINE("UNDEFINE", 0),
    CONNECTION_FAILED("FAILURE", 1),
    SUCCESS("SUCCESS", 2),
    EMPTY("EMPTY" , 3),
    SEARCH_EMPTY("SEARCH_EMPTY" , 4),
    LOADING_BOTTOM("LOADING_BOTTOM" , 5),
    FIRST_SEARCH_STEP("FIRST_SEARCH_STEP" , 6);

    companion object {

        fun Parse(value: String?): ListStatus {
            if (value == null) {
                return UNDEFINE
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return UNDEFINE
        }

        fun Parse(code: Int): ListStatus {
            if (code == 0) {
                return UNDEFINE
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return UNDEFINE
        }
    }
}