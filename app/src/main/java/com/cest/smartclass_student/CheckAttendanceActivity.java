package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cest.smartclass_student.Adapter.CheckAttendanceAdapter;
import com.cest.smartclass_student.dialog.MProgressDialog;
import com.cest.smartclass_student.http.APIManager;
import com.cest.smartclass_student.support.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CheckAttendanceActivity extends Activity {

    @Bind(R.id.check_attendance_name)
    TextView tv_name;
    @Bind(R.id.check_attendance_rv)
    RecyclerView rv_attendance;

    CheckAttendanceAdapter caa;
    JsonArray array;

    LinearLayoutManager mLayoutManager;
    JsonObject obj;
    int c_classid;
    int c_userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        ButterKnife.bind(this);

        Intent i = getIntent();
        obj = new JsonParser().parse(i.getStringExtra("class")).getAsJsonObject();
        c_classid = obj.get("c_classid").getAsInt();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        c_userid = mPref.getInt(C.PREF_KEY_C_USERID, -1);
        tv_name.setText(obj.get("name").getAsString());
        Util.log("c_classid = " +c_classid + " c_userid = " +c_userid);

        array = new JsonArray();
        caa = new CheckAttendanceAdapter(getApplicationContext(),array);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_attendance.setAdapter(caa);
        rv_attendance.setLayoutManager(mLayoutManager);
        rv_attendance.setItemAnimator(new DefaultItemAnimator());

        getAttendances(c_classid, c_userid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void getAttendances(int c_classid,int c_userid){
        final MProgressDialog mpd = new MProgressDialog(CheckAttendanceActivity.this);
        mpd.show();
        APIManager.getInstance().getAPI().getMyAttendance(c_userid, c_classid, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Util.log(jsonElement.toString());
                JsonArray temp = jsonElement.getAsJsonArray();
                for(int i =0; i<array.size();i++){
                    array.remove(i);
                }
                array.addAll(temp);
                caa.notifyDataSetChanged();
                mpd.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                mpd.dismiss();
            }
        });
    }


}
