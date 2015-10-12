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


    @GET("/API/class_/select_all_sugang_class")
    void getMyClasses(@Query("c_userid") int c_userid, Callback<JsonElement> callback);


    @GET("/API/attendance/is_enable_attendance_device")
    void isEnableAttendance(@Query("c_classid") int c_classid,@Query("device") String device, Callback<JsonElement> callback);

    @GET("/API/attendance_reply/select_all_by_userid")
    void getMyAttendance(@Query("c_userid") int c_userid,@Query("c_classid") int c_classid, Callback<JsonElement> callback);

    @Multipart
    @POST("/API/attendance/reply_attendance")
    void sendAttendance(@Part("userfile")TypedFile file,@Part("filename") String filename, @Part("c_classid") int c_classid, @Part("c_userid") int c_userid,@Part("device") String device,Callback<JsonElement> callback);

/*
    @Multipart
    @POST("/API/attendance/test")
    void test(@Part("filename") String filename, @Part("c_classid") int c_classid, @Part("c_userid") int c_userid,@Part("device") String device,Callback<JsonElement> callback);
*/

    /*
   @FormUrlEncoded
   @POST("/login")
   void login(@Field("user_id") String user_id, @Field("password") String password, @Field("gcm_id") String gcm_id, @Field("type") int type, Callback<JsonElement> callback);

   @GET("/class/student/{c_user_id}/{semester}")
   void getMyClass(@Path("c_user_id") int c_user_id, @Path("semester") String semester, Callback<JsonElement> callback);


   @FormUrlEncoded
   @POST("/attendance/reply")
   void uesToken(@Field("uuid") String uuid, @Field("c_user_id")
   int c_user_id, @Field("attendance_time") String attendance_time, @Field("valid_time")
   String valid_time, @Field("receive_time") String receive_time, @Field("reply_time") String reply_time, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/attendance/reply")
    void replyAttendance(@Field("uuid") String uuid, @Field("c_user_id")
    int c_user_id, @Field("reply_time") String reply_time, Callback<JsonElement> callback);


    @Multipart
    @POST("/attendance/replay1")
    void useToken1(@Part("image") TypedFile file, @Part("uuid") String uuid, @Part("c_user_id")
    int c_user_id, @Part("attendance_time") String attendance_time, @Part("valid_time")
                   String valid_time, @Part("receive_time") String receive_time, @Part("reply_time") String reply_time, Callback<JsonElement> callback);


    @GET("/attendance/student/result/{c_class_id}/{c_user_id}")
    void getMyAttendanceResult(@Path("c_class_id") int c_class_id, @Path("c_user_id") int c_user_id, Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST("/attendance/available")
    void getActiveAttendance(@Field("c_class_id")
                             int c_class_id, @Field("c_user_id")
                             int c_user_id, Callback<JsonElement> callback);

*/
}
