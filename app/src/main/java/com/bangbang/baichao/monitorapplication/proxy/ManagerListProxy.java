package com.bangbang.baichao.monitorapplication.proxy;

import android.content.Context;
import android.os.Handler;

import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by baichao on 2015/8/6.
 */
public class ManagerListProxy extends BaseProxy {
    private int mManagerPage = 1;
    public int TOKEN;
    public static final String CHECK_MANAGERLIST = "http://192.168.119.39:8080/domain/pc?opt=24&name=";
    public static final String DELETE_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=22";

    public ManagerListProxy(Handler proxyCallbackHandler, Context stt) {
        super(proxyCallbackHandler, stt);
    }

    public void refreshDateList() {
        mManagerPage = 1;
        loadData();
    }

    public void loadMoreList() {
        mManagerPage++;
        loadData();
    }

    private void loadData() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        final ProxyEntity entity=new ProxyEntity();
        TOKEN = user.getTOKEN();
        String URL = CHECK_MANAGERLIST + "&page=" + mManagerPage + "&token=" + TOKEN;
        httpClient.get(URL, new AsyncHttpResponseHandler() {
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
                    entity.setData(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback(entity);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
