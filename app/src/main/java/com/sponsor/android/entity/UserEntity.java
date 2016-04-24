package com.sponsor.android.entity;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class UserEntity implements Serializable{

    private String birthday;
    //1 - male; 2 - famale
    private int gender;
    @JSONField(name = "avatar_url")
    private String avatarUrl;
    @JSONField(name = "user_id")
    private String uid;
    @JSONField(name = "nick_name")
    private String nickName;
    private String mobile;

    public String getBirthday() {
        return (birthday != null) ? birthday : "";
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return (avatarUrl != null) ? avatarUrl : "";
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
