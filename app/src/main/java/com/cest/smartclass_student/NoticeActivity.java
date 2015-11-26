package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cest.smartclass_student.Adapter.NoticeAdapter;
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


public class NoticeActivity extends Activity {





    @Bind(R.id.notice_class)
    TextView tv_class;
    @Bind(R.id.notice_rv)
    RecyclerView rv_notices;

    JsonArray array;
    NoticeAdapter na;
    LinearLayoutManager mLayoutManager;

    JsonObject obj;
    int c_classid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);

        Intent i = getIntent();
        obj = new JsonParser().parse(i.getStringExtra("class")).getAsJsonObject();
        c_classid = obj.get("c_classid").getAsInt();
        tv_class.setText(obj.get("name").getAsString());

        array = new JsonArray();
        na = new NoticeAdapter(getApplicationContext(), array);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_notices.setAdapter(na);
        rv_notices.setLayoutManager(mLayoutManager);
        rv_notices.setItemAnimator(new DefaultItemAnimator());

        na.setCustomOnItemClickListener(new CustomOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                i.putExtra("notice", na.getItem(position).toString());
                i.putExtra("class", obj.toString());
                startActivity(i);
            }
        });
        getNotices();

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getNotices(){
        final MProgressDialog mpd = new MProgressDialog(NoticeActivity.this);
        mpd.show();
        APIManager.getInstance().getAPI().getNoticesByClassid(c_classid, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
              //  Util.log(jsonElement.toString());
                JsonArray temp = jsonElement.getAsJsonArray();
                Util.log("before size = "+array.size());
                for (int i = 0,size = array.size(); i < size; i++) {
                    Util.log("index = "+i);
                    array.remove(0);
                }
                Util.log("after size = "+array.size());
                array.addAll(temp);
                na.notifyDataSetChanged();
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
