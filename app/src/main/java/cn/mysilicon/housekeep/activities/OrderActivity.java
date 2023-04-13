package cn.mysilicon.housekeep.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONStringer;

import java.io.IOException;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.OrderAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.CarResponse;
import cn.mysilicon.housekeep.model.Order;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //获取数据
        getData();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initViews();
                    break;
                default:
                    break;
            }
        }
    };

    private void initViews() {
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

        //获取RecyclerView的id
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_order);

        //设置recyclerView的布局管理器
        //第一个参数为上下文，第二个参数为布局的方向，第三个参数为是否倒序
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));

        //定义适配器,传入集合数据
        OrderAdapter adapter = new OrderAdapter(orderList, this);

        //为recyclerView设置适配器，即将适配器中数据传入recyclerView中
        recyclerView.setAdapter(adapter);
    }

    //获取数据
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://8.130.79.158/myorders/all")
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();
                    orderList = JSONObject.parseArray(result, Order.class);
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
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