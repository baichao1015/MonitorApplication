package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.MonitorVO;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.ui.activity.EditMonitorActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MonitorListFragment extends Fragment {

    private static final String TAG = "MonitorListFragment";
    private View mLayoutRoot;
    protected final User user = User.getInstance();
    private int TOKEN;
    private int POWER;
    private ListView mMonitorList;
    private int PAGE = 1;
    private ArrayList mMonitorIDs;
    private int monitorid;
    public static final String CHECK_MONITOR= "http://192.168.119.39:8080/domain/pc?opt=34&name=";
    public static final String MODIFY_MONITOR = "http://192.168.119.39:8080/domain/pc?opt=33";
    public static final String DELETE_MONITOR = "http://192.168.119.39:8080/domain/pc?opt=32";
    public static final int DELETE_MONITOR_SUCCESS = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayoutRoot = inflater.inflate(R.layout.fragment_monitor_list, container, false);
        mMonitorList = (ListView) mLayoutRoot.findViewById(R.id.monitorlistView);
        AsyncHttpClient httpclient = new AsyncHttpClient();
        TOKEN = user.getTOKEN();
        String url = "http://192.168.119.39:8080/domain/pc?opt=34&name=" + "&page=" + PAGE + "&token=" +  TOKEN;
        httpclient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<MonitorVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data;
                    TOKEN = jsonObject.getInt("token");
                    user.setTOKEN(TOKEN);
                    JSONArray monitorarray = new JSONArray(jsonObject.getString("data"));
                    for(int i=0;i<monitorarray.length();i++){
                        data=monitorarray.getJSONObject(i);
                        int id = Integer.parseInt(data.getString("id"));
                        String serve1=data.getString("serve");
                        String path1=data.getString("path");
                        String time1=data.getString("time");
                        String interval1=data.getString("interval");
                        String threshold1=data.getString("threshold");
                        String name1=data.getString("name");
                        list.add(new MonitorVO(id,serve1,path1,time1,interval1,threshold1,name1));
                    }
                    if(mMonitorIDs != null){
                        mMonitorIDs.clear();
                    }else{
                        mMonitorIDs = new ArrayList();
                    }
                    ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String,Object>>();
                    for(MonitorVO monitor : list){
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("serve", monitor.getServe());
                        item.put("path", monitor.getPath());
                        item.put("time", monitor.getTime());
                        item.put("interval", monitor.getInterval());
                        item.put("threshold", monitor.getThreshold());
                        item.put("manager", monitor.getName());
                        mMonitorIDs.add(monitor.getId());
                        list1.add(item);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), list1, R.layout.monitor_item,
                            new String[]{"serve", "path", "time" , "interval", "threshold", "manager"}, new int[]{R.id.serve, R.id.path, R.id.time,R.id.interval, R.id.threshold, R.id.manager});
                    mMonitorList.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity().getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });

        mMonitorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monitorid = (int) mMonitorIDs.get(position);
                final CharSequence[] items = {"编辑", "删除"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择操作");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getActivity().getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        switch (item) {
                            case 0:
                                Intent intent = new Intent(getActivity(), EditMonitorActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", monitorid);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case 1:
                                deleteMonitor(monitorid);
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
    public void deleteMonitor(int id) {
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        AsyncHttpClient httpclient = new AsyncHttpClient();
        String url = DELETE_MONITOR + "&id=" + id + "&uid=" + TOKEN;
        httpclient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<ManagerVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int resultcode = jsonObject.getInt("code");
                    TOKEN = jsonObject.getInt("token");
                    user.setTOKEN(TOKEN);
                    if(resultcode == DELETE_MONITOR_SUCCESS){
                        Toast.makeText(getActivity().getApplicationContext(), "成功删除监控", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity().getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
