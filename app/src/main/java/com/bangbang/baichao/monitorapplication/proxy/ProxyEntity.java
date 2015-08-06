package com.bangbang.baichao.monitorapplication.proxy;

import java.io.Serializable;

/**
 * proxy向界面callback的实体对象
 * @author 黄宏宇
 * @date: 2014年8月15日 上午11:14:31
 */
public class ProxyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String mAction="";
	private int mErrorCode;
	private Object mData;

	/**
	 * 默认构造
	 * Creates a new instance of ProxyEntity.
	 */
    public ProxyEntity() { }

    public ProxyEntity(String action) {
        mAction = action;
    }

    public ProxyEntity(String action, Object data) {
        mAction = action;
        mData = data;
    }

    public ProxyEntity(String action, int errorCode) {
        mAction = action;
        mErrorCode = errorCode;
    }

    /**
	 * 带参构造
	 * Creates a new instance of ProxyEntity.
	 * @param action 响应的action
	 * @param errorCode 错误码
	 * @param data response返回的数据
	 */
	public ProxyEntity(String action, int errorCode, Object data) {
		super();
		this.mAction = action;
		this.mErrorCode = errorCode;
		this.mData = data;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return mAction;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.mAction = action;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return mErrorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.mErrorCode = errorCode;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return mData;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.mData = data;
	}

}
