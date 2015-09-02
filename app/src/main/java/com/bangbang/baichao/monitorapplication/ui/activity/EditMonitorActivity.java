package com.bangbang.baichao.monitorapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditMonitorActivity extends Activity {
    private EditText serve;
    private EditText interval;
    private EditText threshold;
    private EditText time;
    private EditText name;
    private Button editconfirm;
    private Bundle bundle;
    private String TOKEN;
    private int POWER;
    private int id;
    protected final User user = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmonitor);
        bundle = getIntent().getExtras();
        serve = (EditText) findViewById(R.id.serve);
        interval = (EditText) findViewById(R.id.interval);
        threshold = (EditText) findViewById(R.id.threshold);
        time = (EditText) findViewById(R.id.time);
        name = (EditText) findViewById(R.id.managername);
        editconfirm = (Button) findViewById(R.id.confirm);
        id = bundle.getInt("id");
        TOKEN = user.getTOKEN();
        POWER = user.getPOWER();
        editconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient httpclient = new AsyncHttpClient();
                String mServe = serve.getText().toString();
                String mInterval = interval.getText().toString();
                String mThreshold = threshold.getText().toString();
                String mTime = time.getText().toString();
                String mName = name.getText().toString();
                String url = "http://192.168.119.39:8080/domain/pc?opt=23&serve=" + mServe + "&interval=" + mInterval + "&threshold=" + mThreshold + "&time=" + mTime+ "&name=" + mName+ "&id=" + id + "&token=" + TOKEN;
                httpclient.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String response = new String(responseBody);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = Integer.parseInt(jsonObject.getString("code"));
                            TOKEN = jsonObject.getString("token");
                            user.setTOKEN(TOKEN);
                            if ( code == 1 ) {
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (code == 0){
                                Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                            } else if (code == 2){
                                Toast.makeText(getApplicationContext(), "账号异常", Toast.LENGTH_SHORT).show();
                                HttpUtils.UserExit();
                                user.init();
                                Intent intent = new Intent(EditMonitorActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
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
