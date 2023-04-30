package cn.mysilicon.housekeep.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mysilicon.housekeep.Adapter.AddressAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Address;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonActivity extends AppCompatActivity {

    public static final String TAG = "PersonActivity";
    private List<Address> addressesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        TextView AddAddress = findViewById(R.id.address_add_textview);
        Button logout_button = findViewById(R.id.logout_button);
        AddAddress.setOnClickListener(v -> {
            //跳转到添加地址页面
            Intent intent = new Intent(PersonActivity.this, AddressManagerActivity.class);
            startActivity(intent);
        });
        logout_button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定要注销吗？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");
                deleteUserInfo(username, password);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();
        });


        initView();
    }


    private void deleteUserInfo(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/user/delete?username=" + username + "&password=" + password)
                        .post(RequestBody.create("", null))
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }


    /**
     * 监听标题栏按钮点击事件.
     *
     * @param item 按钮
     * @return 结果
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Integer user_id = sharedPreferences.getInt("user_id", 0);
        String username = sharedPreferences.getString("username", "");

        TextView unameTv = findViewById(R.id.username_textview);
        unameTv.setText(username);

        getAddressList(user_id);
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //更新UI
                    Integer size = addressesList.size();
                    String[] title = new String[size];
                    String[] name = new String[size];
                    String[] phone = new String[size];
                    Log.d(TAG, "长度: " + addressesList.size());
                    for (int i = 0; i < addressesList.size(); i++) {
                        title[i] = addressesList.get(i).getAddress();
                        name[i] = addressesList.get(i).getUname();
                        phone[i] = addressesList.get(i).getPhone();
                    }
                    List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < name.length; i++) {
                        Map<String, Object> showitem = new HashMap<String, Object>();
                        showitem.put("title", title[i]);
                        showitem.put("name", name[i]);
                        showitem.put("phone", phone[i]);
                        listitem.add(showitem);
                    }
                    AddressAdapter addressAdapter = new AddressAdapter(addressesList, PersonActivity.this);
                    RecyclerView recyclerView = findViewById(R.id.address_listview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PersonActivity.this));
                    recyclerView.setAdapter(addressAdapter);
                    break;
                case 1:
                    Toast.makeText(PersonActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }

        }

        ;
    };

    private void getAddressList(Integer user_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/address/list?uid=" + user_id)
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    Log.d(TAG, "onResponse: " + result);
                    addressesList = JSONArray.parseArray(result, Address.class);
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新页面
        initView();
    }
}