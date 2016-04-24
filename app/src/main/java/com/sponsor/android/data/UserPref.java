package com.sponsor.android.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sponsor.android.SponsorApplication;
import com.sponsor.android.entity.UserEntity;
import com.sponsor.android.util.Checker;

public class UserPref {
    private static final String KEY_ID = "id";
    private static final String KEY_ALIAS = "alias";
    private static final String KEY_USER = "user";

    private static  UserPref instance;

    public static  synchronized  UserPref getInstance(){
        if (instance == null){
            instance = new UserPref(SponsorApplication.getContext());
        }
        return  instance;
    }

    /////////////////////////////////////////////////////////
    private static  final String PREF_NAME = "USER";
    private SharedPreferences mPref;

    private UserPref(Context ctx){
        mPref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setInfo(UserEntity entity){
        if (entity != null){
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_USER, JSON.toJSONString(entity));
            editor.commit();
        }
    }

    public boolean hasLogin(){
        UserEntity entity = getUser();
        return  entity != null;
    }

    public UserEntity getUser(){
        String info = mPref.getString(KEY_USER, null);
        if (!Checker.isEmpty(info)){
            try {
                return JSON.parseObject(info, UserEntity.class);
            }
            catch (Exception e){
                e.printStackTrace();
                clear();
            }
        }
        return  null;
    }

    public String getId(){
        return mPref.getString(KEY_ID, "");
    }

    public void resetUser(UserEntity entity){
        if (entity != null){
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_USER, JSON.toJSONString(entity));
            editor.putString(KEY_ID, entity.getUid());
            editor.commit();
        }
    }

    public String getAlias(){
        return mPref.getString(KEY_ALIAS, "");
    }

    public void setAlias(String alias){
        if (!TextUtils.isEmpty(alias)){
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_ALIAS, alias);
            editor.commit();
        }
    }

    public void clear(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();
    }
}
