package com.cest.smartclass_student.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.cest.smartclass_student.camera.MCamera;

/**
 * Created by pc on 2015-09-26.
 */
public class Util {
    private static final String TAG = "smart_class";

    public static void log(String str){
        Log.d(TAG,str);
    }


    public static String getWifiMacAddress(Context context){
        return ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }
    public static Bitmap rotateBitmap(Bitmap bitmap,MCamera cameraids){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if(cameraids.current ==cameraids.front){
            Util.log("front ---------------------------");
            matrix.postRotate(-90);
        }else if(cameraids.current ==cameraids.back){
            matrix.postRotate(90);
            Util.log("back ---------------------------");
        }else{

        }
        Bitmap rBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.recycle();
        return rBitmap;
    }


}
