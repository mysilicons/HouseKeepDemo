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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.AddressEditActivity;
import cn.mysilicon.housekeep.activities.PersonActivity;
import cn.mysilicon.housekeep.model.Address;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private static final String TAG = "AddressAdapter";
    private List<Address> addressesList;
    private Activity activity;

    public AddressAdapter() {
    }

    public AddressAdapter(List<Address> addressesList, PersonActivity personActivity) {
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
        Address address = addressesList.get(position);
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

        holder.address_delete_button.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle("提示")
                    .setMessage("确定要删除吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        deleteAddress(address.getId());
                        removeAddress(position);
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                    })
                    .create();
            alertDialog.show();
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
                String url = "http://你的服务器地址/address/delete?id=" + id;
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(RequestBody.create("", null))
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    Looper.prepare();
                    Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    handler.sendEmptyMessage(1);
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
                default:
                    break;
            }
        }
    };
}
