package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.bangbang.baichao.monitorapplication.proxy.BaseProxy;
import com.bangbang.baichao.monitorapplication.proxy.ProxyEntity;

import java.lang.ref.WeakReference;

public class BaseFragment extends Fragment {

    protected BaseProxy mProxy;

    private final Handler mProxyCallbackHandler = new MyHandler(this);

    protected void onResponse(ProxyEntity entity) {

    }

    public Handler getProxyCallbackHandler() {
        return mProxyCallbackHandler;
    }


    private static class MyHandler extends Handler {
        private WeakReference<BaseFragment> mFragment;
        private boolean isDestroyed;
        public MyHandler(BaseFragment fragment) {
            mFragment = new WeakReference<BaseFragment>(fragment);
        }
        public void destroy(){
            isDestroyed = true;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mFragment == null) {
                return;
            }
            BaseFragment fragment = mFragment.get();
//            if (fragment == null) {
//                return;
//            }
//            if(isDestroyed){
//                return;
//            }
//
//            if (msg == null || msg.obj == null) {
//                Log.d("handler", "proxy callback object is null");
//                return;
//            }

            if (msg.obj instanceof ProxyEntity) {
                ProxyEntity entity = (ProxyEntity) msg.obj;
                fragment.onResponse(entity);
            } else {
                Log.d("handler", "proxy callback object is not ProxyEntity");
            }
        }
    }


}
