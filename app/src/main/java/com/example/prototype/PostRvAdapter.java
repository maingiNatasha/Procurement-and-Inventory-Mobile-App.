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

public class PostRvAdapter extends RecyclerView.Adapter<PostRvAdapter.ViewProcessHolder> {
    private static final String delete_url = "http://192.168.220.66/PrototypeApi/deleteSupplierItem.php";
    Context context;
    private final ArrayList<SupplierData> supplierData;

    public PostRvAdapter(Context context, ArrayList<SupplierData> supplierData) {
        this.context = context;
        this.supplierData = supplierData;
    }

    @NonNull
    @Override
    public PostRvAdapter.ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRvAdapter.ViewProcessHolder holder, int position) {
        final SupplierData data = supplierData.get(position);

        holder.textViewItemId.setText(String.format("Item Id: %s", data.getItemId()));
        holder.textViewSupplierName.setText(String.format("Supplier Name: %s", data.getSupplierName()));
        holder.textViewSupplierItemName.setText(String.format("Item Name: %s", data.getSupplierItemName()));
        holder.textViewSupplierItemPrice.setText(String.format("Item Price: %s", (data.getSupplierItemPrice())));
        holder.textViewSupplierItemDescription.setText(String.format("Item Description: %s", data.getSupplierItemDescription()));

        ImageButton buttonEdit = holder.itemView.findViewById(R.id.btn_edit);
        ImageButton buttonDelete = holder.itemView.findViewById(R.id.btn_delete);

        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostSalesItem.class);
            intent.putExtra("Item Id", data.getItemId());
            intent.putExtra("Supplier Name", data.getSupplierName());
            intent.putExtra("Item Name", data.getSupplierItemName());
            intent.putExtra("Item Price", data.getSupplierItemPrice());
            intent.putExtra("Item Description", data.getSupplierItemDescription());

            v.getContext().startActivity(intent);
        });

        buttonDelete.setOnClickListener(v -> {
            int itemId = Integer.parseInt(data.getItemId());

            deleteItem(itemId);

        });


    }

    @Override
    public int getItemCount() {
        return supplierData.size();
    }

    public static class ViewProcessHolder extends RecyclerView.ViewHolder {
        TextView textViewSupplierName, textViewSupplierItemName, textViewSupplierItemPrice, textViewSupplierItemDescription, textViewItemId;

        public ViewProcessHolder(View itemView) {
            super(itemView);

            textViewItemId = itemView.findViewById(R.id.tv_item_id);
            textViewSupplierName = itemView.findViewById(R.id.tv_supplier_name);
            textViewSupplierItemName = itemView.findViewById(R.id.tv_supplier_item_name);
            textViewSupplierItemPrice = itemView.findViewById(R.id.tv_supplier_item_price);
            textViewSupplierItemDescription = itemView.findViewById(R.id.tv_supplier_item_description);
        }
    }

    private void deleteItem(int itemId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(context, "Posted Item Deleted", Toast.LENGTH_SHORT).show();
                    Log.i("Response", response);

                    // Remove the item from the ArrayList
                    int position = findItemPosition(itemId);
                    if (position != -1) {
                        supplierData.remove(position);
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
                params.put("supplierItemId", String.valueOf(itemId));
                return params;
            }
        };

        queue.add(request);
    }

    private int findItemPosition(int itemId) {
        for (int i = 0; i < supplierData.size(); i++) {
            if (supplierData.get(i).getItemId().equals(String.valueOf(itemId))) {
                return i;
            }
        }
        return -1;
    }
}
