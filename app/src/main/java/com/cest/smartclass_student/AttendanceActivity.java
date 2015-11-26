package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cest.smartclass_student.bluetooth.BluetoothService;
import com.cest.smartclass_student.dialog.MProgressDialog;
import com.cest.smartclass_student.http.APIManager;
import com.cest.smartclass_student.support.MHandler;
import com.cest.smartclass_student.support.OnHandlerMessage;
import com.cest.smartclass_student.support.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class AttendanceActivity extends Activity implements OnHandlerMessage {

    MHandler handler = new MHandler(this);
    BluetoothService bs;
    JsonObject obj;
    MProgressDialog mpd;

    private static final int REQUEST_CODE_CAMERA = 1;

    @Bind(R.id.attendance_back)
    ImageView iv_back;
    @Bind(R.id.attendance_message)
    TextView tv_message;
    @Bind(R.id.attendance_auth)
    RelativeLayout rl_auth;

    @OnClick({R.id.attendance_auth, R.id.attendance_back})
    void onClick(View v) {
        if (v.getId() == R.id.attendance_back) {
            finish();
        } else {
            processAttendance(obj);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);

        Intent i = getIntent();
        obj = new JsonParser().parse(i.getStringExtra("class")).getAsJsonObject();
        Util.log("received class = " + obj.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void processAttendance(JsonObject obj) {
        int c_classid = obj.get("c_classid").getAsInt();
        String mac = obj.get("mac_address").getAsString();
        String device = Util.getWifiMacAddress(getApplicationContext());
        bs = BluetoothService.getInstance(getApplicationContext(), handler, c_classid);

        startProgress();
        APIManager.getInstance().getAPI().isEnableAttendance(c_classid, device, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                JsonObject json = jsonElement.getAsJsonObject();
                int code = json.get("code").getAsInt();
                if (code == 200) {
                    if (bs.isEnabled()) {
                        JsonArray ja = json.get("mac_addresses").getAsJsonArray();
                        bs.setMac(ja);
                        bs.scanDevice(true);
                    } else {
                        endProgress();
                        Toast.makeText(getApplicationContext(), "블루투스를 활성화 하세요.", Toast.LENGTH_SHORT).show();
                    }
                } else if (code == 300) {
                    endProgress();
                    Toast.makeText(getApplicationContext(), "이미 출석을 불렀습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    endProgress();
                    Toast.makeText(getApplicationContext(), "출석을 부르지 않았습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mpd.dismiss();
            }
        });
    }



    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BluetoothService.FOUND_MAC_ADDRESS:

                //Util.log("MenuActivity - handleMessage - FOUND_MAC_ADDRESSES : " + bs.getMac_addresses());
                break;
            case BluetoothService.START_SCAN:
                Util.log("MenuActivity - handleMessage - START_SCAN");
                break;

            case BluetoothService.STOP_SCAN:
                endProgress();
                Util.log("MenuActivity - handleMessage - STOP_SCAN");
                break;
            case BluetoothService.NOT_FOUND_MAC_ADDRESS:
                //Util.log("MenuActivity - handleMessage - NOT_FOUND_MAC_ADDRESSES : " + bs.getMac_addresses());
                Toast.makeText(getApplicationContext(), "주변에 선택한 수업과 일치하는 비콘정보가 없습니다.", Toast.LENGTH_SHORT).show();
                break;

            case BluetoothService.INDOOR:
                Intent i = new Intent(AttendanceActivity.this, CameraActivity.class);
                startActivityForResult(i, REQUEST_CODE_CAMERA);
                break;
            case BluetoothService.OUTDOOR:
                Toast.makeText(getApplicationContext(), "강의실 안에서만 출석을 할 수 있습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                String path = intent.getStringExtra("path");
                sendAttendance(path);
                Util.log("upload image");
                // Toast.makeText(getApplicationContext(),"출석 완료",Toast.LENGTH_SHORT).show();
            } else {
                Util.log("nothing");

            }
        }

    }

    public void sendAttendance(String path) {
        int c_classid = obj.get("c_classid").getAsInt();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int c_userid = mPref.getInt(C.PREF_KEY_C_USERID, -1);
        String device = Util.getWifiMacAddress(getApplicationContext());
        File file = new File(path);
        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        String filename = file.getName();
        startProgress();
        APIManager.getInstance().getAPI().sendAttendance(typedFile, filename, c_classid, c_userid, device, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                endProgress();
                JsonObject json = jsonElement.getAsJsonObject();
                int code = json.get("code").getAsInt();
                Util.log("code = " + code);
                if (code == 200) {
                    Toast.makeText(getApplicationContext(), "출석 완료 .", Toast.LENGTH_SHORT).show();
                } else if (code == 300) {
                    Toast.makeText(getApplicationContext(), "수업이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "이미 출석 되었습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                endProgress();
            }
        });

    }

    public void startProgress(){
        mpd = new MProgressDialog(AttendanceActivity.this);
        mpd.show();
    }

    public void endProgress(){
        if(mpd!=null&&mpd.isShowing()) {
            mpd.dismiss();
        }
    }

}
