package com.bangbang.baichao.monitorapplication.ui.fragment;

import android.content.Intent;
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
import com.bangbang.baichao.monitorapplication.ui.activity.LoginActivity;
import com.bangbang.baichao.monitorapplication.ui.activity.MainActivity;
import com.bangbang.baichao.monitorapplication.utils.HttpUtils;
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
    private String TOKEN;
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
                if (!managerTel.matches("1[358][0-9]{9,9}")){
                    mManagerTel.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "电话号码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!managerMail.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")){
                    mManagerMail.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "邮件格式不合法，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                                TOKEN = jsonObject.getString("token");
                                user.setTOKEN(TOKEN);
                                int code = jsonObject.getInt("code");
                                if (code == ADD_MANAGER_SUCCESS) {
                                    Log.d(TAG, "添加接口人成功");
                                    Toast.makeText(getActivity().getApplicationContext(), "添加接口人成功", Toast.LENGTH_SHORT).show();
                                    mManagerName.setText("");
                                    mManagerTel.setText("");
                                    mManagerMail.setText("");
                                } else if (code == 0){
                                    Log.d(TAG, "添加接口人失败"+ "code="+ code );
                                    Toast.makeText(getActivity().getApplicationContext(), "添加接口人失败", Toast.LENGTH_SHORT).show();
                                } else if (code == 2){
                                    Toast.makeText(getActivity().getApplicationContext(), "账号异常", Toast.LENGTH_SHORT).show();
                                    try {
                                        HttpUtils.UserExit();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    user.init();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
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
