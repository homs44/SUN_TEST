package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.cest.smartclass_student.camera.CameraPreview;
import com.cest.smartclass_student.camera.MCamera;
import com.cest.smartclass_student.support.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CameraActivity extends Activity {
    Camera mCamera;
    CameraPreview mPreview;
    public static final int REQUEST_CODE_PIC = 1;
    String mCurrentPhotoPath;
    MCamera Cameraids;

    @Bind(R.id.camera_bt)
    Button bt_camera;
    @Bind(R.id.camera_frame)
    FrameLayout preview;

    @OnClick(R.id.camera_bt)
    void onClick(View view) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, mPicture);
        }

    }
    /*
    사진 저장 후 uri preview activity로 넘기고 preview에서 확인 누를시에 해당 uri attendance activity로 전달 후
    attendance_activity에서 출석 보내고 결과 출력
     */

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Util.log("height : " + bitmap.getHeight() + "width : " + bitmap.getWidth());
            bitmap = Util.rotateBitmap(Bitmap.createScaledBitmap(bitmap,1000,800,true), Cameraids);
            String path = saveImage(bitmap);
            Intent i = new Intent(CameraActivity.this, PreviewActivity.class);
            i.putExtra("path", path);
            startActivityForResult(i, REQUEST_CODE_PIC);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        Cameraids = new MCamera();
        Cameraids.front = -1;
        Cameraids.back = -1;
        findCameraids(Cameraids);
        mPreview = new CameraPreview(this);
        preview.addView(mPreview);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.log("CameraActivity - onResume");
        onResumeProcess();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.log("CameraActivity - onPause");
        onPauseProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    private void onResumeProcess() {
        if (mCamera == null) {
            if (checkCameraHardware(getApplicationContext())) {
                mCamera = getCameraInstance();
            }
        }
        mPreview.setCamera(mCamera);

    }


    private void onPauseProcess() {
        try {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mPreview.freeCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            if(Cameraids.front!=-1){
                c = Camera.open(Cameraids.front);
                Cameraids.current = Cameraids.front;
            }else if(Cameraids.back!=-1){
                c = Camera.open(Cameraids.back);
                Cameraids.current = Cameraids.back;
            }else{
                c = Camera.open();
            }

            if(c==null){
                Util.log("Camera =null");
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void findCameraids(MCamera Cameraids){
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i =0; i<numberOfCameras;i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                Cameraids.front = i;
            }else if(info.facing ==Camera.CameraInfo.CAMERA_FACING_BACK){
                Cameraids.back = i;
            }

        }
    }


    @Override
    public void onActivityResult(int request_code, int result_code, Intent data) {
        if (request_code == REQUEST_CODE_PIC) {
            if (result_code == RESULT_OK) {
                Intent i = new Intent();
                i.putExtra("path",data.getStringExtra("path"));
                setResult(RESULT_OK, i);
                finish();

            }
        }
    }



    private String saveImage(Bitmap bitmap) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int c_userid = mPref.getInt(C.PREF_KEY_C_USERID, -1);
        String FileName = c_userid + "_" + System.currentTimeMillis() + ".jpg";
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/smartclass/";
        String FilePath = storageDir + FileName;
        File file = new File(storageDir);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(FilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FilePath;

    }

}
