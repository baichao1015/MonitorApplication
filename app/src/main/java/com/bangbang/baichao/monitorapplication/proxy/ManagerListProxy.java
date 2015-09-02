package com.bangbang.baichao.monitorapplication.proxy;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
 * Created by baichao on 2015/8/6.
 */
public class ManagerListProxy extends BaseProxy {
    private int mManagerPage = 1;
    public String TOKEN;
    public static final String CHECK_MANAGERLIST = "http://192.168.119.39:8080/domain/pc?opt=24&name=";
    public static final String DELETE_MANAGER = "http://192.168.119.39:8080/domain/pc?opt=22";
    public static final String GET_MANAGERLIST = "GET_MANAGERLIST";
    public static final String GET_MORELIST = "GET_MORELIST";
    public static final String DELETE_ID = "DELETE_ID";
    public static final String FAIL_USER_CONFLICT = "FAIL_USER_CONFLICT";
    public static final String DELETE_MANAGER_SUCCESS = "DELETE_MANAGER_SUCCESS";
    public static final String DELETE_MANAGER_FAIL = "DELETE_MANAGER_FAIL";
    public static final String GET_MANAGERNAME_LIST = "GET_MANAGERNAME_LIST";

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
        Log.d("token",TOKEN );
        String URL = CHECK_MANAGERLIST + "&page=" + mManagerPage + "&token=" + TOKEN;
        httpClient.get(URL, new AsyncHttpResponseHandler() {
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
                    entity.setAction(GET_MANAGERLIST);
                    String meg = list.toString();
                    entity.setData(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback(entity);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("managerlistproxy", "连接服务器失败");
            }
        });
    }
    public void deleteManager(int id) {
        TOKEN = user.getTOKEN();
        final ProxyEntity entity = new ProxyEntity();
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
                    int codemsg =  jsonObject.getInt("code");
                    user.setTOKEN(TOKEN);
                    if ( codemsg == 1 ){
                        Log.d("managerlistproxy", "删除成功");
                        entity.setAction(DELETE_MANAGER_SUCCESS);
                        callback(entity);
                    }else if ( codemsg == 0 ){
                        Log.d("managerlistproxy", "删除失败");
                        entity.setAction(DELETE_MANAGER_FAIL);
                        callback(entity);
                    }else if ( codemsg == 2 ){
                        Log.d("managerlistproxy", "被踢");
                        entity.setAction(FAIL_USER_CONFLICT);
                        callback(entity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
//    public void get_managerlist(){
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        final ProxyEntity entity=new ProxyEntity();
//        TOKEN = user.getTOKEN();
//        Log.d("token",TOKEN );
//        String URL = CHECK_MANAGERLIST + "&page=" + mManagerPage + "&token=" + TOKEN;
//        httpClient.get(URL, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String response = new String(responseBody);
//                ArrayList<String> list = new ArrayList<>();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject data;
//                    TOKEN = jsonObject.getString("token");
//                    user.setTOKEN(TOKEN);
//                    JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
//                    for (int i = 0; i < managerArray.length(); i++) {
//                        data = managerArray.getJSONObject(i);
//                        String name1 = data.getString("name");
//                        list.add(name1);
//                    }
//                    entity.setAction(GET_MANAGERNAME_LIST);
//                    entity.setData(list);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                callback(entity);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("managerlistproxy", "连接服务器失败");
//            }
//        });
//    }

}
