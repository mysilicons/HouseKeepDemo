package cn.mysilicon.housekeep.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.mysilicon.housekeep.R;

public class LoginActivity extends AppCompatActivity {
    private EditText login_username;
    private EditText login_password;
    private Button btn_login;
    private Button btnRegister;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        btnRegister = findViewById(R.id.btn_registerActivity);
        login_username = findViewById(R.id.edt_login_username);
        login_password = findViewById(R.id.edt_login_password);
        btn_login = findViewById(R.id.btn_login);

        Intent intent = getIntent();
        String username1 = intent.getStringExtra("username1");
        login_username.setText(username1);

        login_password.setTransformationMethod(PasswordTransformationMethod
                .getInstance());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameStr = login_username.getText().toString().trim();
                String passwordStr = login_password.getText().toString().trim();
                if (usernameStr.equals("") || passwordStr.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("uname", usernameStr);
                        jsonObject.put("pwd", passwordStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (usernameStr.length() != 11) {
                        Toast.makeText(LoginActivity.this, "手机号应为11位数", Toast.LENGTH_SHORT).show();
                    }
                    String url = "http://8.130.79.158/api/login";
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Log.d("服务端返回信息", jsonObject.toString());
                                String msg = jsonObject.getString("msg");
                                Integer user_id = jsonObject.getInt("user_id");
                                Log.d("msg", msg);
                                if (msg.equals("操作成功")) {
                                    //JSONObject detail = jsonObject.getJSONObject("uname");
                                    String username = jsonObject.getString("uname");
                                    //String username = detail.getString("uname");
                                    //写入SharedPreferences
                                    getSharedPreferences("user", MODE_PRIVATE)
                                            .edit()
                                            .putInt("user_id", user_id)
                                            .putString("username", usernameStr)
                                            .putString("password", passwordStr)
                                            .putBoolean("isLogin", true)
                                            .commit();
                                    Boolean mathResult = isPhone(usernameStr);
                                    if (mathResult) {
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "手机号格式有误", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (msg.equals("用户不存在/密码错误")) {
                                    Toast.makeText(LoginActivity.this, "用户名密码有误", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if (isMatch) {
            return true;
        } else {
            return false;
        }
    }
}
