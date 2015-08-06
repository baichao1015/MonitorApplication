package com.bangbang.baichao.monitorapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class LoginActivity extends Activity {
    private Button mlogin;
    private EditText mUsername;
    private EditText mPassword;
    protected final User user = User.getInstance();
    private String TAG = "LoginActivity";
    private int TOKEN;
    private int POWER;
    public static String LOGIN_REQUEST;
    public static final int LOGIN_SUCESS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mlogin = (Button) findViewById(R.id.loginBtn);
        mUsername = (EditText) findViewById(R.id.userName);
        mPassword = (EditText) findViewById(R.id.userPwd);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String userpwd = mPassword.getText().toString();
                AsyncHttpClient client = new AsyncHttpClient();
                String murl = "http://192.168.119.39:8080/domain/pc?opt=10&name="
                        + username + "&password=" + userpwd;
                client.get(murl, new AsyncHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        Log.d(TAG, "连接服务器成功");
                        Toast.makeText(getApplicationContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(responseBody));
                            int loginressult = jsonObject.getInt("code");
                            TOKEN = jsonObject.getInt("token");
                            user.setTOKEN(TOKEN);
                            JSONObject loginmsg = new JSONObject(jsonObject.getString("data"));
                            POWER = loginmsg.getInt("power");
                            if (loginressult == LOGIN_SUCESS_CODE) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                user.setTOKEN(TOKEN);
                                user.setPOWER(POWER);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "用户名密码错误，请重新登陆", Toast.LENGTH_SHORT).show();
                                mUsername.setText("");
                                mPassword.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // 输出错误信息
                        System.out.print("onFailure");
                        Log.d(TAG, "连接服务器失败");
                        Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
