package cn.mysilicon.housekeep.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.mysilicon.housekeep.CommentActivity;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Order;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private static final String TAG = "OrderDetailActivity";
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
    private Button btn_finish;
    private Button btn_cancel;
    private Button btn_comment;


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
                case 1:
                    Toast.makeText(OrderDetailActivity.this, "订单完成", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(OrderDetailActivity.this, "订单取消", Toast.LENGTH_SHORT).show();
                    finish();
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
        btn_finish = findViewById(R.id.btn_finish_order);
        btn_cancel = findViewById(R.id.btn_cancel_order);
        btn_comment = findViewById(R.id.btn_comment_order);

        if (order.getCur_status().equals("进行中")) {
            btn_finish.setEnabled(true);
            btn_cancel.setEnabled(true);
            btn_comment.setEnabled(false);
            btn_finish.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
            btn_comment.setVisibility(View.GONE);
            btn_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确认完成订单？");
                    builder.setPositiveButton("确认", (dialog, which) -> {
                        finish(order.getId());
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {
                    });
                    builder.show();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确认取消订单？");
                    builder.setPositiveButton("确认", (dialog, which) -> {
                        cancel(order.getId());
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {
                    });
                    builder.show();
                }
            });

        } else if (order.getCur_status().equals("已完成")) {
            btn_finish.setEnabled(false);
            btn_cancel.setEnabled(false);
            btn_comment.setEnabled(true);
            btn_finish.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_comment.setVisibility(View.VISIBLE);
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity.this, CommentDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", order.getService_id());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        } else if (order.getCur_status().equals("已取消")) {
            btn_finish.setEnabled(false);
            btn_cancel.setEnabled(false);
            btn_comment.setEnabled(false);
            btn_finish.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_comment.setVisibility(View.GONE);
        }


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

    private void cancel(int order_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/order/cancel?id=" + order_id)
                        .post(RequestBody.create("", null))
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(TAG, "run: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(OrderDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    handler.sendEmptyMessage(2);
                }

            }
        }).start();
    }

    private void finish(int order_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/order/finish?id=" + order_id)
                        .post(RequestBody.create("", null))
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(TAG, "run: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(OrderDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    handler.sendEmptyMessage(1);
                }

            }
        }).start();
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