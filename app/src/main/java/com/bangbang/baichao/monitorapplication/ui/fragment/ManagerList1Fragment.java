package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.ui.activity.EditManagerActivity;
import com.bangbang.baichao.monitorapplication.utils.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 58 on 2015/7/23.
 */
public class ManagerList1Fragment extends Fragment {

    private View mLayoutRoot;
    private int TOKEN;
    private int POWER;
    private int PAGE = 1;
    protected final User user = User.getInstance();
    private PullToRefreshListView mManagerList;
    private ArrayList ids;
    private int managerid;
    private ArrayAdapter<ManagerVO> adapter;
    public static final String DELETE_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=22";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.managerlist, container, false);
        mManagerList = (PullToRefreshListView) mLayoutRoot.findViewById(R.id.listView);
        ids = getData();
        adapter = new ManagerListAdapter(getActivity(),R.layout.manager_list_table,ids);
        mManagerList.setAdapter(adapter);

//        AsyncHttpClient httpclient = new AsyncHttpClient();
//        String url = "http://192.168.119.39:8080/domain/pc?opt=24&name=" + "&page=" + PAGE + "&token=" + TOKEN;
//        httpclient.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String response = new String(responseBody);
//                ArrayList<ManagerVO> list = new ArrayList<>();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject data;
//                    JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
//                    for (int i = 0; i < managerArray.length(); i++) {
//                        data = managerArray.getJSONObject(i);
//                        int id1 = data.getInt("id");
//                        String name1 = data.getString("name");
//                        String tel1 = data.getString("tel");
//                        String mail1 = data.getString("mail");
//                        list.add(new ManagerVO(id1, name1, tel1, mail1));
//                    }
//                    ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
//                    if (ids != null) {
//                        ids.clear();
//                    } else {
//                        ids = new ArrayList();
//                    }
//                    for (ManagerVO manager : list) {
//                        HashMap<String, Object> item = new HashMap<String, Object>();
//                        item.put("name", manager.getmMangerName());
//                        item.put("phone", manager.getmManagerTel());
//                        item.put("mail", manager.getmManagerMail());
//                        ids.add(manager.getID());
//                        list1.add(item);
//                    }
//                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), list1, R.layout.manager_list_table,
//                            new String[]{"name", "phone", "mail"}, new int[]{R.id.name, R.id.phone, R.id.mail});
//                    mManagerList.setAdapter(adapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Toast.makeText(getActivity().getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
//            }
//        });
        mManagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                managerid = (int) ids.get(position);
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
                                deleteManager(managerid);
                                break;
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        mManagerList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        return mLayoutRoot;
    }

    public void deleteManager(int id) {
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        AsyncHttpClient httpclient = new AsyncHttpClient();
        String url = DELETE_MANAGER + "&id=" + id + "&token=" + TOKEN;
        httpclient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<ManagerVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data;
                    TOKEN = jsonObject.getInt("token");
                    user.setTOKEN(TOKEN);
                    JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
                    for (int i = 0; i < managerArray.length(); i++) {
                        data = managerArray.getJSONObject(i);
                        int id1 = data.getInt("id");
                        String name1 = data.getString("name");
                        String tel1 = data.getString("tel");
                        String mail1 = data.getString("mail");
                        list.add(new ManagerVO(id1, name1, tel1, mail1));
                    }
                    ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
                    ids.clear();
                    for (ManagerVO manager : list) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("name", manager.getmMangerName());
                        item.put("phone", manager.getmManagerTel());
                        item.put("mail", manager.getmManagerMail());
                        ids.add(manager.getID());
                        list1.add(item);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), list1, R.layout.manager_list_table,
                            new String[]{"name", "phone", "mail"}, new int[]{R.id.name, R.id.phone, R.id.mail});
                    mManagerList.setAdapter(adapter);
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

    private ArrayList<ManagerVO> getData() {
        //创建默认的httpClient实例.
        User user = User.getInstance();
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.119.39:8080/domain/pc?opt=24&name=" + "&page=" + PAGE + "&token=" + TOKEN;
        HttpGet getMethod = new HttpGet(url);
        ArrayList<ManagerVO> list = new ArrayList<ManagerVO>();
        HttpResponse response = null;
        HttpEntity entity = null;
        StringBuilder builder = new StringBuilder();
        JSONObject jsonObject = null;
        try {
            // 创建httpost.访问本地服务器网址
            response = httpClient.execute(getMethod);
            if (response.getEntity() != null) {
                //如果服务器端JSON没写对，这句是会出异常，是执行不过去的
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String s = reader.readLine();
                for (; s != null; s = reader.readLine()) {
                    builder.append(s);
                }
                JSONObject data;
                jsonObject = new JSONObject(builder.toString());
                TOKEN = jsonObject.getInt("token");
                user.setTOKEN(TOKEN);
                JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
                for (int i = 0; i < managerArray.length(); i++) {
                    data = managerArray.getJSONObject(i);
                    int id1 = data.getInt("id");
                    String name1 = data.getString("name");
                    String tel1 = data.getString("tel");
                    String mail1 = data.getString("mail");
                    list.add(new ManagerVO(id1, name1, tel1, mail1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (entity != null) {
                    httpClient.getConnectionManager().shutdown();//关闭连接
                    //这两种释放连接的方法都可以
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return list;

    }

    private class GetDataTask extends AsyncTask<Void, Void, List<ManagerVO>> {

        //后台处理部分
        @Override
        protected List<ManagerVO> doInBackground(Void... params) {
            // Simulates a background job.

            return getData();
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(List<ManagerVO> result) {
            //在头部增加新添内容
                ids.clear();
                ids.addAll(result);
                adapter.notifyDataSetChanged();
                mManagerList.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    public class ManagerListAdapter extends ArrayAdapter<ManagerVO> {
        private int mResourceId;

        public ManagerListAdapter(Context context, int resource, List<ManagerVO> objects) {
            super(context, resource, objects);
            mResourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ManagerVO managerVO = getItem(position);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);
            TextView nameText = (TextView) view.findViewById(R.id.name);
            TextView phoneText = (TextView) view.findViewById(R.id.phone);
            TextView mailText = (TextView) view.findViewById(R.id.mail);
            nameText.setText(managerVO.getmMangerName());
            phoneText.setText(managerVO.getmManagerTel());
            mailText.setText(managerVO.getmManagerMail());
            return view;
        }
    }

}
