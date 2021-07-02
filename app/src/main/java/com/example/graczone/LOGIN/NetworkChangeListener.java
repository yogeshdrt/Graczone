package com.example.graczone.LOGIN;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.graczone.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myTag", "call on Receive method");
        if (!Common.isConnectedToInternet(context)) {
            Log.d("myTag", "start alert method");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialog_layout = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
            builder.setView(dialog_layout);
            AppCompatButton button = dialog_layout.findViewById(R.id.noBtn);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);
            button.setOnClickListener(v -> {
                dialog.dismiss();
                onReceive(context, intent);
            });
            Log.d("myTag", "after alert dialog in internet");

        }
    }
}
