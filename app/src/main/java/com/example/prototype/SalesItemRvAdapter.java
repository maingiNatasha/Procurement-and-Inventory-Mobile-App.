package com.example.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SalesItemRvAdapter extends RecyclerView.Adapter<SalesItemRvAdapter.ViewProcessHolder>{
    Context context;
    private final ArrayList<SupplierData> supplierData;

    public SalesItemRvAdapter(ArrayList<SupplierData> supplierData, Context context) {
        this.supplierData = supplierData;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesItemRvAdapter.ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_item, parent, false);
        return new SalesItemRvAdapter.ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesItemRvAdapter.ViewProcessHolder holder, int position) {
        final SupplierData data = supplierData.get(position);

        holder.textViewSupplierName.setText(String.format("Supplier Name: %s", data.getSupplierName()));
        holder.textViewSupplierItemName.setText(String.format("Item Name: %s", data.getSupplierItemName()));
        holder.textViewSupplierItemPrice.setText(String.format("Item Price: %s", (data.getSupplierItemPrice())));
        holder.textViewSupplierItemDescription.setText(String.format("Item Description: %s", data.getSupplierItemDescription()));
    }

    @Override
    public int getItemCount() {
        return supplierData.size();
    }

    public static class ViewProcessHolder extends RecyclerView.ViewHolder {
        TextView textViewSupplierName, textViewSupplierItemName, textViewSupplierItemPrice, textViewSupplierItemDescription;

        public ViewProcessHolder(View itemView) {
            super(itemView);

            textViewSupplierName = itemView.findViewById(R.id.tv_supplier_name);
            textViewSupplierItemName = itemView.findViewById(R.id.tv_supplier_item_name);
            textViewSupplierItemPrice = itemView.findViewById(R.id.tv_supplier_item_price);
            textViewSupplierItemDescription = itemView.findViewById(R.id.tv_supplier_item_description);
        }
    }
}
