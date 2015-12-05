package com.cest.smartclass_student.http;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by pc on 2015-07-31.
 */
public interface APIService {

    @FormUrlEncoded
    @POST("/auth/login")
    void login(@Field("userid") String user_id, @Field("password") String password, @Field("gcmid") String gcmid,@Field("type") int type, Callback<JsonElement> callback);
    //asdjfklasdjfklsaf

    @GET("/API/class_/select_all_sugang_class")
    void getMyClasses(@Query("c_userid") int c_userid, Callback<JsonElement> callback);


    @GET("/API/attendance/is_enable_attendance_device")
    void isEnableAttendance(@Query("c_classid") int c_classid,@Query("device") String device, Callback<JsonElement> callback);

    @GET("/API/attendance_reply/select_all_by_userid")
    void getMyAttendance(@Query("c_userid") int c_userid,@Query("c_classid") int c_classid, Callback<JsonElement> callback);

    @Multipart
    @POST("/API/attendance/reply_attendance")
    void sendAttendance(@Part("userfile")TypedFile file,@Part("filename") String filename, @Part("c_classid") int c_classid, @Part("c_userid") int c_userid,@Part("device") String device,Callback<JsonElement> callback);

    @GET("/API/notice/select_by_noticeid")
    void getNotice(@Query("c_noticeid") int c_noticeid, Callback<JsonElement> callback);

    @GET("/API/notice/select_all_by_classid")
    void getNoticesByClassid(@Query("for_c_classid") int for_c_classid, Callback<JsonElement> callback);


}
