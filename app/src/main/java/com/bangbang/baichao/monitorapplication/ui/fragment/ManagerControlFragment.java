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

public class ManagerControlFragment extends Fragment {
    private View mLayoutRoot;
    private EditText mManagerName;
    private EditText mManagerTel;
    private EditText mManagerMail;
    private Button mAddManager;
    private int TOKEN;
    private int POWER;
    protected final User user = User.getInstance();
    private static int ADD_MANAGER_SUCCESS = 1;
    String TAG = "ManagerControlFragment";

    public static ManagerControlFragment newInstance(String param1, String param2) {
        ManagerControlFragment fragment = new ManagerControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ManagerControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.fragment_manager_control, container, false);
        mManagerName = (EditText) mLayoutRoot.findViewById(R.id.managerName);
        mManagerTel = (EditText) mLayoutRoot.findViewById(R.id.managerTel);
        mManagerMail = (EditText) mLayoutRoot.findViewById(R.id.managerMail);
        mAddManager = (Button) mLayoutRoot.findViewById(R.id.addManager);

        mAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String managerName = mManagerName.getText().toString();
                String managerTel = mManagerTel.getText().toString();
                String managerMail = mManagerMail.getText().toString();
                TOKEN = user.getTOKEN();
                POWER = user.getPOWER();
                if (POWER == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "用户没有权限进行此操作", Toast.LENGTH_SHORT).show();
                } else {
                    String mUrl = "http://192.168.119.39:8080/domain/pc?opt=21&name="
                            + managerName + "&tel=" + managerTel + "&mail=" + managerMail + "&token=" + TOKEN;
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
                                if (code == ADD_MANAGER_SUCCESS) {
                                    Log.d(TAG, "添加接口人成功");
                                    Toast.makeText(getActivity().getApplicationContext(), "添加接口人成功", Toast.LENGTH_SHORT).show();
                                    mManagerName.setText("");
                                    mManagerTel.setText("");
                                    mManagerMail.setText("");
                                } else {
                                    Log.d(TAG, "添加接口人失败");
                                    Toast.makeText(getActivity().getApplicationContext(), "添加接口人失败", Toast.LENGTH_SHORT).show();
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
                }

            }
        });


        return mLayoutRoot;
    }


}
