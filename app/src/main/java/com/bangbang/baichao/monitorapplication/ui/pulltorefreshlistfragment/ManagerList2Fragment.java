package com.bangbang.baichao.monitorapplication.ui.pulltorefreshlistfragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.adapter.MyManagerListAdapter;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.proxy.ManagerListProxy;
import com.bangbang.baichao.monitorapplication.proxy.ProxyEntity;

import com.bangbang.baichao.monitorapplication.ui.activity.EditManagerActivity;
import com.bangbang.baichao.monitorapplication.ui.activity.LoginActivity;
import com.bangbang.baichao.monitorapplication.ui.fragment.BaseFragment;
import com.bangbang.baichao.monitorapplication.utils.HttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerList2Fragment extends BaseFragment implements OnRefreshListener {

    private View mLayoutRoot;
    private PullToRefreshListView mList;
    private String TOKEN;
    private int POWER;
    private ManagerListProxy mProxy;
    private MyManagerListAdapter myManagerListAdapter;
    protected final User user = User.getInstance();
    private ArrayList<ManagerVO> mManagerlistArray = new ArrayList<ManagerVO>();
    private int managerid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.managerlist, container, false);
        mList = (PullToRefreshListView) mLayoutRoot.findViewById(R.id.listView);
        mProxy = new ManagerListProxy(getProxyCallbackHandler(), getActivity());
        myManagerListAdapter = new MyManagerListAdapter(getActivity());
        mList.setAdapter(myManagerListAdapter);
        mList.setMode(Mode.PULL_FROM_START);
        mList.setOnRefreshListener(this);
        mProxy.refreshDateList();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POWER = user.getPOWER();
                if (POWER != 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "没有权限进行此操作", Toast.LENGTH_SHORT);
                    return;
                }
                managerid = (int) mManagerlistArray.get(position - 1).getID();
                final CharSequence[] items = {"编辑", "删除"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择操作");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getActivity().getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        switch (item) {
                            case 0:
                                Intent intent = new Intent(getActivity(), EditManagerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", managerid);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case 1:
                                mProxy.deleteManager(managerid);
                                break;
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        return mLayoutRoot;
    }

    @Override
    protected void onResponse(ProxyEntity entity) {
        super.onResponse(entity);
        String action = entity.getAction();
        ArrayList<ManagerVO> resultArray;
        if (action.equals(ManagerListProxy.GET_MANAGERLIST)) {
            mManagerlistArray = (ArrayList<ManagerVO>) entity.getData();
            Set<String> namelist = new HashSet<>();
            myManagerListAdapter.setListData(mManagerlistArray);
            SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("ManagerNameList",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            Log.d("zhaobo", mManagerlistArray.size() + "");
            for(ManagerVO v:mManagerlistArray){
                Log.d("zhaobo", "VO=" + v);
                namelist.add(v.getmMangerName());
            }
            editor.putStringSet("ManagerName", namelist);
            editor.commit();
            mList.onRefreshComplete();
        } else if (action.equals(ManagerListProxy.DELETE_MANAGER_SUCCESS)) {
            mProxy.refreshDateList();
        } else if (action.equals(ManagerListProxy.DELETE_MANAGER_FAIL)) {
            mProxy.refreshDateList();
        } else if (action.equals(ManagerListProxy.FAIL_USER_CONFLICT)) {
            HttpUtils.UserExit();
            user.init();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        mProxy.refreshDateList();
    }
}
