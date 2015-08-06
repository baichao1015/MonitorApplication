package com.bangbang.baichao.monitorapplication.ui.activity;

import android.app.Activity;
import android.os.Bundle;
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

public class EditManagerActivity extends Activity {
    private EditText name;
    private EditText tel;
    private EditText mail;
    private Button editconfirm;
    private Bundle bundle;
    private int TOKEN;
    private int POWER;
    private int id;
    protected final User user = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmanager);
        bundle = getIntent().getExtras();
        name = (EditText) findViewById(R.id.managerName);
        tel = (EditText) findViewById(R.id.managerTel);
        mail = (EditText) findViewById(R.id.managerMail);
        editconfirm = (Button) findViewById(R.id.confirm);
        id = bundle.getInt("id");
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        editconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient httpclient = new AsyncHttpClient();
                String managername = name.getText().toString();
                String managertel = tel.getText().toString();
                String managermail = mail.getText().toString();
                String url = "http://192.168.119.39:8080/domain/pc?opt=23&name=" + managername + "&tel=" + managertel + "&mail=" + managermail + "&id=" + id + "&token=" + TOKEN;
                httpclient.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String response = new String(responseBody);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = Integer.parseInt(jsonObject.getString("code"));
                            if ( code == 1 ) {
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
