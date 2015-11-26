package com.cest.smartclass_student.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cest.smartclass_student.LoginActivity;
import com.cest.smartclass_student.R;
import com.google.android.gms.gcm.GcmListenerService;


public class GCMListenerService extends GcmListenerService {

    public static final int NOTFICATION_ID = 1000;
    @Override
    public void onMessageReceived(String from, Bundle data){
        String title  = data.getString("title");
        String content = data.getString("content");
        setNotification(title,content);
    }


    private void setNotification(String title, String content){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setTicker("Smart Class");
        mBuilder.setSmallIcon(R.drawable.smartclass);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pi);

        nm.notify(NOTFICATION_ID,mBuilder.build());
    }
}
