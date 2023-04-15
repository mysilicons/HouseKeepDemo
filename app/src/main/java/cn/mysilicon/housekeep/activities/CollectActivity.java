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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.StoreAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.CarResponse;
import cn.mysilicon.housekeep.utils.GoodsCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 购物车
 *
 * @author llw
 */
public class CollectActivity extends AppCompatActivity implements GoodsCallback, View.OnClickListener {

    public static final String TAG = "CollectActivity";

    private RecyclerView rvStore;
    private StoreAdapter storeAdapter;
    private List<CarResponse.OrderDataBean> mList = new ArrayList<>();
    private TextView tvEdit;//编辑
    private ImageView ivCheckedAll;//全选
    private TextView tvTotal;//合计价格
    private TextView tvSettlement;//结算
    private LinearLayout layEdit;//编辑底部布局
    private TextView tvDeleteGoods;//删除服务

    private boolean isEdit = false;//是否编辑
    private boolean isAllChecked = false;//是否全选

    private List<Integer> shopIdList = new ArrayList<>();//店铺列表

    private double totalPrice = 0.00;//服务总价
    private int totalCount = 0;//服务总数量

    private AlertDialog dialog;//弹窗

    private boolean isHaveGoods = false;//购物车是否有服务

    private SmartRefreshLayout refresh;//刷新布局
    private LinearLayout layEmpty;//空布局
    private CarResponse carResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        //初始标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getData();
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


    /**
     * 初始化
     */
    private void initView() throws IOException {
        //设置亮色状态栏模式 systemUiVisibility在Android11中弃用了，可以尝试一下。
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        rvStore = findViewById(R.id.rv_store);
        tvEdit = findViewById(R.id.tv_edit);
        ivCheckedAll = findViewById(R.id.iv_checked_all);
        tvTotal = findViewById(R.id.tv_total);
        tvSettlement = findViewById(R.id.tv_settlement);
        layEdit = findViewById(R.id.lay_edit);
        tvDeleteGoods = findViewById(R.id.tv_delete_goods);
        refresh = findViewById(R.id.refresh);
        layEmpty = findViewById(R.id.lay_empty);
        //禁用下拉刷新和上拉加载更多
        refresh.setEnableRefresh(false);
        refresh.setEnableLoadMore(false);
        //下拉刷新监听
        refresh.setOnRefreshListener(refreshLayout -> {
            try {
                initView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tvEdit.setOnClickListener(this);
        ivCheckedAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        tvDeleteGoods.setOnClickListener(this);

        Log.d(TAG, "initView: " + carResponse.toString());
        mList.addAll(carResponse.getOrderData());
        storeAdapter = new StoreAdapter(R.layout.item_store, mList, this);
        rvStore.setLayoutManager(new LinearLayoutManager(this));
        rvStore.setAdapter(storeAdapter);

        //店铺点击
        storeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CarResponse.OrderDataBean storeBean = mList.get(position);
                if (view.getId() == R.id.iv_checked_store) {
                    storeBean.setChecked(!storeBean.isChecked());
                    storeAdapter.notifyDataSetChanged();
                    //传递选中的id
                    if (storeBean.isChecked()) {
                        //全选服务
                        storeAdapter.controlGoods(storeBean.getShopId(), true);

                        //添加到列表中
                        if (!shopIdList.contains(storeBean.getShopId())) {
                            //如果列表中没有这个Id且当前分类为选中状态
                            shopIdList.add(storeBean.getShopId());
                        }
                    } else {
                        //清除全选服务
                        storeAdapter.controlGoods(storeBean.getShopId(), false);

                        //从列表中清除
                        if (shopIdList.contains(storeBean.getShopId())) {
                            shopIdList.remove((Integer) storeBean.getShopId());
                        }
                    }
                    //控制页面全选
                    controlAllChecked();
                }
            }
        });
        //有服务
        isHaveGoods = true;
        //下拉加载数据完成后，关闭下拉刷新动画
        refresh.finishRefresh();
        //隐藏布局
        layEmpty.setVisibility(View.GONE);
    }


