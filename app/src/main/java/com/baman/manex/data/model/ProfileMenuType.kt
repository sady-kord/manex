package com.baman.manex.data.model

enum class ProfileMenuType (val value: String, val code: Int){

    UNDEFINE("UNDEFINE", 0),
    DivisionItem("DivisionItem", 1),
    MenuItem("MenuItem", 2);

    companion object {

        fun Parse(value: String?): ProfileMenuType {
            if (value == null) {
                return UNDEFINE
            }
            val `arr$` = ProfileMenuType.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return UNDEFINE
        }

        fun Parse(code: Int): ProfileMenuType {
            if (code == 0) {
                return UNDEFINE
            }
            val `arr$` = ProfileMenuType.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return UNDEFINE
        }
    }

}