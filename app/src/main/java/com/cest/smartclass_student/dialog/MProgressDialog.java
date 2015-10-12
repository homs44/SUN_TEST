package com.cest.smartclass_student.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.cest.smartclass_student.R;


/**
 * Created by pc on 2015-10-01.
 */
public class MProgressDialog extends Dialog {


    public MProgressDialog(Context context) {
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        windowParams.dimAmount =0.3f;
        getWindow().setAttributes(windowParams);
        setContentView(R.layout.dialog_progress);
    }
}
