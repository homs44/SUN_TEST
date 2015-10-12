package com.cest.smartclass_student.gcm;

import com.cest.smartclass_student.C;
import com.cest.smartclass_student.support.Util;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(C.SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Util.log("onTokenRefresh  - token : " + token);
        } catch (IOException e) {

        }
    }
}
