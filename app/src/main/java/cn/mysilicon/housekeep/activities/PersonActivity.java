package cn.mysilicon.housekeep.activities;

import static android.os.SystemClock.sleep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Address;
import cn.mysilicon.housekeep.model.AddressMatch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonActivity extends AppCompatActivity {

    public static final String TAG = "PersonActivity";
    private List<AddressMatch> addressesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        TextView AddAddress = findViewById(R.id.address_add_textview);
        AddAddress.setOnClickListener(v -> {
            //跳转到添加地址页面
            Intent intent = new Intent(PersonActivity.this, AddressManagerActivity.class);
            startActivity(intent);
        });

        initView();
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
            if (msg.what == 0) {//完成主界面更新,拿到数据
                Log.d(TAG, "拿到数据了准备更新UI");
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
                //创建一个simpleAdapter
                SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), listitem, R.layout.adress_item, new String[]{"title", "name", "phone"}, new int[]{R.id.address_title_textview, R.id.address_name_textview, R.id.address_phone_textview});
                ListView listView = (ListView) findViewById(R.id.address_listview);
                listView.setAdapter(myAdapter);
            }
        }
    };

    private void getAddressList(Integer user_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://8.130.79.158/address/get?uid=" + user_id)
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    Log.d(TAG, "onResponse: " + result);
                    addressesList = JSONArray.parseArray(result, AddressMatch.class);
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