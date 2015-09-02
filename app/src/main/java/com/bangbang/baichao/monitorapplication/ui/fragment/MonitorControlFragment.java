package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.adapter.MyManagerListAdapter;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.proxy.ManagerListProxy;
import com.bangbang.baichao.monitorapplication.proxy.ProxyEntity;
import com.bangbang.baichao.monitorapplication.ui.activity.LoginActivity;
import com.bangbang.baichao.monitorapplication.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonitorControlFragment extends Fragment {

    private View mLayoutRoot;
    private EditText mServe;
    private EditText mPath;
    private EditText mInterval;
    private EditText mTime;
    private EditText mThreshold;
    private EditText mManager;
    private Button mAddMonitor;
    private Spinner spinner;
    private ArrayAdapter adapter;
    protected final User user = User.getInstance();
    private String TOKEN;
    private int POWER;
    private int mManagerPage;
    private String TAG = "MonitorControlFragment";
    private ArrayList<String> mManagerlistArray = new ArrayList<String>();


    public static final String CHECK_MANAGERLIST = "http://192.168.119.39:8080/domain/pc?opt=24&name=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayoutRoot = inflater.inflate(R.layout.fragment_monitor_control, container, false);
        mServe = (EditText) mLayoutRoot.findViewById(R.id.serve);
        mPath = (EditText) mLayoutRoot.findViewById(R.id.path);
        mInterval = (EditText) mLayoutRoot.findViewById(R.id.timechannel);
        mTime = (EditText) mLayoutRoot.findViewById(R.id.time);
        mThreshold = (EditText) mLayoutRoot.findViewById(R.id.threshold);
        mManager = (EditText) mLayoutRoot.findViewById(R.id.manager_select);
        spinner = (Spinner) mLayoutRoot.findViewById(R.id.manager_select1);
        mManagerlistArray = getManagerList();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mManagerlistArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mManager.setText(mManagerlistArray.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        mAddMonitor = (Button) mLayoutRoot.findViewById(R.id.addMonitor);

        mAddMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vServe = mServe.getText().toString();
                String vPath = mPath.getText().toString();
                String vInterval = mInterval.getText().toString();
                String vTime = mTime.getText().toString();
                String vThreshold = mThreshold.getText().toString();
                String vManager = mManager.getText().toString();
                POWER = user.getPOWER();
                TOKEN = user.getTOKEN();
                if (POWER == 1) {
                    String mUrl = "http://192.168.119.39:8080/domain/pc?opt=21&serve="
                            + vServe + "&path=" + vPath + "&interval=" + vInterval + "&threshold=" + vThreshold + "&time=" + vTime + "&name=" + vManager + "&token=" + TOKEN;
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.get(mUrl, new AsyncHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers,
                                              byte[] responseBody) {
                            try {
                                String response = new String(responseBody);
                                JSONObject jsonObject = new JSONObject(response);
                                TOKEN = jsonObject.getString("token");
                                user.setTOKEN(TOKEN);
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    Log.d(TAG, "添加监控成功");
                                    Toast.makeText(getActivity().getApplicationContext(), "添加监控成功", Toast.LENGTH_SHORT).show();
                                } else if (code == 0) {
                                    Log.d(TAG, "添加接口人失败" + "code=" + code);
                                    Toast.makeText(getActivity().getApplicationContext(), "添加接口人失败", Toast.LENGTH_SHORT).show();
                                } else if (code == 2) {
                                    Toast.makeText(getActivity().getApplicationContext(), "账号异常", Toast.LENGTH_SHORT).show();
                                    try {
                                        HttpUtils.UserExit();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    user.init();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            // 输出错误信息
                            Log.d(TAG, "访问服务器失败");
                            Toast.makeText(getActivity().getApplicationContext(), "访问服务器失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "用户没有权限进行此操作", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mLayoutRoot;
    }

    public ArrayList<String> getManagerList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ManagerNameList",
                Activity.MODE_PRIVATE);
        Set<String> namelist = new HashSet<String>();
        ArrayList list = new ArrayList();
        namelist = sharedPreferences.getStringSet("ManagerName", namelist);
        for (Iterator iter = namelist.iterator(); iter.hasNext(); ) {
            String str = (String) iter.next();
            list.add(str);
        }
        return list;

//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        final ArrayList<String> list = new ArrayList<>();
//        TOKEN = user.getTOKEN();
//        String URL = CHECK_MANAGERLIST + "&page=" + "1" + "&token=" + TOKEN;
//        httpClient.get(URL, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String response = new String(responseBody);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject data;
//                    TOKEN = jsonObject.getString("token");
//                    user.setTOKEN(TOKEN);
//                    JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
//                    for (int i = 0; i < managerArray.length(); i++) {
//                        data = managerArray.getJSONObject(i);
//                        int id1 = data.getInt("id");
//                        String name1 = data.getString("name");
//                        list.add(name1);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("managerlistproxy", "连接服务器失败");
//            }
//        });
//        return list;
    }

}
