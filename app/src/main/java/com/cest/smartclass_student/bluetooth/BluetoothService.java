package com.cest.smartclass_student.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import com.cest.smartclass_student.support.Util;
import com.google.gson.JsonArray;

import java.util.HashMap;

/**
 * Created by pc on 2015-08-02.
 */
public class BluetoothService {

    public static final int STATE_IDLE = 1;
    public static final int STATE_SCANNING = 2;
    public static final int STATE_FOUND = 3;

    public static final int START_SCAN  = 1;
    public static final int FOUND_MAC_ADDRESS  = 2;
    public static final int STOP_SCAN  = 3;
    public static final int NOT_FOUND_MAC_ADDRESS  = 4;
    public static final int INDOOR = 5;
    public static final int OUTDOOR = 6;

    private int state;
    private BluetoothAdapter adapter;
    private static final long SCAN_PERIOD = 10000;
    private Handler handler;
    MScanCallback callback;
    int c_classid;
    HashMap<String,Mac_info> mm;
    Mac_info Max_mac_info;

    private static BluetoothService instance;

    public static BluetoothService getInstance(Context context,Handler handler,int c_classid){
        if(instance ==null){
            instance = new BluetoothService(context);
        }
        instance.init(handler,c_classid);
        return instance;
    }
    private BluetoothService(Context context){
        adapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        callback = new MScanCallback();
        mm = new HashMap<String,Mac_info>();

    }

    private void init(Handler handler,int c_classid) {
        this.handler = handler;
        this.c_classid = c_classid;
        callback.init(handler);
    }

    public boolean isEnabled() {
        if (adapter == null || !adapter.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }
    public void setMac(JsonArray ja){
        mm.clear();
        for(int i =0,size=ja.size();i<size;i++){
            Mac_info mi = new Mac_info();
            mi.setMac(ja.get(i).getAsJsonObject().get("mac_address").getAsString());
            mi.setType(ja.get(i).getAsJsonObject().get("type").getAsInt());
            mi.setFound(false);
            mm.put(mi.getMac(),mi);
        }
    }

    public void scanDevice(final boolean enable) {

        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanDevice(false);
                }
            }, SCAN_PERIOD);
            state = STATE_SCANNING;
            adapter.startLeScan(callback);
            handler.sendEmptyMessage(START_SCAN);
        } else {
            if(state==STATE_FOUND){
                state = STATE_IDLE;
                if(Max_mac_info.getType()==1){
                    //in
                    handler.sendEmptyMessage(INDOOR);
                }else{
                    //out
                    handler.sendEmptyMessage(OUTDOOR);
                }

            }else if(state == STATE_SCANNING){
                state = STATE_IDLE;
                handler.sendEmptyMessage(NOT_FOUND_MAC_ADDRESS);
            }
            adapter.stopLeScan(callback);
            handler.sendEmptyMessage(STOP_SCAN);
        }
    }


    public class MScanCallback implements BluetoothAdapter.LeScanCallback {



        Handler handler;

        public void init(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Util.log("found mac : " + bluetoothDevice.getAddress());
            String mac = bluetoothDevice.getAddress();
            Mac_info mi = mm.get(mac);
            if(mi!=null){
                state = STATE_FOUND;
                mi.setRssi(i);
                if(Max_mac_info==null){
                    Max_mac_info = mi;
                }else{
                    if(mi.getRssi() > Max_mac_info.getRssi()){
                        Max_mac_info = mi;
                    }
                }
            }

        }


    }




    public int getC_classid() {
        return c_classid;
    }



    private class Mac_info{
        String mac;
        int type;
        boolean found;
        int rssi;

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isFound() {
            return found;
        }

        public void setFound(boolean found) {
            this.found = found;
        }
    }


}
