package com.example.prototype;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewProcessHolder> {
    private static final String delete_url = "http://192.168.220.66/PrototypeApi/deleteInventoryItem.php";
    Context context;
    private final ArrayList<ModelData> item;

    public RvAdapter(Context context, ArrayList<ModelData> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapter.ViewProcessHolder holder, int position) {
        final ModelData data = item.get(position);

        holder.tv_item_id.setText(String.format("Item ID: %s", data.getItemId()));
        holder.tv_item_name.setText(String.format("Item Name: %s", data.getItemName()));
        holder.tv_item_type.setText(String.format("Item Type: %s", data.getItemType()));

        String quantityWithUnit = String.format("%s %s", data.getItemQuantity(), data.getItemUnit());
        holder.tv_item_quantity.setText(String.format("Item Quantity: %s", quantityWithUnit));

        String minQuantityWithUnit = String.format("%s %s", data.getItemMinQuantity(), data.getItemUnit());
        holder.tv_item_min_quantity.setText(String.format("Item Quantity: %s", minQuantityWithUnit));

        if(data.getItemQuantity() <= data.getItemMinQuantity()) {
            holder.showWarning();
        } else {
            holder.hideWarning();
        }

        ImageButton buttonEdit = holder.itemView.findViewById(R.id.btn_edit);
        ImageButton buttonDelete = holder.itemView.findViewById(R.id.btn_delete);

        //Onclick listener for item view
        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddInventoryItem.class);
            intent.putExtra("itemId", data.getItemId());
            intent.putExtra("itemName", data.getItemName());
            intent.putExtra("itemType", data.getItemType());
            intent.putExtra("itemQuantity", data.getItemQuantity());
            intent.putExtra("itemMinQuantity", data.getItemMinQuantity());
            intent.putExtra("itemUnit", data.getItemUnit());

            v.getContext().startActivity(intent);
        });

        buttonDelete.setOnClickListener(v -> {
            int itemId = Integer.parseInt(data.getItemId());

            deleteInventoryItem(itemId);

        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewProcessHolder extends RecyclerView.ViewHolder {
        TextView tv_item_id, tv_item_name, tv_item_type, tv_item_quantity, tv_item_min_quantity, tv_warning_text;

        public ViewProcessHolder(View itemView) {
            super(itemView);

            tv_item_id = itemView.findViewById(R.id.tv_order_id);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_type = itemView.findViewById(R.id.tv_order_quantity);
            tv_item_quantity = itemView.findViewById(R.id.tv_order_price);
            tv_item_min_quantity = itemView.findViewById(R.id.tv_payment_method);
            tv_warning_text = itemView.findViewById(R.id.warning_message);

        }

        public void showWarning() {
            tv_warning_text.setVisibility(View.VISIBLE);
        }

        public void hideWarning() {
            tv_warning_text.setVisibility(View.GONE);
        }
    }

    private void deleteInventoryItem(int itemId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                    Log.i("Response", response);

                    // Remove the item from the ArrayList
                    int position = findItemPosition(itemId);
                    if (position != -1) {
                        item.remove(position);
                        notifyItemRemoved(position);
                    }

                },
                error -> {
                    // Handle error
                    Toast.makeText(context, "Error canceling order: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Error", error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("itemId", String.valueOf(itemId));
                return params;
            }
        };

        queue.add(request);
    }

    private int findItemPosition(int itemId) {
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i).getItemId().equals(String.valueOf(itemId))) {
                return i;
            }
        }
        return -1;
    }
}
