package com.baman.manex.data.model

enum class FaqType (val value: String, val code: Int) {

    undefine("undefine",0),
    User("User",1),
    Partner("Partner",2);

    companion object {

        fun Parse(value: String?): FaqType {
            if (value == null) {
                return FaqType.undefine
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return FaqType.undefine
        }

        fun Parse(code: Int): FaqType {
            if (code == 0) {
                return FaqType.undefine
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return FaqType.undefine
        }
    }

}