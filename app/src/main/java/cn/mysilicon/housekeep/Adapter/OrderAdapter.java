package cn.mysilicon.housekeep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Order;

public class OrderAdapter extends RecyclerView.Adapter {
    private static final String TAG = "OrderAdapter";
    private List<Order> mOrderList;
    private Context mContext;

    public OrderAdapter(List<Order> orderList, Context mContext) {
        this.mOrderList = orderList;
        this.mContext = mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_time;
        TextView cur_status;
        ImageView img;
        TextView title;
        TextView price;
        TextView content;
        TextView finish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_time = itemView.findViewById(R.id.order_time);
            cur_status = itemView.findViewById(R.id.cur_status);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            content = itemView.findViewById(R.id.content);
            finish = itemView.findViewById(R.id.finish);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order order = mOrderList.get(position);

        String URL = mOrderList.get(position).getImage_url();
        Glide.with(mContext).load(URL).into(((ViewHolder) holder).img);
        ((ViewHolder) holder).order_time.setText(order.getOrder_time());
        ((ViewHolder) holder).cur_status.setText(order.getCur_status());
        ((ViewHolder) holder).title.setText(order.getTitle());
        ((ViewHolder) holder).price.setText(order.getPrice());
        ((ViewHolder) holder).content.setText(order.getContent());
        ((ViewHolder) holder).finish.setText("完成");
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mOrderList.get(position);

        holder.order_time.setText(order.getOrder_time());
        holder.cur_status.setText(order.getCur_status());
        holder.title.setText(order.getTitle());
        holder.price.setText(order.getPrice());
        holder.content.setText(order.getContent());
        holder.finish.setText("完成");
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
