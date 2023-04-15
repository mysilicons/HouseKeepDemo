package cn.mysilicon.housekeep.Adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.DetailsActivity;
import cn.mysilicon.housekeep.model.CarResponse;
import cn.mysilicon.housekeep.utils.GoodsCallback;

/**
 * 店铺适配器
 *
 * @author llw
 */
public class StoreAdapter extends BaseQuickAdapter<CarResponse.OrderDataBean, BaseViewHolder> {

    private RecyclerView rvGood;
    //商品回调
    private GoodsCallback goodsCallback;
    //店铺对象
    private List<CarResponse.OrderDataBean> storeBean;

    public StoreAdapter(int layoutResId, @Nullable List<CarResponse.OrderDataBean> data, GoodsCallback goodsCallback) {
        super(layoutResId, data);
        this.goodsCallback = goodsCallback;
        storeBean = data;//赋值
    }

    @Override
    protected void convert(BaseViewHolder helper, final CarResponse.OrderDataBean item) {
        rvGood = helper.getView(R.id.rv_goods);
        helper.setText(R.id.tv_store_name, item.getShopName());

        ImageView checkedStore = helper.getView(R.id.iv_checked_store);
        if (item.isChecked()) {
            checkedStore.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
        } else {
            checkedStore.setImageDrawable(mContext.getDrawable(R.drawable.ic_check));
        }
        //点击事件
        helper.addOnClickListener(R.id.iv_checked_store);//选中店铺


        final GoodsAdapter goodsAdapter = new GoodsAdapter(R.layout.item_good, item.getCartlist());
        rvGood.setLayoutManager(new LinearLayoutManager(mContext));
        rvGood.setAdapter(goodsAdapter);

        //商品item中的点击事件
        goodsAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CarResponse.OrderDataBean.CartlistBean goodsBean = item.getCartlist().get(position);
                switch (view.getId()) {
                    case R.id.iv_checked_goods://选中商品
                        //如果已选中则取消选中，未选中则选中
                        goodsBean.setChecked(!goodsBean.isChecked());
                        //刷新适配器
                        goodsAdapter.notifyDataSetChanged();
                        //控制店铺是否选中
                        controlStore(item);
                        //商品控制价格
                        goodsCallback.calculationPrice();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void jumpGoodsDetail(int productId) {
        //跳转到商品详情
        Intent intent = new Intent(mContext, DetailsActivity.class);
        intent.putExtra("id", productId);
        mContext.startActivity(intent);
    }

    /**
     * 控制店铺是否选中
     */
    private void controlStore(CarResponse.OrderDataBean item) {
        int num = 0;
        for (CarResponse.OrderDataBean.CartlistBean bean : item.getCartlist()) {
            if (bean.isChecked()) {
                ++num;
            }
        }
        if (num == item.getCartlist().size()) {
            //全选中  传递需要选中的店铺的id过去
            goodsCallback.checkedStore(item.getShopId(), true);
        } else {
            goodsCallback.checkedStore(item.getShopId(), false);
        }
    }

    /**
     * 控制商品是否选中
     */
    public void controlGoods(int shopId, boolean state) {
        //根据店铺id选中该店铺下所有商品
        for (CarResponse.OrderDataBean orderDataBean : storeBean) {
            //店铺id等于传递过来的店铺id  则选中该店铺下所有商品
            if (orderDataBean.getShopId() == shopId) {
                for (CarResponse.OrderDataBean.CartlistBean cartlistBean : orderDataBean.getCartlist()) {
                    cartlistBean.setChecked(state);
                    //刷新
                    notifyDataSetChanged();
                }
            }
        }
    }
}
