package com.bangbang.baichao.monitorapplication.proxy;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bangbang.baichao.monitorapplication.entity.User;


/**
 * Proxy����
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
     * ��ʼ��proxy Creates a new instance of BaseProxy.
     *
     * @param proxyCallbackHandler ��������淢��response��handler
     */
    public BaseProxy(Handler proxyCallbackHandler) {
        mHandler = proxyCallbackHandler;
        mTag = this.getClass().getSimpleName();
        user = User.getInstance();
    }

    /**
     * Creates a new instance of BaseProxy.
     *
     * @param proxyCallbackHandler ���洫�ݸ�Proxy��Handler������Proxy��Activity����
     * @param context              context
     */
    public BaseProxy(Handler proxyCallbackHandler, Context context) {
        this(proxyCallbackHandler);
        mContext = context;
    }

    /**
     * �鷽�����˷������ڴ����첽���������ĳЩҵ����Ҫִ�ж���첽����������ǰһ���첽�����Ľ����ȷ����һ��ִ��ʲô
     *
     * @param status ��ǰִ�е�״̬
     */
    protected void onAsynReceived(int status, Object... args) {

    }

    /**
     * ������callback
     */
    protected void callback(ProxyEntity entity) {
        if (mHandler != null) {
            Message msg = Message.obtain(mHandler);
            msg.obj = entity;
            msg.sendToTarget();
        }
    }

    /**
     * ע��proxy��������дʱһ��Ҫ����super.destroy()��ȷ��Activity�˳�ʱ��handler������Ϊ�գ�
     * ����Activity�˳���response��������
     */
    public void destroy() {
        mHandler = null;
        mContext = null;
    }

    /**
     * �������ʽ�����ص�֪ͨ����
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
