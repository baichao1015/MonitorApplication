package com.bangbang.baichao.monitorapplication.entity;

/**
 * Created by 58 on 2015/7/22.
 */
public class ManagerVO {
    private int ID;
    private String mMangerName;
    private String mManagerTel;
    private String mManagerMail;

    public ManagerVO(int ID, String mMangerName, String mManagerTel, String mManagerMail) {
        this.mMangerName = mMangerName;
        this.mManagerMail = mManagerMail;
        this.mManagerTel = mManagerTel;
        this.ID = ID;

    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getmMangerName() {
        return mMangerName;
    }

    public void setmMangerName(String mMangerName) {
        this.mMangerName = mMangerName;
    }

    public String getmManagerTel() {
        return mManagerTel;
    }

    public void setmManagerTel(String mManagerTel) {
        this.mManagerTel = mManagerTel;
    }

    public String getmManagerMail() {
        return mManagerMail;
    }

    public void setmManagerMail(String mManagerMail) {
        this.mManagerMail = mManagerMail;
    }
}
