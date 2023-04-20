package cn.mysilicon.housekeep.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.AddressEditActivity;
import cn.mysilicon.housekeep.activities.PersonActivity;
import cn.mysilicon.housekeep.model.AddressMatch;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private static final String TAG = "AddressAdapter";
    private List<AddressMatch> addressesList;
    private Activity activity;

    public AddressAdapter() {
    }

    public AddressAdapter(List<AddressMatch> addressesList, PersonActivity personActivity) {
        this.addressesList = addressesList;
        this.activity = personActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adress_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        AddressMatch address = addressesList.get(position);
        holder.address_title_textview.setText(address.getAddress());
        holder.address_name_textview.setText(address.getUname());
        holder.address_phone_textview.setText(address.getPhone());
        holder.address_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddressEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", address.getId());
                bundle.putString("name", address.getUname());
                bundle.putString("phone", address.getPhone());
                bundle.putString("address", address.getAddress());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        holder.address_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddress(address.getId());
                removeAddress(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView address_title_textview;
        TextView address_name_textview;
        TextView address_phone_textview;
        Button address_edit_button;
        Button address_delete_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address_title_textview = itemView.findViewById(R.id.address_title_textview);
            address_name_textview = itemView.findViewById(R.id.address_name_textview);
            address_phone_textview = itemView.findViewById(R.id.address_phone_textview);
            address_edit_button = itemView.findViewById(R.id.address_edit_button);
            address_delete_button = itemView.findViewById(R.id.address_delete_button);
        }
    }

    private void deleteAddress(int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "http://mysilicon.cn/address/delete?id=" + id;
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(RequestBody.create("", null))
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }
        }).start();
    }

    private void removeAddress(int position) {
        addressesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addressesList.size());
    }

    //Handler
    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(activity, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}