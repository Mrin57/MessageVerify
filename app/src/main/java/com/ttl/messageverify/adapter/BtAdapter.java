package com.ttl.messageverify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttl.messageverify.R;
import com.ttl.messageverify.model.BtDevice;

import java.util.List;

public class BtAdapter extends RecyclerView.Adapter<BtAdapter.BtViewHolder> {


    private List<BtDevice> devices;

    public BtAdapter(List<BtDevice> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public BtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_devices, parent, false);
        return new BtViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BtViewHolder holder, int position) {
        BtDevice device = devices.get(position);
        holder.tvBtName.setText(device.getDevieName());
        holder.tvStrength.setText("" + device.getStrength());
    }

    @Override
    public int getItemCount() {
        return devices != null ? devices.size() : 0;
    }

    class BtViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvBtName, tvStrength;

        public BtViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBtName = itemView.findViewById(R.id.tv_device_name);
            tvStrength = itemView.findViewById(R.id.tv_strength);
        }
    }
}
