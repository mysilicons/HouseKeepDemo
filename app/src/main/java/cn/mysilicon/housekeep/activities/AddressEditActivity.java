package cn.mysilicon.housekeep.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.io.IOException;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Address;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AddressEditActivity extends AppCompatActivity {
    private static final String TAG = "AddressEditActivity";
    EditText name_edit;
    EditText phone_edit;
    EditText address_edit;

    TextView mTvCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("地址管理");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id");
        String name = bundle.getString("name");
        String phone = bundle.getString("phone");
        String address = bundle.getString("address");

        name_edit = findViewById(R.id.et_receiver);
        phone_edit = findViewById(R.id.et_phone);
        address_edit = findViewById(R.id.et_address);
        mTvCity = findViewById(R.id.tv_city_pick);
        name_edit.setText(name);
        phone_edit.setText(phone);
        address_edit.setText(address);
        Button BtnSave = findViewById(R.id.btn_save_address);
        //保存地址
        BtnSave.setOnClickListener(v -> {
            String nameStr = name_edit.getText().toString();
            String phoneStr = phone_edit.getText().toString();
            String addressStr = address_edit.getText().toString();
            if (nameStr.equals("") || phoneStr.equals("") || addressStr.equals("")) {
                Toast.makeText(AddressEditActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                Integer user_id = sharedPreferences.getInt("user_id", 0);
                Address new_address = new Address(id, user_id, nameStr, phoneStr, addressStr);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressEditActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定保存地址吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    //发送地址信息到服务器
                    editAddress(new_address);
                });
                builder.setNegativeButton("取消", (dialog, which) -> {
                });
                builder.show();
            }
        });
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

    //Handler
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(AddressEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void editAddress(Address newAddress) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String json = gson.toJson(newAddress);
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(json, okhttp3.MediaType.parse("application/json; charset=utf-8"));
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://你的服务器地址/address/edit")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.code() != 200) {
                Looper.prepare();
                Toast.makeText(AddressEditActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
