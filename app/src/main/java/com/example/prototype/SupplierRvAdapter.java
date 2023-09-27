package com.example.prototype;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SupplierRvAdapter extends RecyclerView.Adapter<SupplierRvAdapter.ViewProcessHolder> {

    Context context;
    private final ArrayList<SupplierData> supplierData;

    public SupplierRvAdapter(Context context, ArrayList<SupplierData> supplierData) {
        this.supplierData = supplierData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_item, parent, false);
        return new ViewProcessHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SupplierRvAdapter.ViewProcessHolder holder, int position) {
        final SupplierData data = supplierData.get(position);

        holder.textViewSupplierName.setText(String.format("Supplier Name: %s", data.getSupplierName()));
        holder.textViewSupplierItemName.setText(String.format("Item Name: %s", data.getSupplierItemName()));
        holder.textViewSupplierItemPrice.setText(String.format("Item Price: %s", (data.getSupplierItemPrice())));
        holder.textViewSupplierItemDescription.setText(String.format("Item Description: %s", data.getSupplierItemDescription()));

        //Onclick listener for item view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ItemDetails.class);
            intent.putExtra("Supplier Name", data.getSupplierName());
            intent.putExtra("Item Name", data.getSupplierItemName());
            intent.putExtra("Item Price", data.getSupplierItemPrice());
            intent.putExtra("Item Description", data.getSupplierItemDescription());

            v.getContext().startActivity(intent);
        });

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

