package cn.mysilicon.housekeep.activities;

import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.DetailsBean;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private final List<DetailsBean> detailsBeanList = new ArrayList<>();
    private DetailsBean detailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id");
        getData(id);
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
                        .url("http://mysilicon.cn/service/detail?id=" + id)
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    JSONArray jsonArray = JSONArray.parseArray(result);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        detailsBean = new DetailsBean();
                        detailsBean.setId(jsonArray.getJSONObject(i).getInteger("id"));
                        detailsBean.setImg(jsonArray.getJSONObject(i).getString("image_url"));
                        detailsBean.setTitle(jsonArray.getJSONObject(i).getString("title"));
                        detailsBean.setContent(jsonArray.getJSONObject(i).getString("content"));
                        detailsBean.setPrice(jsonArray.getJSONObject(i).getString("price"));
                        detailsBeanList.add(detailsBean);
                    }
                    Log.i(TAG, "run: " + detailsBeanList);
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    initView();
                    break;

                case 1:
                    Toast.makeText(DetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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

        Glide.with(this).load(detailsBeanList.get(0).getImg()).into(imageView);
        title.setText(detailsBeanList.get(0).getTitle());
        content.setText(detailsBeanList.get(0).getContent());
        price.setText(detailsBeanList.get(0).getPrice());
    }

    private void initListener() {
        findViewById(R.id.my_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, CollectActivity.class);
                startActivity(intent);
            };
        });
        findViewById(R.id.collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect(detailsBean.getId());
            };
        });
        findViewById(R.id.conect_seller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(android.net.Uri.parse("tel:028-88888888"));
                startActivity(intent);
            };
        });
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy(detailsBean.getId());
                delete(detailsBean.getId());
                Toast.makeText(DetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailsActivity.this, OrderActivity.class);
                startActivity(intent);
            };
        });

    }

    private void collect(Integer id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/orders/add?id=" + id)
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

    private void buy(int productId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/myorders/buy?product_id=" + productId)
                        .post(RequestBody.create("", null))
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

    private void delete(int productId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/orders/delete?id=" + productId)
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
}