package cn.mysilicon.housekeep.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.OrderAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Order;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private List<Order> orderList;
    private Integer user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //获取用户id
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);

        //获取数据
        getData(user_id);
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
        toolbar.setTitle("我的订单");
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
    private void getData(Integer user_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()//创建Request 对象
                        .url("http://mysilicon.cn/order/list?user_id=" + user_id)
                        .get()
                        .build();
                Call call = client.newCall(request);//创建Call对象
                Response response = null;
                String result = null;
                try {
                    response = call.execute();//得到Response 对象
                    result = response.body().string();//得到字符串
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(OrderActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    // 请求成功，处理结果
                    Log.d(TAG, "onResponse: " + result);
                    orderList = JSONObject.parseArray(result, Order.class);//将字符串转换为订单List
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

    @Override
    protected void onResume() {
        super.onResume();
        //获取数据
        getData(user_id);
    }
}