package cn.mysilicon.housekeep.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import cn.mysilicon.housekeep.Adapter.PreOrderAddressAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Address;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PreOrderActivity extends AppCompatActivity {
    private static final String TAG = "PreOrderActivity";
    private int id;
    public static int address_id;
    private Integer user_id;
    private TextView tvDate;
    private TextView tvTime;
    public static TextView tvAddress;
    private Button btnSubmit;
    private RecyclerView rvPreOrder;
    private List<Address> addressesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);

        getAddress(user_id);
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                case 1:
                    Toast.makeText(PreOrderActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void getAddress(Integer user_id) {
        new Thread(() -> {
            // 创建OkHttpClient对象
            OkHttpClient client = new OkHttpClient();

            // 创建Request对象
            Request request = new Request.Builder()
                    .url("http://你的服务器地址/address/list?uid=" + user_id)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = null;
            String result = null;
            try {
                response = call.execute();
                result = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.code() != 200) {
                Looper.prepare();
                Toast.makeText(PreOrderActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                // 请求成功，处理结果
                addressesList = JSONArray.parseArray(result, Address.class);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("预约下单");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvAddress = findViewById(R.id.tv_address);
        rvPreOrder = findViewById(R.id.rv_pre_order);
        btnSubmit = findViewById(R.id.btn_submit_order);

        PreOrderAddressAdapter adapter = new PreOrderAddressAdapter(addressesList);
        rvPreOrder.setLayoutManager(new LinearLayoutManager(PreOrderActivity.this));
        rvPreOrder.setAdapter(adapter);

        //获取当前日期
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        Integer mYear = calendar.get(Calendar.YEAR);
        Integer mMonth = calendar.get(Calendar.MONTH);
        Integer mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        tvDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(PreOrderActivity.this, (view, year, month, dayOfMonth) -> {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                tvDate.setText(date);
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        tvTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(PreOrderActivity.this, (view, hourOfDay, minute1) -> {
                String time = hourOfDay + ":" + minute1;
                tvTime.setText(time);
            }, mHour, mMinute, true);
            timePickerDialog.show();
        });

        btnSubmit.setOnClickListener(v -> {
            if (tvDate.getText().toString().equals("请选择日期")) {
                Toast.makeText(PreOrderActivity.this, "请选择日期", Toast.LENGTH_SHORT).show();
            } else if (tvTime.getText().toString().equals("请选择时间")) {
                Toast.makeText(PreOrderActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
            } else if (tvAddress.getText().toString().equals("请选择地址")) {
                Toast.makeText(PreOrderActivity.this, "请选择地址", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(PreOrderActivity.this)
                        .setTitle("提示")
                        .setMessage("确定预约吗？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            String server_time = tvDate.getText().toString() + "+" + tvTime.getText().toString() + ":00";
                            buy(server_time, id, address_id, user_id);
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    private void buy(String server_time, int service_id, int address_id, int user_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();
                String url = "http://你的服务器地址/order/add?server_time=" + server_time + "&service_id=" + service_id + "&address_id=" + address_id + "&user_id=" + user_id;
                Log.d(TAG, "run: " + url);
                // 创建Request对象
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create("", null))
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(PreOrderActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    handler.sendEmptyMessage(1);
                }

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
}
