package com.bangbang.baichao.monitorapplication.entity;

/**
 * 用户信息
 * Created by 58 on 2015/7/27.
 */
public class User {

    private static User instance;
    private String mUserName;
    private String mPwd;
    private String TOKEN;
    private int POWER;
    // 是否需要自动登录，启动客户端首次登录后该值设置为true，掉线后自动重连登录
    private boolean mIsAotuLogin = false;
    private User(){}
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public void init(){
        instance = new User();
    }

    public int getPOWER() {
        return POWER;
    }

    public void setPOWER(int POWER) {
        this.POWER = POWER;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPwd() {
        return mPwd;
    }

    public void setmPwd(String mPwd) {
        this.mPwd = mPwd;
    }

    public boolean ismIsAotuLogin() {
        return mIsAotuLogin;
    }

    public void setmIsAotuLogin(boolean mIsAotuLogin) {
        this.mIsAotuLogin = mIsAotuLogin;
    }
}
