package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.ui.activity.EditManagerActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 58 on 2015/7/23.
 */
public class ManagerListFragment extends Fragment {

    private View mLayoutRoot;
    private String TOKEN;
    private int POWER;
    private int PAGE = 1;
    protected final User user = User.getInstance();
    private ListView mManagerList;
    private ArrayList ids;
    private int managerid;


    public static final String CHECK_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=24&name=";
    public static final String MODIFY_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=23";
    public static final String DELETE_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=22";

    public ManagerListFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.manager_list, container, false);
        mManagerList = (ListView) mLayoutRoot.findViewById(R.id.listView);
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        AsyncHttpClient httpclient = new AsyncHttpClient();
        String url = "http://192.168.119.39:8080/domain/pc?opt=24&name=" + "&page=" + PAGE + "&token=" + TOKEN;
        httpclient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<ManagerVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data;
                    TOKEN = jsonObject.getString("token");
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
                    if(ids != null){
                        ids.clear();
                    }else{
                        ids = new ArrayList();
                    }
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
                    TOKEN = jsonObject.getString("token");
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
}
