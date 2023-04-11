package cn.mysilicon.housekeep.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.DetailsActivity;
import cn.mysilicon.housekeep.activities.ServicesActivity;
import cn.mysilicon.housekeep.model.ServiceItemBean;

/**
 * @author lzsheng
 */
public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SearchResultAdapter";
    private List<ServiceItemBean> mServiceItemBeanList;
    private Activity mActivity;
    private int listColumn = ServicesActivity.LIST_COLUMN_SINGLE;
    private Context mContext;

    public SearchResultAdapter(List<ServiceItemBean> mServiceItemBeanList, Activity mActivity, Context mContext) {
        this.mServiceItemBeanList = mServiceItemBeanList;
        this.mActivity = mActivity;
        this.mContext = mContext;
    }

    public void setListColumn(int listColumn) {
        this.listColumn = listColumn;
    }

    @Override
    public int getItemViewType(int position) {
        return listColumn;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case ServicesActivity.LIST_COLUMN_SINGLE:
                ViewHolderItemSingle viewHolderItemSingle= new ViewHolderItemSingle(mActivity.getLayoutInflater().inflate(R.layout.rv_item_single, parent, false));
                viewHolderItemSingle.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("id", mServiceItemBeanList.get(viewHolderItemSingle.getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                    }
                });
                return viewHolderItemSingle;
            case ServicesActivity.LIST_COLUMN_DOUBLE:
                ViewHolderItemDouble viewHolderItemDouble = new ViewHolderItemDouble(mActivity.getLayoutInflater().inflate(R.layout.rv_item_double, parent, false));
                viewHolderItemDouble.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("id", mServiceItemBeanList.get(viewHolderItemDouble.getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                    }
                });
                return viewHolderItemDouble;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ServicesActivity.LIST_COLUMN_SINGLE:
                ViewHolderItemSingle holderItemSingle = (ViewHolderItemSingle) holder;
                String url1 = mServiceItemBeanList.get(position).getURL();
                Glide.with(mActivity).load(url1).into(holderItemSingle.singleimage);
                Log.d(TAG, "onBindViewHolder: url1=" + url1);
                holderItemSingle.tv_1.setText(mServiceItemBeanList.get(position).getTitle());
                holderItemSingle.tv_2.setText(mServiceItemBeanList.get(position).getContent());
                holderItemSingle.tv_price.setText(mServiceItemBeanList.get(position).getPrice());
                break;
            case ServicesActivity.LIST_COLUMN_DOUBLE:
                ViewHolderItemDouble holderItemDouble = (ViewHolderItemDouble) holder;
                String url2 = mServiceItemBeanList.get(position).getURL();
                Glide.with(mActivity).load(url2).into(holderItemDouble.doubleimage);
                Log.d(TAG, "onBindViewHolder: url2=" + url2);
                holderItemDouble.tv_1.setText(mServiceItemBeanList.get(position).getTitle());
                holderItemDouble.tv_2.setText(mServiceItemBeanList.get(position).getContent());
                holderItemDouble.tv_price.setText(mServiceItemBeanList.get(position).getPrice());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mServiceItemBeanList.size();
    }

    static class ViewHolderItemSingle extends RecyclerView.ViewHolder {

        ImageView singleimage;
        TextView tv_1;
        TextView tv_2;
        TextView tv_price;

        public ViewHolderItemSingle(@NonNull View itemView) {
            super(itemView);
            singleimage = itemView.findViewById(R.id.singleimageView);
            tv_1 = itemView.findViewById(R.id.singletextView);
            tv_2 = itemView.findViewById(R.id.singletextView2);
            tv_price = itemView.findViewById(R.id.singletv_price);
        }
    }

    static class ViewHolderItemDouble extends RecyclerView.ViewHolder {

        ImageView doubleimage;
        TextView tv_1;
        TextView tv_2;
        TextView tv_price;

        public ViewHolderItemDouble(@NonNull View itemView) {
            super(itemView);
            doubleimage = itemView.findViewById(R.id.doubleimageView);
            tv_1 = itemView.findViewById(R.id.doubletextView);
            tv_2 = itemView.findViewById(R.id.doubletextView2);
            tv_price = itemView.findViewById(R.id.doubletv_price);
        }
    }

}
