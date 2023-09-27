package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GenerateOrder extends AppCompatActivity {
    TextInputEditText textInputEditTextOrderItemName, textInputEditTextOrderItemPrice, textInputEditTextOrderItemQuantity, textInputEditTextOrderItemUnit, textInputEditTextTotalPrice, textInputEditTextOrderSupplier;
    Button buttonSubmitOrder, buttonBack;
    RadioGroup radioGroup;
    String orderItemName, orderItemUnit, orderSupplier, paymentMethod;
    int orderItemPrice, orderItemQuantity,  orderTotalPrice, itemPriceNum;
    private static final String submit_order_url = "http://192.168.220.66/PrototypeApi/addPurchaseOrder.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_order);

        // Retrieve the preloaded values from the intent extras
        String supplierName = getIntent().getStringExtra("supplierName");
        String itemName = getIntent().getStringExtra("itemName");
        String itemPrice = getIntent().getStringExtra("itemPrice");
        itemPriceNum = Integer.parseInt(itemPrice);

        textInputEditTextOrderItemName = findViewById(R.id.etOrderItemName);
        textInputEditTextOrderItemPrice = findViewById(R.id.etOrderItemPrice);
        textInputEditTextOrderItemQuantity = findViewById(R.id.etOrderItemQuantity);
        textInputEditTextOrderItemUnit = findViewById(R.id.etOrderItemUnit);
        textInputEditTextTotalPrice = findViewById(R.id.etOrderTotalPrice);
        textInputEditTextOrderSupplier = findViewById(R.id.etOrderSupplierName);
        buttonSubmitOrder = findViewById(R.id.buttonSubmitOrder);
        buttonBack = findViewById(R.id.buttonBack);
        radioGroup = findViewById(R.id.radioGroup);

        // Set the preloaded values to the TextInputEditText fields
        textInputEditTextOrderItemName.setText(itemName);
        textInputEditTextOrderItemPrice.setText(itemPrice);
        textInputEditTextOrderSupplier.setText(supplierName);

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
            startActivity(intent);
            finish();
        });

        textInputEditTextOrderItemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateOrderTotalPrice();
            }
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton1 = findViewById(R.id.radioButton1);
            RadioButton radioButton2 = findViewById(R.id.radioButton2);

            if (checkedId == radioButton1.getId()) {
                paymentMethod = radioButton1.getText().toString();
            }
            else if (checkedId == radioButton2.getId()) {
                paymentMethod = radioButton2.getText().toString();
            }
        });
        
        buttonSubmitOrder.setOnClickListener(v -> {
            orderItemName = String.valueOf(textInputEditTextOrderItemName.getText());
            orderItemPrice = Integer.parseInt(String.valueOf(textInputEditTextOrderItemPrice.getText()));
            orderItemQuantity = Integer.parseInt(String.valueOf(textInputEditTextOrderItemQuantity.getText()));
            orderItemUnit = String.valueOf(textInputEditTextOrderItemUnit.getText());
            orderTotalPrice = calculateOrderTotalPrice();
            orderSupplier = String.valueOf(textInputEditTextOrderSupplier.getText());

            submitOrder();

        });
    }

    private int calculateOrderTotalPrice() {
        String quantityString = Objects.requireNonNull(textInputEditTextOrderItemQuantity.getText()).toString();
        if (!quantityString.isEmpty()) {
            int quantity = Integer.parseInt(quantityString);
            int totalPrice = itemPriceNum * quantity;
            textInputEditTextTotalPrice.setText(String.valueOf(totalPrice));
            return totalPrice;
        }
        return 0;
    }


    private void submitOrder() {
        // Obtain the farmer info from the SessionManager
        int farmerId = SessionManager.getInstance(getApplicationContext()).getFarmerId();
        String fullname = SessionManager.getInstance(getApplicationContext()).getFullname();

        //Create new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, submit_order_url, response -> {
            // Handle the response from the server
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            Log.i("Response", response);
            
        },
                //Error listener
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Error", error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("orderItemName", orderItemName);
                params.put("orderItemPrice", String.valueOf(orderItemPrice));
                params.put("orderItemQuantity", String.valueOf(orderItemQuantity));
                params.put("orderItemUnit", orderItemUnit);
                params.put("orderTotalPrice", String.valueOf(orderTotalPrice));
                params.put("orderSupplierName", orderSupplier);
                params.put("paymentMethod", paymentMethod);
                params.put("orderCustomerName", fullname);
                params.put("farmerId", String.valueOf(farmerId));

                return params;
            }
        };

        //Add StringRequest to Request Queue to execute network request
        queue.add(request);
    }

}