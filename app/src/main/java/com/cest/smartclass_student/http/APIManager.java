package com.cest.smartclass_student.http;

import retrofit.RestAdapter;

/**
 * Created by pc on 2015-07-31.
 */
public class APIManager {

    private  static APIManager instance;
    private RestAdapter restAdapter;
    private APIService api;
    private static final String SERVER_URL = "http://192.168.123.166/index.php";

    public synchronized static APIManager getInstance(){
        if(instance ==null) {
            instance = new APIManager();
        }
        return instance;
    }

    private APIManager(){
        restAdapter = new RestAdapter.Builder().setEndpoint(SERVER_URL).build();
        api = restAdapter.create(APIService.class);
    }

    public APIService getAPI(){
        if(restAdapter==null){
            restAdapter = new RestAdapter.Builder().setEndpoint(SERVER_URL).build();
        }
        if(api ==null){
            api = restAdapter.create(APIService.class);
        }
        return api;
    }
}
