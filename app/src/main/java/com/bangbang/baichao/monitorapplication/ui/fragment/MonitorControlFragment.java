package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MonitorControlFragment extends Fragment {

    private View mLayoutRoot;
    private EditText mServe;
    private EditText mPath;
    private EditText mInterval;
    private EditText mTime;
    private EditText mThreshold;
    private EditText mManager;
    private Button mAddMonitor;
    protected final User user = User.getInstance();
    private int TOKEN;
    private int POWER;
    private String TAG = "MonitorControlFragment";
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
                                TOKEN = jsonObject.getInt("token");
                                user.setTOKEN(TOKEN);
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    Log.d(TAG, "添加监控成功");
                                    Toast.makeText(getActivity().getApplicationContext(), "添加监控成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "添加监控失败");
                                    Toast.makeText(getActivity().getApplicationContext(), "添加监控失败", Toast.LENGTH_SHORT).show();
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
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "用户没有权限进行此操作", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mLayoutRoot;
    }


}
