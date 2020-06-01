package com.baman.manex.data.model

enum class ContactType(val value: String, val code: Int) {

    undefine("undefine",0),
    Mobile("Mobile",1),
    Phone("Phone",2),
    Email("Email",3),
    SiteAddress("SiteAddress",4),
    Telegram("Telegram",5),
    Instagram("Instagram",6),
    Twitter("Twitter",7),
    Linkedin ("Linkedin",8);

    companion object {

        fun Parse(value: String?): ContactType {
            if (value == null) {
                return ContactType.undefine
            }
            val `arr$` = ContactType.values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return ContactType.undefine
        }

        fun Parse(code: Int): ContactType {
            if (code == 0) {
                return ContactType.undefine
            }
            val `arr$` = ContactType.values()
            for (`val` in `arr$`) {
                if (`val`.code == code) {
                    return `val`
                }
            }
            return ContactType.undefine
        }
    }

}