package cn.mysilicon.housekeep.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {
    private EditText edt_username;
    private EditText edt_password;
    private Button goLogin;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edt_username=findViewById(R.id.edt_register_username);
        edt_password=findViewById(R.id.edt_register_password);
        goLogin=findViewById(R.id.btn_goLogin);
        submit=findViewById(R.id.btn_submit);
        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username.getText().toString().trim();
                String password = edt_password.getText().toString().trim();

                goLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                if(username.equals("")||password.equals("")){
                    Toast.makeText(RegisterActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("uname",username);
                        jsonObject.put("pwd",password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //检查手机号是否合法
                    if (username.length() != 11) {
                        Toast.makeText(RegisterActivity.this, "手机号应为11位数", Toast.LENGTH_SHORT).show();
                    }
                    Boolean mathResult = isPhone(username);
                    if (mathResult ==false) {
                        Toast.makeText(RegisterActivity.this, "手机号格式有误", Toast.LENGTH_SHORT).show();
                    }

                    String url="http://8.130.79.158/api/register";
                    RequestQueue requestQueue=Volley.newRequestQueue(RegisterActivity.this);
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url,jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Log.d("注册信息", jsonObject.toString());
                                String msg = jsonObject.getString("msg");
                                if(msg.equals("操作成功")){
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    String token = jsonObject.getString("token");
                                    Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                                    if (token.equals("用户已存在")) {
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(RegisterActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
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
