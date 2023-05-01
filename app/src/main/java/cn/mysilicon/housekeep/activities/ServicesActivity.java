package cn.mysilicon.housekeep.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.SearchResultAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.ServiceItemBean;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServicesActivity extends AppCompatActivity {
    private static final String TAG = "ServicesActivity";
    private RecyclerView mRvSearchResult;
    private SearchView mSearch;
    private ImageView mIvSwitch;
    private boolean isSingle = true;
    private SearchResultAdapter mAdapterSearchResult;
    private GridLayoutManager mLayoutManager;
    public static final int LIST_COLUMN_SINGLE = 1;
    public static final int LIST_COLUMN_DOUBLE = 2;
    private List<ServiceItemBean> ServiceItemBeanList = new ArrayList<>();
    private ServiceItemBean serviceItemBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传过来的数据
        Bundle bundle = getIntent().getExtras();
        String classification = bundle.getString("classification");
        String category = bundle.getString("category");
        Log.d(TAG, "onCreate: " + classification);
        setContentView(R.layout.activity_services);
        if (classification != null && category == null) {
            getClassificationData(classification);
        }
        if (category != null && classification == null) {
            getCategoryData(category);
        }

    }

    private void getCategoryData(String classification1) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/service/classification1?classification1=" + classification1)
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
                    Toast.makeText(ServicesActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    // 请求成功，处理结果
                    ServiceItemBeanList = JSONArray.parseArray(result, ServiceItemBean.class);
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void getClassificationData(String classification2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/service/classification2?classification2=" + classification2)
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
                    Toast.makeText(ServicesActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    // 请求成功，处理结果
                    ServiceItemBeanList = JSONArray.parseArray(result, ServiceItemBean.class);
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//完成主界面更新,拿到数据
                initView();
            }
        }
    };


    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("分类");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSearch = findViewById(R.id.searchview);
        mSearch.setIconifiedByDefault(false);
        mRvSearchResult = findViewById(R.id.recyclerview);
        mIvSwitch = findViewById(R.id.iv_switch);
        mLayoutManager = new GridLayoutManager(this, 2);
        // 默认是单行显示，每个item需要占用两个columns；
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int postion) {
                return LIST_COLUMN_DOUBLE;
            }
        });
        mRvSearchResult.setLayoutManager(mLayoutManager);
        mAdapterSearchResult = new SearchResultAdapter(ServiceItemBeanList, this, this);
        mAdapterSearchResult.setListColumn(LIST_COLUMN_SINGLE);
        mRvSearchResult.setAdapter(mAdapterSearchResult);
        mIvSwitch.setImageDrawable(ContextCompat.getDrawable(ServicesActivity.this, R.drawable.img_double));

        mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSingle = !isSingle;
                if (isSingle) {
                    mIvSwitch.setImageDrawable(ContextCompat.getDrawable(ServicesActivity.this, R.drawable.img_double));
                    mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            // 单行显示，每个item需要占用两个columns；
                            return LIST_COLUMN_DOUBLE;
                        }
                    });
                    mAdapterSearchResult.setListColumn(LIST_COLUMN_SINGLE);
                } else {
                    mIvSwitch.setImageDrawable(ContextCompat.getDrawable(ServicesActivity.this, R.drawable.img_single));
                    mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            // 双排显示
                            return LIST_COLUMN_SINGLE;
                        }
                    });
                    mAdapterSearchResult.setListColumn(LIST_COLUMN_DOUBLE);
                }

                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                // 定位在显示的item位置；
                mRvSearchResult.smoothScrollToPosition(firstVisibleItemPosition);

                mAdapterSearchResult.notifyItemRangeChanged(firstVisibleItemPosition, mAdapterSearchResult.getItemCount());
            }
        });

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mAdapterSearchResult.renewData();
                } else {
                    mAdapterSearchResult.getFilter(newText);
                }
                return false;
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
}