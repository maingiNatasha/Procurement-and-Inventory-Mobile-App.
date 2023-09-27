package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetails extends AppCompatActivity {
    TextView textViewDetailSupplier, textViewDetailItemName, textViewDetailItemPrice, textViewDetailItemDescription;
    String supplierName, itemName, itemDescription;
    int itemPrice;
    Button buttonGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        textViewDetailSupplier = findViewById(R.id.textView_supplier_name);
        textViewDetailItemName = findViewById(R.id.textView_item_name);
        textViewDetailItemPrice = findViewById(R.id.textView_item_price);
        textViewDetailItemDescription = findViewById(R.id.textView_item_description);
        buttonGenerate = findViewById(R.id.buttonGenerateOrder);

        // Retrieve the item details from the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            supplierName = extras.getString("Supplier Name");
            itemName = extras.getString("Item Name");
            itemPrice = extras.getInt("Item Price");
            itemDescription = extras.getString("Item Description");
        }

        // Set the item details to the corresponding TextViews
        textViewDetailSupplier.setText(supplierName);
        textViewDetailItemName.setText(itemName);
        textViewDetailItemPrice.setText(String.valueOf(itemPrice));
        textViewDetailItemDescription.setText(itemDescription);

        buttonGenerate.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GenerateOrder.class);

            intent.putExtra("supplierName", textViewDetailSupplier.getText().toString());
            intent.putExtra("itemName", textViewDetailItemName.getText().toString());
            intent.putExtra("itemPrice", String.valueOf(itemPrice));

            startActivity(intent);
            finish();
        });
    }
}
