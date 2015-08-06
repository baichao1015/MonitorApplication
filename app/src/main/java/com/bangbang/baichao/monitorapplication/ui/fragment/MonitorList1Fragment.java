package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.MonitorVO;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.utils.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MonitorList1Fragment extends Fragment {

    private static final String TAG = "MonitorListFragment";
    private View mLayoutRoot;
    protected final User user = User.getInstance();
    private int TOKEN;
    private int POWER;
    private PullToRefreshListView mMonitorList;
    private int PAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayoutRoot = inflater.inflate(R.layout.fragment_monitor_list1, container, false);
        mMonitorList = (PullToRefreshListView) mLayoutRoot.findViewById(R.id.monitorlistView);
        mMonitorList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        getMonitorList();
        mMonitorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return mLayoutRoot;
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            AsyncHttpClient httpclient = new AsyncHttpClient();
            final ArrayList<MonitorVO> list = new ArrayList<>();
            final ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
            TOKEN = user.getTOKEN();
            String url = "http://192.168.119.39:8080/domain/pc?opt=34&name=" + "&page=" + PAGE + "&token=" + TOKEN;
            httpclient.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data;
                        JSONArray monitorarray = new JSONArray(jsonObject.getString("data"));
                        for (int i = 0; i < monitorarray.length(); i++) {
                            data = monitorarray.getJSONObject(i);
                            int id = Integer.parseInt(data.getString("id"));
                            String serve1 = data.getString("serve");
                            String path1 = data.getString("path");
                            String time1 = data.getString("time");
                            String interval1 = data.getString("interval");
                            String threshold1 = data.getString("threshold");
                            String name1 = data.getString("name");
                            list.add(new MonitorVO(id, serve1, path1, time1, interval1, threshold1, name1));
                        }
                        for (MonitorVO monitor : list) {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("serve", monitor.getServe());
                            item.put("path", monitor.getPath());
                            item.put("time", monitor.getTime());
                            item.put("interval", monitor.getInterval());
                            item.put("threshold", monitor.getThreshold());
                            item.put("manager", monitor.getName());
                            list1.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }
            });

            return list1;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, R.layout.monitor_item,
                    new String[]{"serve", "path", "time", "interval", "threshold", "manager"}, new int[]{R.id.serve, R.id.path, R.id.time, R.id.interval, R.id.threshold, R.id.manager});
            mMonitorList.setAdapter(adapter);
            // Call onRefreshComplete when the list has been refreshed.
            mMonitorList.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    public void getMonitorList() {
        AsyncHttpClient httpclient = new AsyncHttpClient();
        TOKEN = user.getTOKEN();
        String url = "http://192.168.119.39:8080/domain/pc?opt=34&name=" + "&page=" + PAGE + "&token=" + TOKEN;
        httpclient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<MonitorVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data;
                    JSONArray monitorarray = new JSONArray(jsonObject.getString("data"));
                    for (int i = 0; i < monitorarray.length(); i++) {
                        data = monitorarray.getJSONObject(i);
                        int id = Integer.parseInt(data.getString("id"));
                        String serve1 = data.getString("serve");
                        String path1 = data.getString("path");
                        String time1 = data.getString("time");
                        String interval1 = data.getString("interval");
                        String threshold1 = data.getString("threshold");
                        String name1 = data.getString("name");
                        list.add(new MonitorVO(id, serve1, path1, time1, interval1, threshold1, name1));
                    }
                    ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
                    for (MonitorVO monitor : list) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("serve", monitor.getServe());
                        item.put("path", monitor.getPath());
                        item.put("time", monitor.getTime());
                        item.put("interval", monitor.getInterval());
                        item.put("threshold", monitor.getThreshold());
                        item.put("manager", monitor.getName());
                        list1.add(item);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), list1, R.layout.monitor_item,
                            new String[]{"serve", "path", "time", "interval", "threshold", "manager"}, new int[]{R.id.serve, R.id.path, R.id.time, R.id.interval, R.id.threshold, R.id.manager});
                    mMonitorList.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
