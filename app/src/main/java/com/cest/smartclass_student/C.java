package com.cest.smartclass_student;

import java.util.ArrayList;

/**
 * Created by pc on 2015-09-26.
 */
public class C {

    public static final String SENDER_ID = "105532635623";
    public static final int USER_TYPE = 2;


    public static final String PREF_KEY_C_USERID = "pref.c_userid";
    public static final String PREF_KEY_USERID = "pref.userid";
    public static final String PREF_KEY_PHONE = "pref.phone";
    public static final String PREF_KEY_NAME = "pref.name";
    public static final String PREF_KEY_TYPE = "pref.type";



    public static int RESULT_EXIST_USER = 1;
    public static int RESULT_NOT_EXIST_USER = 2;
    public static int RESULT_CAMERA_DONE = 3;



    public static final int MENU_ATTENDANCE = 1;
    public static final int MENU_CHECK_ATTENDANCE = 2;
    public static final int MENU_NOTICE = 3;

    public static final ArrayList<MenuItem> menus = new ArrayList<MenuItem>(){
        {
            add(new MenuItem(MENU_ATTENDANCE,R.drawable.check,"출석"));
            add(new MenuItem(MENU_CHECK_ATTENDANCE,R.drawable.search,"출결조회"));
            add(new MenuItem(MENU_NOTICE,R.drawable.bell,"공지사항"));
        }
    };
}