    /**
     * 控制页面全选   与页面全选进行交互
     */
    private void controlAllChecked() {
        if (shopIdList.size() == mList.size() && mList.size() != 0) {
            //全选
            ivCheckedAll.setImageDrawable(getDrawable(R.drawable.ic_checked));
            isAllChecked = true;
        } else {
            //不全选
            ivCheckedAll.setImageDrawable(getDrawable(R.drawable.ic_check));
            isAllChecked = false;
        }
        //计算价格
        calculationPrice();
    }

    /**
     * 选中店铺
     *
     * @param shopId 店铺id
     */
    @Override
    public void checkedStore(int shopId, boolean state) {
        for (CarResponse.OrderDataBean bean : mList) {
            //遍历
            if (shopId == bean.getShopId()) {
                bean.setChecked(state);
                storeAdapter.notifyDataSetChanged();
                //记录选中店铺的shopid,添加到一个列表中。
                if (!shopIdList.contains(bean.getShopId()) && state) {
                    //如果列表中没有这个店铺Id且当前店铺为选中状态
                    shopIdList.add(bean.getShopId());
                } else {
                    if (shopIdList.contains(bean.getShopId())) {
                        //通过list.indexOf获取属性的在列表中的下标，不过强转Integer更简洁
                        shopIdList.remove((Integer) bean.getShopId());
                    }
                }
            }
        }
        //控制页面全选
        controlAllChecked();
    }


    /**
     * 服务价格
     */
    @Override
    public void calculationPrice() {
        //每次计算之前先置零
        totalPrice = 0.00;
        totalCount = 0;
        //循环购物车中的店铺列表
        for (int i = 0; i < mList.size(); i++) {
            CarResponse.OrderDataBean orderDataBean = mList.get(i);
            //循环店铺中的服务列表
            for (int j = 0; j < orderDataBean.getCartlist().size(); j++) {
                CarResponse.OrderDataBean.CartlistBean cartlistBean = orderDataBean.getCartlist().get(j);
                //当有选中服务时计算数量和价格
                if (cartlistBean.isChecked()) {
                    totalCount++;
                    totalPrice += cartlistBean.getPrice();
                }
            }
        }
        tvTotal.setText("￥" + totalPrice);
        tvSettlement.setText(totalCount == 0 ? "结算" : "结算(" + totalCount + ")");
    }

