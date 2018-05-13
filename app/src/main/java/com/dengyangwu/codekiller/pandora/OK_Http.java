package com.dengyangwu.codekiller.pandora;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CodeKiller on 2017/5/29.
 */
public class OK_Http {
    public ArrayList<Fleamarket> okHttp_flea_load(final ArrayList<Fleamarket> myFleamarket){
        String url="http://192.168.191.1:8081/flea_down_context.php";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("test","");
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("dyw123",res);
                try {

                    JSONArray ja = new JSONArray(res);
                    for(int i=0;i<ja.length();i++){
                        Fleamarket mf = new Fleamarket();
                        JSONObject jo = ja.getJSONObject(i);
                        mf.type=jo.getString("type");
                        mf.demand=jo.getString("demand");
                        mf.id=jo.getInt("id");
                        mf.state=jo.getInt("state");
                        mf.userid=jo.getString("userid");
                        mf.price=jo.getString("price");
                     //   mj.person_img=decode(jo.getString("person_img"));
                       myFleamarket.add(i,mf);
                    }
                   Log.e("context:",myFleamarket.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return myFleamarket;
    }

    public ArrayList<Fleamarket> okHttp_flea_img_load(final ArrayList<Fleamarket> myFleamarket){
        Log.e("test2",myFleamarket.size()+"");
        String url="http://192.168.191.1:8081/flea_down_pic.php";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("test","");
        for(int i=0;i<myFleamarket.size();i++){
            //builder.add("uid"+i,Login_Menu.user_id);
            builder.add("id"+i,myFleamarket.get(i).id+"");
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("图片",res);
                try {
                    JSONArray ja_all = new JSONArray(res);
                    for(int i=0;i<ja_all.length();i++){
                        //MyjournalContext mj = new MyjournalContext();
                        JSONArray ja = ja_all.getJSONArray(i);
                        if(ja.length()==0){
                            myFleamarket.get(i).photo.add("");
                            Log.e("weikong","");
                            //myjournalContextList.add(i,myjournalContextList.get(i).journal_img);
                        }else {
                            for(int j=0;j<ja.length();j++){
                                JSONObject jo = ja.getJSONObject(j);
                                myFleamarket.get(i).photo.add(j,decode(jo.getString("pic_url")));
                            }
                            //myjournalContextList.add(i,mj);
                            Log.e("list"+i+"",myFleamarket.get(i).photo.toString());
                        }
                    }
                    Log.e("图片",myFleamarket.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
        return myFleamarket;
    }
    public String decode(String s){
        s=s.replace("\\","");
        s=s.trim();
        return s;
    }
}
