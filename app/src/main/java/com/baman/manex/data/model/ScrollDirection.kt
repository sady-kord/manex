package com.baman.manex.data.model

enum class ScrollDirection(val value: String, val code: Int) {

    UNDEFINE("undefine", 0),
    UP("UP", 1),
    DOWN("DOWN", 2);

    companion object {

        fun Parse(value: String?): ScrollDirection {
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

        fun Parse(code: Int): ScrollDirection {
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