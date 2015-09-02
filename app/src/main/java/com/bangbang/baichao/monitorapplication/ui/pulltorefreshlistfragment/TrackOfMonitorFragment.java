package com.bangbang.baichao.monitorapplication.ui.pulltorefreshlistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.adapter.MyTrackListAdapter;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.TrackVO;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.proxy.ProxyEntity;
import com.bangbang.baichao.monitorapplication.proxy.TrackOfMonitorProxy;
import com.bangbang.baichao.monitorapplication.ui.fragment.BaseFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by baichao on 2015/8/6.
 */
public class TrackOfMonitorFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener {

    private View mLayoutRoot;
    protected final User user = User.getInstance();
    private TrackOfMonitorProxy mProxy;
    private MyTrackListAdapter myTrackListAdapter;
    private PullToRefreshListView mList;
    private ArrayList<TrackVO> mTracklistArray = new ArrayList<TrackVO>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.fragment_trackofmonitor, container, false);
        mList = (PullToRefreshListView) mLayoutRoot.findViewById(R.id.listView);
        mProxy = new TrackOfMonitorProxy(getProxyCallbackHandler(), getActivity());
        myTrackListAdapter = new MyTrackListAdapter(getActivity());
        mList.setAdapter(myTrackListAdapter);
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mList.setOnRefreshListener(this);
        mProxy.refreshDateList();
        return mLayoutRoot;
    }
    @Override
    protected void onResponse(ProxyEntity entity) {
        super.onResponse(entity);
        String action = entity.getAction();
        ArrayList<ManagerVO> resultArray;
        if (action.equals(TrackOfMonitorProxy.GET_TRACKMONITOR_LIST)) {
            mTracklistArray = (ArrayList<TrackVO>) entity.getData();
            myTrackListAdapter.setListData(mTracklistArray);
            Log.d("zhaobo", mTracklistArray.size() + "");
            for(TrackVO  v :  mTracklistArray){
                Log.d("zhaobo","VO="+v);
            }
            mList.onRefreshComplete();
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        mProxy.refreshDateList();
    }
}
