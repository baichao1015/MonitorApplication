package com.bangbang.baichao.monitorapplication.proxy;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bangbang.baichao.monitorapplication.entity.User;


/**
 * Proxy基类
 *
 * @author huanghongyu
 */
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

    /**
     * 初始化proxy Creates a new instance of BaseProxy.
     *
     * @param proxyCallbackHandler 用于向界面发送response的handler
     */
    public BaseProxy(Handler proxyCallbackHandler) {
        mHandler = proxyCallbackHandler;
        mTag = this.getClass().getSimpleName();
        user = User.getInstance();
    }

    /**
     * Creates a new instance of BaseProxy.
     *
     * @param proxyCallbackHandler 界面传递个Proxy的Handler，用于Proxy和Activity交互
     * @param context              context
     */
    public BaseProxy(Handler proxyCallbackHandler, Context context) {
        this(proxyCallbackHandler);
        mContext = context;
    }

    /**
     * 虚方法，此方法用于处理异步结果，例如某些业务需要执行多个异步操作，根据前一个异步操作的结果来确定下一步执行什么
     *
     * @param status 当前执行的状态
     */
    protected void onAsynReceived(int status, Object... args) {

    }

    /**
     * 给界面callback
     */
    protected void callback(ProxyEntity entity) {
        if (mHandler != null) {
            Message msg = Message.obtain(mHandler);
            msg.obj = entity;
            msg.sendToTarget();
        }
    }

    /**
     * 注销proxy，子类重写时一定要调用super.destroy()，确保Activity退出时将handler引用置为空，
     * 避免Activity退出后response继续上抛
     */
    public void destroy() {
        mHandler = null;
        mContext = null;
    }

    /**
     * 设置请问结果并回调通知界面
     *
     * @author zhao yanhui
     */
    protected void setResultDataAndCallback(String action, int resultCode, Object data) {
        ProxyEntity entity = new ProxyEntity();
        entity.setAction(action);
        entity.setData(data);
        entity.setErrorCode(resultCode);
        callback(entity);
    }



}
