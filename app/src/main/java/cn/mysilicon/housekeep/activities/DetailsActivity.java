package cn.mysilicon.housekeep.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.CommentAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Comment;
import cn.mysilicon.housekeep.model.DetailsBean;
import cn.mysilicon.housekeep.model.ServiceItemBean;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private List<Comment> commentList = new ArrayList<>();
    private ServiceItemBean serviceItemBean;
    private static Integer user_id;
    private String merchant_name;
    private RecyclerView recyclerView;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        //获取用户id
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        getData(id);
        getComment(id);
        initListener();
    }

    private void getData(Integer id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://你的服务器地址/service/detail?id=" + id)
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
                    Toast.makeText(DetailsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    // 请求成功，处理结果
                    serviceItemBean = JSON.parseObject(result, ServiceItemBean.class);
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void getComment(Integer id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://你的服务器地址/comment/list?service_id=" + id)
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
                    Toast.makeText(DetailsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    // 请求成功，处理结果
                    commentList = JSON.parseArray(result, Comment.class);
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initView();
                    initRecyclerView();
                    break;
                case 1:
                    Toast.makeText(DetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    merchant_name = (String) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        CommentAdapter adapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("服务详情");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView imageView = findViewById(R.id.service_imageview);
        TextView title = findViewById(R.id.service_title);
        TextView content = findViewById(R.id.service_content);
        TextView price = findViewById(R.id.service_price);

        Log.d(TAG, "initView: " + serviceItemBean.getImage_url());
        Glide.with(this).load(serviceItemBean.getImage_url()).into(imageView);


        title.setText(serviceItemBean.getTitle());
        content.setText(serviceItemBean.getContent());
        price.setText(serviceItemBean.getPrice());

        getConversationId(serviceItemBean.getMerchant_id());

    }

    private void initListener() {
        findViewById(R.id.my_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, CollectActivity.class);
                startActivity(intent);
            }

            ;
        });
        findViewById(R.id.collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(DetailsActivity.this)
                        .setTitle("提示")
                        .setMessage("是否收藏该服务？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            collect(serviceItemBean.getId());
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                        })
                        .create();
                alertDialog.show();
            }

            ;
        });
        findViewById(R.id.conect_seller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, MessageActivity.class);
                intent.putExtra("name", merchant_name);
                startActivity(intent);
            };
        });
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, PreOrderActivity.class);
                intent.putExtra("id", serviceItemBean.getId());
                startActivity(intent);
            }

            ;
        });

    }

    private void getConversationId(Integer merchant_id) {
        new Thread(() -> {
            // 创建OkHttpClient对象
            OkHttpClient client = new OkHttpClient();
            String url = "http://你的服务器地址/merchant/getConversationId?merchant_id=" + merchant_id;
            // 创建Request对象
            Request request = new Request.Builder()
                    .url(url)
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
                Toast.makeText(DetailsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                handler.sendMessage(handler.obtainMessage(2, result));
            }
        }).start();
    }

    private void collect(Integer service_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();
                String url = "http://你的服务器地址/collect/add?user_id=" + user_id + "&service_id=" + service_id;
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
                    Toast.makeText(DetailsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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