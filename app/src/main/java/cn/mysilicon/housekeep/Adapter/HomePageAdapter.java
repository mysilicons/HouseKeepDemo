package cn.mysilicon.housekeep.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.DetailsActivity;
import cn.mysilicon.housekeep.model.ServiceItemBean;

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "HomePageAdapter";
    private List<ServiceItemBean> mServiceItemBeanList;

    private Activity mActivity;

    public HomePageAdapter(List<ServiceItemBean> mServiceItemBeanList, Activity mActivity) {
        this.mServiceItemBeanList = mServiceItemBeanList;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String url1 = mServiceItemBeanList.get(position).getURL();
        Glide.with(mActivity).load(url1).into(viewHolder.singleimage);
//        Log.d(TAG, "onBindViewHolder: url1=" + url1);
        viewHolder.tv_1.setText(mServiceItemBeanList.get(position).getTitle());
        viewHolder.tv_2.setText(mServiceItemBeanList.get(position).getContent());
        viewHolder.tv_price.setText(mServiceItemBeanList.get(position).getPrice());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DetailsActivity.class);
                intent.putExtra("id", mServiceItemBeanList.get(viewHolder.getAdapterPosition()).getId());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServiceItemBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView singleimage;
        TextView tv_1;
        TextView tv_2;
        TextView tv_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            singleimage = itemView.findViewById(R.id.singleimageView);
            tv_1 = itemView.findViewById(R.id.singletextView);
            tv_2 = itemView.findViewById(R.id.singletextView2);
            tv_price = itemView.findViewById(R.id.singletv_price);
        }
    }
}