    /**
     * 页面控件点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit://编辑
                if (!isHaveGoods) {
                    showMsg("当前空空如也~");
                    return;
                }
                if (isEdit) {
                    tvEdit.setText("编辑");
                    layEdit.setVisibility(View.GONE);
                    isEdit = false;
                } else {
                    tvEdit.setText("完成");
                    layEdit.setVisibility(View.VISIBLE);
                    isEdit = true;
                }
                break;
            case R.id.iv_checked_all://全选
                if (!isHaveGoods) {
                    showMsg("当前空空如也~");
                    return;
                }
                if (isAllChecked) {
                    //取消全选
                    isSelectAllStore(false);
                } else {
                    //全选
                    isSelectAllStore(true);
                }
                break;
            case R.id.tv_settlement://结算
                if (!isHaveGoods) {
                    showMsg("当前空空如也~");
                    return;
                }
                if (totalCount == 0) {
                    showMsg("请选择要结算的服务");
                    return;
                }
                //弹窗
                dialog = new AlertDialog.Builder(this)
                        .setMessage("总计:" + totalCount + "种服务，" + totalPrice + "元")
                        .setPositiveButton("确定", (dialog, which) -> takeOrder())
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .create();
                dialog.show();
                break;
            case R.id.tv_delete_goods://删除
                if (totalCount == 0) {
                    showMsg("请选择要删除的服务");
                    return;
                }
                //弹窗
                dialog = new AlertDialog.Builder(this)
                        .setMessage("确定要删除所选服务吗?")
                        .setPositiveButton("确定", (dialog, which) -> deleteGoods())
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .create();
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void takeOrder() {
        //店铺列表
        List<CarResponse.OrderDataBean> storeList = new ArrayList<>();

        for (int i = 0; i < mList.size(); i++) {
            CarResponse.OrderDataBean store = mList.get(i);
            if (store.isChecked()) {
                //店铺如果选择则添加到此列表中
                storeList.add(store);
            }
            //服务列表
            List<CarResponse.OrderDataBean.CartlistBean> goodsList = new ArrayList<>();

            List<CarResponse.OrderDataBean.CartlistBean> goods = store.getCartlist();
            //循环店铺中的服务列表
            for (int j = 0; j < goods.size(); j++) {
                CarResponse.OrderDataBean.CartlistBean cartlistBean = goods.get(j);
                //当有选中服务时添加到此列表中
                if (cartlistBean.isChecked()) {
                    goodsList.add(cartlistBean);
                    buy(cartlistBean.getProductId());
                    delete(cartlistBean.getProductId());
                }
            }
            //删除服务
            goods.removeAll(goodsList);
        }
        //删除店铺
        mList.removeAll(storeList);

        shopIdList.clear();//删除了选中服务，清空已选择的标识
        controlAllChecked();//控制去全选
        //改变界面UI
        tvEdit.setText("编辑");
        layEdit.setVisibility(View.GONE);
        isEdit = false;
        //刷新数据
        storeAdapter.notifyDataSetChanged();
    }


    /**
     * 删除服务
     */
    private void deleteGoods() {
        //店铺列表
        List<CarResponse.OrderDataBean> storeList = new ArrayList<>();

        for (int i = 0; i < mList.size(); i++) {
            CarResponse.OrderDataBean store = mList.get(i);
            if (store.isChecked()) {
                //店铺如果选择则添加到此列表中
                storeList.add(store);
            }
            //服务列表
            List<CarResponse.OrderDataBean.CartlistBean> goodsList = new ArrayList<>();

            List<CarResponse.OrderDataBean.CartlistBean> goods = store.getCartlist();
            //循环店铺中的服务列表
            for (int j = 0; j < goods.size(); j++) {
                CarResponse.OrderDataBean.CartlistBean cartlistBean = goods.get(j);
                //当有选中服务时添加到此列表中
                if (cartlistBean.isChecked()) {
                    goodsList.add(cartlistBean);
                    delete(cartlistBean.getProductId());
                    System.out.println("cartlistBean.getProductId() = " + cartlistBean.getProductId());
                }
            }
            //删除服务
            goods.removeAll(goodsList);
        }
        //删除店铺
        mList.removeAll(storeList);

        shopIdList.clear();//删除了选中服务，清空已选择的标识
        controlAllChecked();//控制去全选
        //改变界面UI
        tvEdit.setText("编辑");
        layEdit.setVisibility(View.GONE);
        isEdit = false;
        //刷新数据
        storeAdapter.notifyDataSetChanged();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        initView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    Toast.makeText(CollectActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(CollectActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CollectActivity.this, OrderActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 获取数据
     */
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://8.130.79.158/orders")
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();
                    carResponse = gson.fromJson(result, CarResponse.class);
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                        .url("http://8.130.79.158/orders/delete?id=" + productId)
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
                        .url("http://8.130.79.158/myorders/buy?product_id=" + productId)
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


    /**
     * 是否全选
     *
     * @param state 状态
     */
    private void isSelectAllStore(boolean state) {
        //修改背景
        ivCheckedAll.setImageDrawable(getDrawable(state ? R.drawable.ic_checked : R.drawable.ic_check));

        for (CarResponse.OrderDataBean orderDataBean : mList) {
            //服务是否选中
            storeAdapter.controlGoods(orderDataBean.getShopId(), state);
            //店铺是否选中
            checkedStore(orderDataBean.getShopId(), state);
        }
        isAllChecked = state;
    }

    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
