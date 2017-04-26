package com.beacon.drunkengranite.forwardspam;

import android.Manifest;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by drunkengranite on 1/24/17.
 */
// todo not hack the executable thread
public class CallService extends IntentService {
    ArrayList<String> blockedNumbers;
    HashMap<String, Object> badActions;
    ArrayList<String> coughtNumbers;

    public CallService() {
        super("CallService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (telephonyManager == null) {
                // this will be easier for debugging later on
                throw new NullPointerException("tm == null");
            }
            // do reflection magic
            telephonyManager.getClass().getMethod("answerRingingCall").invoke(telephonyManager);

        } catch (Exception e) {
            // we catch it all as the following things could happen:
            // NoSuchMethodException, if the answerRingingCall() is missing
            // SecurityException, if the security manager is not happy
            // IllegalAccessException, if the method is not accessible
            // IllegalArgumentException, if the method expected other arguments
            // InvocationTargetException, if the method threw itself
            // NullPointerException, if something was a null value along the way
            // ExceptionInInitializerError, if initialization failed
            // something more crazy, if anything else breaks

            // TODO decide how to handle this state
            // you probably want to set some failure state/go to fallback
            System.out.println(e);

        }

        PhoneStateListener listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        if(incomingNumber.equals("7072800526")){
                            acceptCall();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                }
            }
        };
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getDataString()) {
            case "init": {
                System.out.println("initialized listener");
            }
            case "blockAll": {
                System.out.println("Caught");
            }
            case "blockSelected": {
                System.out.println("Caught");
            }
            case "changePunishment": {
                System.out.println("Caught");
            }
        }
    }


    public boolean isBlockedNumber(String phoneNumber) {
        return blockedNumbers.contains(phoneNumber);
    }

    // http://stackoverflow.com/questions/26924618/how-can-incoming-calls-be-answered-programmatically-in-android-5-0-lollipop
    public void endCallForwarding(){






    }

    private void acceptCall() {

//        // for HTC devices we need to broadcast a connected headset
//        boolean broadcastConnected = MANUFACTURER_HTC.equalsIgnoreCase(Build.MANUFACTURER)
//                && !audioManager.isWiredHeadsetOn();
//
//        if (broadcastConnected) {
//            broadcastHeadsetConnected(false);
//        }

        try {
            try {
                Runtime.getRuntime().exec("input keyevent " +
                        Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

            } catch (IOException e) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_HEADSETHOOK));
                Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_HEADSETHOOK));

                sendOrderedBroadcast(btnDown, enforcedPerm);
                sendOrderedBroadcast(btnUp, enforcedPerm);
            }
        } finally {

            System.out.println("sonofabitch");
//          if (broadcastConnected) {
//                broadcastHeadsetConnected(false);
//            }
        }
    }



//    public void setUpConditionalCallForwarding(){
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        String prefix = "*63";
//        prefix = Uri.encode(prefix);
//        intent.setData( Uri.parse("tel:"+prefix+", 3, 1234, #, 1"));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        startActivity(intent);
//    }
//
//
//    public void dispatchForward(String phoneNumber) {
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        String prefix = "*63";
//        prefix = Uri.encode(prefix);
//        intent.setData( Uri.parse("tel:"+prefix+", 3, 1234, #, 1"));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        startActivity(intent);
//    }




}
