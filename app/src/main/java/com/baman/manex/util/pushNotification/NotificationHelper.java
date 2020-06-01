package com.baman.manex.util.pushNotification;

import android.content.Context;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import ir.vasl.globalEnums.EnumNotificationAction;
import ir.vasl.globalObjects.ActionButton;
import ir.vasl.magicalnotifier.MagicalNotifier;

public class NotificationHelper {

    public static void prepareNotification(Context context, RemoteMessage remoteMessage) throws JSONException {

        String title = null;
        String subTitle = null;
        String bigPictureUrl = null;
        String bigText = null;
        String actionButtonOne = null;
        String actionButtonTwo = null;
        String actionButtonThree = null;


        Map<String, String> messageData = remoteMessage.getData();

        if (messageData.containsKey(EnumFCMNotificationKeys.TITLE.getCode()))
            title = messageData.get(EnumFCMNotificationKeys.TITLE.getCode());

        if (messageData.containsKey(EnumFCMNotificationKeys.SUB_TITLE.getCode()))
            subTitle = messageData.get(EnumFCMNotificationKeys.SUB_TITLE.getCode());

        if (messageData.containsKey(EnumFCMNotificationKeys.BIG_PICTURE.getCode()))
            bigPictureUrl = messageData.get(EnumFCMNotificationKeys.BIG_PICTURE.getCode());

        if (messageData.containsKey(EnumFCMNotificationKeys.BIG_TEXT.getCode()))
            bigText = messageData.get(EnumFCMNotificationKeys.BIG_TEXT.getCode());

        if (messageData.containsKey(EnumFCMNotificationKeys.ACTION_ONE.getCode()))
            actionButtonOne = messageData.get(EnumFCMNotificationKeys.ACTION_ONE.getCode());

        if (messageData.containsKey(EnumFCMNotificationKeys.ACTION_TWO.getCode()))
            actionButtonTwo = messageData.get(EnumFCMNotificationKeys.ACTION_TWO.getCode());

        if (messageData.containsKey(EnumFCMNotificationKeys.ACTION_THREE.getCode()))
            actionButtonThree = messageData.get(EnumFCMNotificationKeys.ACTION_THREE.getCode());

        MagicalNotifier.Builder builder = new MagicalNotifier.Builder(context);

        if (title != null)
            builder.setTitle(title);
        if (subTitle != null)
            builder.setSubTitle(subTitle);
        if (bigPictureUrl != null)
            builder.setBigPictureUrl(bigPictureUrl);
        if (bigText != null)
            builder.setBigText(bigText);

        if (actionButtonOne != null) {
            try {
                JSONObject jsonObjectActionOne = new JSONObject(actionButtonOne);
                if (jsonObjectActionOne.has(EnumFCMNotificationKeys.ACTION_TITLE.getCode()) && jsonObjectActionOne.has(EnumFCMNotificationKeys.ACTION_URL.getCode()))
                    builder.setActionButtonOne(new ActionButton(jsonObjectActionOne.getString(EnumFCMNotificationKeys.ACTION_TITLE.getCode())
                            , EnumNotificationAction.OPEN_URL
                            , jsonObjectActionOne.getString(EnumFCMNotificationKeys.ACTION_URL.getCode())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (actionButtonTwo != null) {
            try {
                JSONObject jsonObjectActionTwo = new JSONObject(actionButtonTwo);
                if (jsonObjectActionTwo.has(EnumFCMNotificationKeys.ACTION_TITLE.getCode()) && jsonObjectActionTwo.has(EnumFCMNotificationKeys.ACTION_URL.getCode()))
                    builder.setActionButtonTwo(new ActionButton(jsonObjectActionTwo.getString(EnumFCMNotificationKeys.ACTION_TITLE.getCode())
                            , EnumNotificationAction.OPEN_URL
                            , jsonObjectActionTwo.getString(EnumFCMNotificationKeys.ACTION_URL.getCode())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (actionButtonThree != null) {
            try {
                JSONObject jsonObjectActionThree = new JSONObject(actionButtonThree);
                if (jsonObjectActionThree.has(EnumFCMNotificationKeys.ACTION_TITLE.getCode()) && jsonObjectActionThree.has(EnumFCMNotificationKeys.ACTION_URL.getCode()))
                    builder.setActionButtonThree(new ActionButton(jsonObjectActionThree.getString(EnumFCMNotificationKeys.ACTION_TITLE.getCode())
                            , EnumNotificationAction.OPEN_URL
                            , jsonObjectActionThree.getString(EnumFCMNotificationKeys.ACTION_URL.getCode())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // show notification
        builder.show();

    }
}
