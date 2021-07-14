package com.example.graczone.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.File;

/**
 * Created by ${ajinkya} on ${2016-04-04}.
 */
public class ShowNote extends Activity {
    boolean isDeleted;
    private BroadcastReceiver receiver;
    private long enqueue;
    private DownloadManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("CAcompAdda");
//        builder.setIcon(R.mipmap.icon);
        builder.setMessage("Latest Version is Available. Click on OK to update");
//        builder.getContext().setTheme(R.style.AppTheme_NoActionBar);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "App Downloading...Please Wait", Toast.LENGTH_LONG).show();
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                File file = new File("/mnt/sdcard/Download/app-debug.apk");
                if (file.exists()) {

                    isDeleted = file.delete();
                    deleteAndInstall();
                } else {
                    firstTimeInstall();
                }
            }
        });
        builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShowNote.this.finish();
            }
        });
        builder.show();
    }

    private void firstTimeInstall() {
        Log.d("myTag", "May be 1st Update: " + "OR deleteed from folder");
        downloadAndInstall();
    }

    private void deleteAndInstall() {
        if (isDeleted) {
            Log.d("myTag", "Deleted Existance file: " + String.valueOf(isDeleted));
            downloadAndInstall();

        } else {
            Log.d("myTag", "NOT DELETED: " + String.valueOf(isDeleted));
            Toast.makeText(getApplicationContext(), "Error in Updating...Please try Later", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadAndInstall() {
        Uri uri = Uri.parse("https://graczone.netlify.app/app-debug.apk");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-debug.apk");

        enqueue = dm.enqueue(request);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();

                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Log.d("ainfo", uriString);

                            if (downloadId == c.getInt(0)) {
                                Log.d("DOWNLOAD PATH:", c.getString(c.getColumnIndex("local_uri")));

                                //if your device is rooted then you can install or update app in background directly
                                Toast.makeText(getApplicationContext(), "App Installing...Please Wait", Toast.LENGTH_LONG).show();
                                File file = new File("/mnt/sdcard/Download/app-debug.apk");
                                Log.d("IN INSTALLER:", "/mnt/sdcard/Download/app-debug.apk");
                                if (file.exists()) {
                                    try {
                                        String command;
                                        Log.d("IN File exists:", "/mnt/sdcard/Download/app-debug.apk");

                                        command = "pm install -r " + "/mnt/sdcard/Download/app-debug.apk";
                                        Log.d("COMMAND:", command);
                                        Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                                        proc.waitFor();
                                        Toast.makeText(getApplicationContext(), "App Installed Successfully", Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                    c.close();
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}