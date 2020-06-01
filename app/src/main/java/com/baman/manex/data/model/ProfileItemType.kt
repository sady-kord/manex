package com.baman.manex.data.model

enum class ProfileItemType (val value: String, val code: Int){

    UNDEFINE("UNDEFINE", 0),
    INVITE_FRIEND("INVITE_FRIEND", 100),
    MESSAGES("MESSAGES", 110),
    MANEX_PLUS("MANEX_PLUS" , 120),
    SUPPORT_CENTER("SUPPORT_CENTER" , 140),
    HELP_AND_QUESTIONS("HELP_AND_QUESTIONS" , 150),
    TERMS_AND_CONDITION("TERMS_AND_CONDITION" , 160),
    ABOUT_US("ABOUT_US" , 170),
    COOPERATIONS("COOPERATIONS" , 180),
    SETTING("SETTING" , 190);

    companion object {

        fun Parse(value: String?): ProfileItemType {
            if (value == null) {
                return UNDEFINE
            }
            val `arr$` = ProfileItemType.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return UNDEFINE
        }

        fun Parse(code: Int): ProfileItemType {
            if (code == 0) {
                return UNDEFINE
            }
            val `arr$` = ProfileItemType.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return UNDEFINE
        }
    }

}