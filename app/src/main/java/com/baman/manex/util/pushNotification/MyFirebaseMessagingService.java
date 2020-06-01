package com.baman.manex.util.pushNotification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    /**
     * Called when message is received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        try {
            NotificationHelper.prepareNotification(getApplicationContext(), remoteMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        try {
//            String oldToken = MyPreferences.readString(getApplicationContext(), MyPreferences.FIREBASE_TOKEN, "");
//            if (!oldToken.equals(token)) {
//                MyPreferences.writeString(getApplicationContext(), MyPreferences.FIREBASE_TOKEN, token);
//                MyPreferences.writeBoolean(getApplicationContext(), MyPreferences.FIREBASE_TOKEN_REGISTERED, false);
//                sendRegistrationToServer(token);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void sendRegistrationToServer(String token) {
//
//        @SuppressLint("HardwareIds")
//        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        MyApplication.getVaslAppClient().pushFCMServiceV1().setToken(androidId, token, new ResponseProtoHandler<PushFCMGlobal.SetToken>() {
//            @Override
//            public void onSuccess(PushFCMGlobal.SetToken setToken) {
//                Log.i(TAG, "onSuccess: " + setToken.getMsg());
//                MyPreferences.writeBoolean(getApplicationContext(), MyPreferences.FIREBASE_TOKEN_REGISTERED, true);
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                Log.i(TAG, "onFailure: " + s);
//                MyPreferences.writeBoolean(getApplicationContext(), MyPreferences.FIREBASE_TOKEN_REGISTERED, false);
//            }
//        });
//
//        MyApplication.getVaslAppClient().pushFCMServiceV1().addTopics(androidId,
//                null,
//                null,
//                null,
//                new ResponseProtoHandler<PushFCMGlobal.SetToken>() {
//                    @Override
//                    public void onSuccess(PushFCMGlobal.SetToken result) {
//                        Log.i(TAG, "onSuccess: " + result.getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(int errorCode, String errorMsg) {
//                        Log.i(TAG, "onFailure: " + errorMsg);
//                    }
//                });
//    }
}
