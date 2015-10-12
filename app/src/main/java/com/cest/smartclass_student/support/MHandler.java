package com.cest.smartclass_student.support;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by pc on 2015-08-02.
 */
public class MHandler extends Handler {
    private final WeakReference<OnHandlerMessage> handleActivity;

    public MHandler(OnHandlerMessage activity){
        handleActivity = new WeakReference<OnHandlerMessage>(activity);
    }
    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);
        OnHandlerMessage activity = (OnHandlerMessage)handleActivity.get();
        if(activity==null) return;
        activity.handleMessage(msg);

    }

}
