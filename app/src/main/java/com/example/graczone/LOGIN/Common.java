package com.example.graczone.LOGIN;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class Common {
    public static boolean isConnectedToInternet(Context context) {
        Log.d("myTag", "call is connectedToInterner");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null) {
//            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i<info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        Log.d("myTag", "connected to internet");
//                        return true;
//                    }
//                }
//            }
//        }
//        Log.d("myTag", "not connected");
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
