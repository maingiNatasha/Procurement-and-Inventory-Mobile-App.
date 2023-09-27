package com.example.prototype;

import android.content.Context;
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

public class PurchasesRvAdapter extends RecyclerView.Adapter<PurchasesRvAdapter.ViewProcessHolder> {
    private static final String cancel_order_url = "http://192.168.220.66/PrototypeApi/cancelPurchase.php";
    Context context;
    private final ArrayList<PurchaseData> item;

    public PurchasesRvAdapter(Context context, ArrayList<PurchaseData> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public PurchasesRvAdapter.ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
        return new PurchasesRvAdapter.ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasesRvAdapter.ViewProcessHolder holder, int position) {
        final PurchaseData data = item.get(position);

        holder.tv_p_order_id.setText(String.format("Order Id: %s", data.getpOrderId()));
        holder.tv_p_item.setText(String.format("Item Name: %s", data.getpItemName()));
        holder.tv_p_quantity.setText(String.format("Ordered Quantity: %s", data.getpItemQuantity()));
        holder.tv_p_item_unit.setText(String.format("%s", data.getpItemUnit()));
        holder.tv_p_total.setText(String.format("Total Price %s", data.getpItemTotal()));
        holder.tv_payment_method.setText(String.format("Payment Method: %s", data.getPaymentMethod()));
        holder.tv_p_supplier.setText(String.format("Supplier: %s", data.getpSupplier()));

        ImageButton buttonDelete = holder.itemView.findViewById(R.id.btn_delete);

        buttonDelete.setOnClickListener(v -> {
            // Get the order ID of the clicked item
            int orderId = Integer.parseInt(data.getpOrderId());

            cancelOrder(orderId);
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewProcessHolder extends RecyclerView.ViewHolder {
        TextView tv_p_order_id, tv_p_item, tv_p_quantity, tv_p_item_unit, tv_p_total, tv_payment_method, tv_p_supplier;

        public ViewProcessHolder(View itemView) {
            super(itemView);

            tv_p_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_p_item = itemView.findViewById(R.id.tv_item_name);
            tv_p_quantity = itemView.findViewById(R.id.tv_order_quantity);
            tv_p_item_unit = itemView.findViewById(R.id.tv_order_unit);
            tv_p_total = itemView.findViewById(R.id.tv_order_price);
            tv_payment_method = itemView.findViewById(R.id.tv_payment_method);
            tv_p_supplier = itemView.findViewById(R.id.tv_p_supplier);
        }
    }

    private void cancelOrder(int orderId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, cancel_order_url,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(context, "Order canceled", Toast.LENGTH_SHORT).show();
                    Log.i("Response", response);

                    // Remove the item from the ArrayList
                    int position = findItemPosition(orderId);
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
                params.put("orderId", String.valueOf(orderId));
                return params;
            }
        };

        queue.add(request);
    }

    private int findItemPosition(int orderId) {
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i).getpOrderId().equals(String.valueOf(orderId))) {
                return i;
            }
        }
        return -1;
    }


}
