package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cest.smartclass_student.Adapter.ShowClassAdapter;
import com.cest.smartclass_student.dialog.MProgressDialog;
import com.cest.smartclass_student.http.APIManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ShowClassActivity extends Activity{

    int menu;

    @Bind(R.id.show_class_title)
    TextView tv_title;

    @Bind(R.id.show_class_rv)
    RecyclerView rv_class;

    @Bind(R.id.show_class_back)
    ImageView iv_back;

    @OnClick({R.id.show_class_back})
    void onClick(View view){
        if(view.getId()==R.id.show_class_back){
            finish();
        }
    }

    ShowClassAdapter sca;
    JsonArray array;
    LinearLayoutManager mLayoutManager;

    //callback adapter로 넘기고

    //출석일 경우 출석 가능여부 확인 이후 가능하면 출석 시작

    //결과일경우 결과화면 출력


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);
        ButterKnife.bind(this);
        menu = getIntent().getIntExtra("menu",-1);

        if(menu == C.MENU_ATTENDANCE){
            tv_title.setText("출석");
        }else if(menu == C.MENU_CHECK_ATTENDANCE){
            tv_title.setText("출결확인");
        }else if(menu==C.MENU_NOTICE){
            tv_title.setText("공지사항");
        }else{
            Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        array = new JsonArray();
        sca = new ShowClassAdapter(getApplicationContext(),array);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_class.setAdapter(sca);
        rv_class.setLayoutManager(mLayoutManager);
        rv_class.setItemAnimator(new DefaultItemAnimator());

        sca.setCustomOnItemClickListener(new CustomOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                nextProcess(sca.getItem(position));
            }
        });

        getMyClasses();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sca.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void getMyClassesTest(){
        JsonArray temp = new JsonArray();
        for(int i =0; i<10; i++) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", "네트워크");
            obj.addProperty("c_classid", i);
            obj.addProperty("professor", "조유제");
            obj.addProperty("code", "ELEC241001");
            obj.addProperty("mac_address", "DF:37:11:AC:F6:2D");
            //obj.addProperty("mac_address", "DF:37:11:AC:F6:23");
            JsonParser parser = new JsonParser();
            JsonElement x = parser.parse(obj.toString());
            temp.add(x);
        }
        array.addAll(temp);
        sca.notifyDataSetChanged();

    }

    private void getMyClasses(){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int c_userid = mPref.getInt(C.PREF_KEY_C_USERID, -1);
        final MProgressDialog mpd = new MProgressDialog(ShowClassActivity.this);
        mpd.show();
        APIManager.getInstance().getAPI().getMyClasses(c_userid, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonArray temp = jsonElement.getAsJsonArray();
                for(int i =0,size = array.size(); i<size;i++){
                    array.remove(0);
                }
                array.addAll(temp);
                sca.notifyDataSetChanged();
                mpd.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                mpd.dismiss();
            }
        });
    }

    private void nextProcess(JsonObject obj){
        if(menu == C.MENU_ATTENDANCE){
           // processAttendance(obj);
            Intent i = new Intent(ShowClassActivity.this,AttendanceActivity.class);
            i.putExtra("class",obj.toString());
            startActivity(i);
        }else if(menu == C.MENU_CHECK_ATTENDANCE){
            Intent i = new Intent(ShowClassActivity.this,CheckAttendanceActivity.class);
            i.putExtra("class",obj.toString());
            startActivity(i);
        }else if(menu==C.MENU_NOTICE){
            Intent i = new Intent(ShowClassActivity.this,NoticeActivity.class);
            i.putExtra("class",obj.toString());
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }




}
