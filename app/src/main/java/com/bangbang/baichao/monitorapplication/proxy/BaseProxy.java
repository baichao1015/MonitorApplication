package com.bangbang.baichao.monitorapplication.proxy;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bangbang.baichao.monitorapplication.entity.User;



public class BaseProxy {
    
    protected Handler mHandler;

    protected Context mContext;

    protected final String mTag;

    protected final User user;

    /**
     * @return the Tag
     */
    protected String getTag() {
        return mTag;
    }

    public BaseProxy(Handler proxyCallbackHandler) {
        mHandler = proxyCallbackHandler;
        mTag = this.getClass().getSimpleName();
        user = User.getInstance();
    }


    public BaseProxy(Handler proxyCallbackHandler, Context context) {
        this(proxyCallbackHandler);
        mContext = context;
    }

    protected void onAsynReceived(int status, Object... args) {

    }


    protected void callback(ProxyEntity entity) {
        if (mHandler != null) {
            Message msg = Message.obtain(mHandler);
            msg.obj = entity;
            msg.sendToTarget();
        }
    }

    public void destroy() {
        mHandler = null;
        mContext = null;
    }

    protected void setResultDataAndCallback(String action, int resultCode, Object data) {
        ProxyEntity entity = new ProxyEntity();
        entity.setAction(action);
        entity.setData(data);
        entity.setErrorCode(resultCode);
        callback(entity);
    }

}
