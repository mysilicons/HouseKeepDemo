package cn.mysilicon.housekeep.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.PreOrderActivity;
import cn.mysilicon.housekeep.model.Address;

public class PreOrderAddressAdapter extends RecyclerView.Adapter<PreOrderAddressAdapter.ViewHolder> {
    private List<Address> addressesList;

    public PreOrderAddressAdapter(List<Address> addressesList) {
        this.addressesList = addressesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_order_adress_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = addressesList.get(position);
        holder.address_title_textview.setText(address.getAddress());
        holder.address_name_textview.setText(address.getUname());
        holder.address_phone_textview.setText(address.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreOrderActivity.tvAddress.setText(address.getAddress());
                PreOrderActivity.address_id = address.getId();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address_title_textview = itemView.findViewById(R.id.address_title_textview);
            address_name_textview = itemView.findViewById(R.id.address_name_textview);
            address_phone_textview = itemView.findViewById(R.id.address_phone_textview);
        }
    }
}
