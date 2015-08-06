package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.User;
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
public class ManagerListDemoFragment extends Fragment {

    private View mLayoutRoot;
    private int TOKEN;
    private int POWER;
    private int PAGE = 1;
    protected final User user = User.getInstance();
    private ListView mManagerList;
    public static final String CHECK_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=24&name=";
    public static final String MODIFY_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=23";
    public static final String DELETE_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=22&name=";

    public ManagerListDemoFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.managerlistdemo, container, false);
        mManagerList = (ListView) mLayoutRoot.findViewById(R.id.listView);
        AsyncHttpClient httpclient = new AsyncHttpClient();
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        String url = "http://192.168.119.39:8080/domain/pc?opt=24&name=" + "&page=" + PAGE + "&token=" +  TOKEN;
        httpclient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<ManagerVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data;
                    JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
                    for(int i=0;i<managerArray.length();i++){
                        data=managerArray.getJSONObject(i);
                        int id1 = data.getInt("id");
                        String name1=data.getString("name");
                        String tel1=data.getString("tel");
                        String mail1=data.getString("mail");
                        list.add(new ManagerVO(id1, name1, tel1, mail1));
                    }
                    ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String,Object>>();
                    for(ManagerVO manager : list){
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("name", manager.getmMangerName());
                        item.put("phone", manager.getmManagerTel());
                        item.put("mail", manager.getmManagerMail());
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

            }
        });
        mManagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return mLayoutRoot;
    }
}
