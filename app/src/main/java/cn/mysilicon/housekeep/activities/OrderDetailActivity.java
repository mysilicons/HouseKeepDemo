package cn.mysilicon.housekeep.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Order;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private Integer order_id;
    private Order order;
    private ImageView order_image;
    private TextView order_date;
    private TextView order_number;
    private TextView order_name;
    private TextView order_content;
    private TextView order_price;
    private TextView server_time;
    private TextView server_address;
    private TextView order_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Bundle bundle = getIntent().getExtras();
        order_id = bundle.getInt("id");
        getOrderData(order_id);
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("订单详情");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        order_image = findViewById(R.id.iv_order_image);
        order_date = findViewById(R.id.tv_order_time);
        order_number = findViewById(R.id.tv_order_number);
        order_name = findViewById(R.id.tv_order_name);
        order_content = findViewById(R.id.tv_order_content);
        order_price = findViewById(R.id.tv_order_price);
        server_time = findViewById(R.id.tv_server_time);
        server_address = findViewById(R.id.tv_service_address);
        order_status = findViewById(R.id.tv_order_status);

        Glide.with(this).load(order.getImage()).into(order_image);
        order_date.setText(order.getOrder_time());
        order_number.setText(String.valueOf(order.getOrder_number()));
        order_name.setText(order.getName());
        order_content.setText(order.getContent());
        order_price.setText(order.getPrice().toString());
        server_time.setText(order.getServer_time());
        server_address.setText(order.getAddress());
        order_status.setText(order.getCur_status());
    }

    private void getOrderData(Integer orderId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();
                String url = "http://mysilicon.cn/order/get?id=" + orderId;
                // 创建Request对象
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Response response = null;
                String result = null;
                try {
                    response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(OrderDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    order = JSONArray.parseObject(result, Order.class);
                    handler.sendEmptyMessage(0);
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