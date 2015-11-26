package com.cest.smartclass_student;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;


public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler(){
            @Override
            public void handleMessage(Message msg){

                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int c_userid = mPref.getInt(C.PREF_KEY_C_USERID,-1);
                if(c_userid==-1){
                    setResult(C.RESULT_NOT_EXIST_USER);
                }else{
                    setResult(C.RESULT_EXIST_USER);
                }

                finish();
            }
        }.sendEmptyMessageDelayed(0, 1000);

    }



}
