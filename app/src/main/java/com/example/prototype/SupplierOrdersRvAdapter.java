package com.example.prototype;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SupplierOrdersRvAdapter extends RecyclerView.Adapter<SupplierOrdersRvAdapter.ViewProcessHolder>{
    Context context;
    private final ArrayList<PurchaseData> item;

    public SupplierOrdersRvAdapter(ArrayList<PurchaseData> item, Context context) {
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public SupplierOrdersRvAdapter.ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new SupplierOrdersRvAdapter.ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierOrdersRvAdapter.ViewProcessHolder holder, int position) {
        final PurchaseData data = item.get(position);

        holder.tv_o_order_id.setText(String.format("Order Id: %s", data.getpOrderId()));
        holder.tv_o_item.setText(String.format("Item Name: %s", data.getpItemName()));
        holder.tv_o_quantity.setText(String.format("Ordered Quantity: %s", data.getpItemQuantity()));
        holder.tv_o_item_unit.setText(String.format("%s", data.getpItemUnit()));
        holder.tv_o_total.setText(String.format("Total Price %s", data.getpItemTotal()));
        holder.tv_payment_method.setText(String.format("Payment Method: %s", data.getPaymentMethod()));
        holder.tv_o_supplier.setText(String.format("Supplier: %s", data.getpSupplier()));
        holder.tv_o_customer.setText(String.format("Customer: %s", data.getpCustomer()));
        holder.tv_o_status.setText(String.format("Order Status: %s", data.getpStatus()));

        //Onclick listener for item view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OrderDetails.class);
            intent.putExtra("Order Id", data.getpOrderId());
            intent.putExtra("Item Name", data.getpItemName());
            intent.putExtra("Ordered Quantity", data.getpItemQuantity());
            intent.putExtra("Unit", data.getpItemUnit());
            intent.putExtra("Total Price", data.getpItemTotal());
            intent.putExtra("Payment Method", data.getPaymentMethod());
            intent.putExtra("Supplier", data.getpSupplier());
            intent.putExtra("Customer", data.getpCustomer());
            intent.putExtra("Order Status", data.getpStatus());

            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewProcessHolder extends RecyclerView.ViewHolder {
        TextView tv_o_order_id, tv_o_item, tv_o_quantity, tv_o_item_unit, tv_o_total, tv_payment_method, tv_o_supplier, tv_o_customer, tv_o_status;

        public ViewProcessHolder(View itemView) {
            super(itemView);

            tv_o_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_o_item = itemView.findViewById(R.id.tv_item_name);
            tv_o_quantity = itemView.findViewById(R.id.tv_order_quantity);
            tv_o_item_unit = itemView.findViewById(R.id.tv_order_unit);
            tv_o_total = itemView.findViewById(R.id.tv_order_price);
            tv_payment_method = itemView.findViewById(R.id.tv_payment_method);
            tv_o_supplier = itemView.findViewById(R.id.tv_order_supplier);
            tv_o_customer = itemView.findViewById(R.id.tv_order_customer);
            tv_o_status = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
