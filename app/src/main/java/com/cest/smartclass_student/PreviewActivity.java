package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PreviewActivity extends Activity {

   String path;
    Bitmap bitmap;
    int cameraid;

    @Bind(R.id.preview_img)
    ImageView iv_img;

    @Bind(R.id.preview_okay)
    Button bt_okay;
    @Bind(R.id.preview_cancel)
    Button bt_cancel;

    @OnClick({R.id.preview_okay,R.id.preview_cancel})
    void onClick(View v){
        if(v.getId()==R.id.preview_okay){
            //성공
            Intent i = new Intent();
            i.putExtra("path", path);
            setResult(RESULT_OK, i);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        path = getIntent().getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);
        iv_img.setImageBitmap(bitmap);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        ButterKnife.unbind(this);
    }



}
