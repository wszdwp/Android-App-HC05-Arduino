package com.example.danyal.bluetoothhc05;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceRecyclerViewAdapter
        extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener {

    private List<BluetoothDevice> devices;
    private CustomItemClickListener customItemClickListener;

    @Override
    public void onClick(View view) {
        final int position = (int) view.getTag();
        customItemClickListener.onItemClick(view, position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView addressTextView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            nameTextView = view.findViewById(R.id.name);
            addressTextView = view.findViewById(R.id.address);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getAddressTextView() {
            return addressTextView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param devices List<BluetoothDevice> containing the data to populate views to be used
     * by RecyclerView.
     */
    public DeviceRecyclerViewAdapter(List<BluetoothDevice> devices, CustomItemClickListener customItemClickListener) {
        this.devices = devices;
        this.customItemClickListener = customItemClickListener;
    }

    public void setDevices(List<BluetoothDevice> devices) {
        this.devices = devices;
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.device_row_layout, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        BluetoothDevice device = devices.get(position);
        viewHolder.getNameTextView().setText(device.getName());
        viewHolder.getAddressTextView().setText(device.getAddress());
        viewHolder.itemView.setTag(position);
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }
}