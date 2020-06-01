package com.baman.manex.util.pushNotification;

public enum EnumFCMNotificationKeys {

    undefined("undefine"),
    TITLE("key_title"),
    SUB_TITLE("key_sub_title"),
    BIG_PICTURE("key_big_picture"),
    BIG_TEXT("key_big_text"),
    ACTION_ONE("key_action_one_url"),
    ACTION_TWO("key_action_two_url"),
    ACTION_THREE("key_action_three_url"),
    FORCE("key_force"),
    ACTION_TITLE("action_title"),
    ACTION_URL("action_url"),
    ;

    private String value;

    EnumFCMNotificationKeys(String value) {
        this.value = value;
    }

    public String getCode() {
        return value;
    }

    public static EnumFCMNotificationKeys Parse(String value) {
        if (value == null) {
            return undefined;
        }
        EnumFCMNotificationKeys[] arr$ = values();
        for (EnumFCMNotificationKeys val : arr$) {
            if (val.value.equals(value)) {
                return val;
            }
        }
        return undefined;
    }


}
