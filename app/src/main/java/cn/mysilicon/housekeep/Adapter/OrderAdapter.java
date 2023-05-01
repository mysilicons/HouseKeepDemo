package cn.mysilicon.housekeep.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Order;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        String URL = mOrderList.get(position).getImage();
        Log.d(TAG, "onBindViewHolder: " + URL);
        Glide.with(mContext).load(URL).into(((ViewHolder) holder).img);
        ((ViewHolder) holder).order_time.setText(order.getOrder_time());
        ((ViewHolder) holder).cur_status.setText(order.getCur_status());
        ((ViewHolder) holder).title.setText(order.getName());
        ((ViewHolder) holder).price.setText(order.getPrice());
        ((ViewHolder) holder).content.setText(order.getContent());
        if (order.getCur_status().equals("进行中")) {
            ((ViewHolder) holder).finish.setText("完成");
            ((ViewHolder) holder).finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(order.getId());
                    fresh(position);
                    ((ViewHolder) holder).finish.setText("删除");
                }
            });
        }
        if (order.getCur_status().equals("已完成")) {
            ((ViewHolder) holder).finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(order.getId());
                    remove(position);
                }
            });
            ((ViewHolder) holder).finish.setText("删除");
        }
    }

    private void fresh(int position) {
        mOrderList.get(position).setCur_status("已完成");
        notifyItemChanged(position);
    }

    private void remove(int position) {
        mOrderList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mOrderList.size());
    }


    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, "订单已完成", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "订单已删除", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void delete(int service_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/order/delete?id=" + service_id)
                        .post(RequestBody.create("", null))
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(TAG, "run: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }


    private void finish(int service_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/order/finish?id=" + service_id)
                        .post(RequestBody.create("", null))
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(TAG, "run: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    handler.sendEmptyMessage(0);
                }

            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
