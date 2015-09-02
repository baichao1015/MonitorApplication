package com.bangbang.baichao.monitorapplication.proxy;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.TrackVO;
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
public class TrackOfMonitorProxy extends BaseProxy {
    private int mTrackPage = 1;
    public String TOKEN;

    public static final String TRACK_MONITORLIST = "http://192.168.119.39:8080/domain/pc?opt=41&page=";
    public static final String GET_TRACKMONITOR_LIST = "get_trackmonitor_list";

    public TrackOfMonitorProxy(Handler proxyCallbackHandler, Context stt) {
        super(proxyCallbackHandler, stt);
    }

    public void refreshDateList() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        final ProxyEntity entity=new ProxyEntity();
        TOKEN = user.getTOKEN();
        Log.d("token",TOKEN );
        String URL = TRACK_MONITORLIST + mTrackPage + "&token=" + TOKEN;
        httpClient.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<TrackVO> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data;
                    TOKEN = jsonObject.getString("token");
                    user.setTOKEN(TOKEN);
                    JSONArray managerArray = new JSONArray(jsonObject.getString("data"));
                    for (int i = 0; i < managerArray.length(); i++) {
                        data = managerArray.getJSONObject(i);
                        String serve = data.getString("serve");
                        String time = data.getString("time");
                        String monitor_count = data.getString("monitorCount");
                        String fail_count = data.getString("failCount");
                        list.add(new TrackVO(serve, time, monitor_count, fail_count));
                    }
                    entity.setAction(GET_TRACKMONITOR_LIST);
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

}
