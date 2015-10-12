package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cest.smartclass_student.http.APIManager;
import com.cest.smartclass_student.support.Util;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends Activity {

    private static int REQUEST_CODE_INTRO = 1;

    @Bind(R.id.login_id)
    EditText et_id;
    @Bind(R.id.login_password)
    EditText et_password;
    @Bind(R.id.login_bt)
    Button bt_login;

    @OnClick(R.id.login_bt)
    void onClick(View view){
        if(view.getId()==R.id.login_bt){
            login();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        startActivityForResult(new Intent(LoginActivity.this, IntroActivity.class), REQUEST_CODE_INTRO);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == REQUEST_CODE_INTRO){
            if(resultCode == C.RESULT_EXIST_USER){
                //로그인
                next();
            }else{
                //test
                //next();
            }
        }

    }

    private void login(){
        String id = et_id.getText().toString();
        String password = et_password.getText().toString();

        if(id==""||password==""){
            Toast.makeText(getApplicationContext(),"아이디/비밀번호를 다시 한번 확인하세요",Toast.LENGTH_SHORT).show();
            return;
        }

        background(id,password);

    }

    private void next(){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    private void background(String id, String password) {
        new AsyncTask<String, String, String>() {


            @Override
            public String doInBackground(String... params) {
                String result =null;
                String id = params[0];
                String password = params[1];
                String gcmid = null;
                try {
                    gcmid = InstanceID.getInstance(getApplicationContext()).getToken(C.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(gcmid !=null) {
                    result ="available";
                    APIManager.getInstance().getAPI().login(id, password, gcmid,C.USER_TYPE, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            Util.log(response.getBody().toString());
                            Util.log(jsonElement.toString());

                            JsonObject json = jsonElement.getAsJsonObject();
                            if (json.get("code").getAsInt()==200) {
                                //사용가능
                                saveUser(json);
                            } else {
                                Toast.makeText(getApplicationContext(), "아이디/비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Toast.makeText(getApplicationContext(), "서버가 불안정합니다\n잠시후 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                            Log.d("cest", "error : " + retrofitError.toString());
                        }
                    });
                }

                return result;
            }

            @Override
            public void onPostExecute(String result) {
                if(result==null){
                    Toast.makeText(getApplicationContext(),"서비스를 이용하실 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }

        }.execute(id, password);

    }

    private void saveUser(JsonObject json) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(C.PREF_KEY_C_USERID, json.get("c_userid").getAsInt());
        editor.putString(C.PREF_KEY_USERID, json.get("userid").getAsString());
        editor.putString(C.PREF_KEY_NAME, json.get("name").getAsString());
        editor.putString(C.PREF_KEY_PHONE, json.get("phone").getAsString());
        editor.putInt(C.PREF_KEY_TYPE, json.get("type").getAsInt());
        editor.commit();

        next();
    }


}
