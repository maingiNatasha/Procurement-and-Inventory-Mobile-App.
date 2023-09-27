package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {
    TextView textViewId, textViewItem, textViewQuantity, textViewUnit, textViewPrice, textViewPaymentMethod, textViewSupplier, textViewCustomer, textViewStatus;
    String orderId, itemName, itemUnit, paymentMethod, supplierName, customerName, orderStatus;
    int itemQuantity, totalPrice;
    private static final String update_status_url = "http://192.168.220.66/PrototypeApi/updateStatus.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Button buttonUpdateStatus = findViewById(R.id.buttonUpdateStatus);
        Button buttonBack = findViewById(R.id.buttonBack);

        textViewId = findViewById(R.id.textView_Order_Id);
        textViewItem = findViewById(R.id.textView_Order_Item_Name);
        textViewQuantity = findViewById(R.id.textView_Order_Quantity);
        textViewUnit = findViewById(R.id.textView_Order_Unit);
        textViewPrice = findViewById(R.id.textView_Order_Price);
        textViewPaymentMethod = findViewById(R.id.textView_Order_Payment_Method);
        textViewSupplier = findViewById(R.id.textView_Order_Supplier);
        textViewCustomer = findViewById(R.id.textView_Order_Customer);
        textViewStatus = findViewById(R.id.textView_Order_Status);

        // Retrieve the item details from the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderId = extras.getString("Order Id");
            itemName = extras.getString("Item Name");
            itemQuantity = extras.getInt("Ordered Quantity");
            itemUnit = extras.getString("Unit");
            totalPrice = extras.getInt("Total Price");
            paymentMethod = extras.getString("Payment Method");
            supplierName = extras.getString("Supplier");
            customerName = extras.getString("Customer");
            orderStatus = extras.getString("Order Status");
        }

        // Set the item details to the corresponding TextViews
        textViewId.setText(orderId);
        textViewItem.setText(itemName);
        textViewQuantity.setText(String.valueOf(itemQuantity));
        textViewUnit.setText(itemUnit);
        textViewPrice.setText(String.valueOf(totalPrice));
        textViewPaymentMethod.setText(paymentMethod);
        textViewSupplier.setText(supplierName);
        textViewCustomer.setText(customerName);
        textViewStatus.setText(orderStatus);

        // Get the reference to the Spinner
        Spinner spinnerOrderStatus = findViewById(R.id.spinner_Order_Status);

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.order_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(adapter);

        // Set the initial selection of the Spinner based on the orderStatus
        int position = adapter.getPosition(orderStatus);
        spinnerOrderStatus.setSelection(position);

        buttonUpdateStatus.setOnClickListener(v -> {
            // Get the selected status from the spinner
            String selectedStatus = spinnerOrderStatus.getSelectedItem().toString();

            // Update the status in the database using Volley
            updateOrderStatus(selectedStatus);
        });

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MyOrders.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateOrderStatus(String selectedStatus) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, update_status_url,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(getApplicationContext(), "Status updated successfully", Toast.LENGTH_SHORT).show();
                    Log.i("Response", response);
                    // Update the current status TextView
                    textViewStatus.setText(selectedStatus);
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Error updating status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Error", error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("orderId", orderId);
                params.put("orderStatus", selectedStatus);
                return params;
            }
        };
        queue.add(request);
    }
}