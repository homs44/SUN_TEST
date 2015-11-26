package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NoticeDetailActivity extends Activity {

    @Bind(R.id.notice_detail_back)
    ImageView iv_back;
    @Bind(R.id.notice_detail_class)
    TextView tv_class;
    @Bind(R.id.notice_detail_title)
    EditText et_title;
    @Bind(R.id.notice_detail_content)
    EditText et_content;
    @Bind(R.id.notice_detail_confirm)
    Button bt_confirm;
    @Bind(R.id.notice_detail_date)
    TextView tv_date;

    @OnClick({R.id.notice_detail_back,R.id.notice_detail_confirm})
    void onClick(View v)
    {
        finish();
    }
    JsonObject notice;
    JsonObject obj;
    int c_classid;
    int c_noticeid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        Intent i = getIntent();
        obj = new JsonParser().parse(i.getStringExtra("class")).getAsJsonObject();
        notice = new JsonParser().parse(i.getStringExtra("notice")).getAsJsonObject();
        c_noticeid = notice.get("c_noticeid").getAsInt();
        c_classid = obj.get("c_classid").getAsInt();
        tv_class.setText(obj.get("name").getAsString());
        tv_date.setText(notice.get("created").getAsString());
        et_title.setText(notice.get("title").getAsString());
        et_content.setText(notice.get("content").getAsString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
