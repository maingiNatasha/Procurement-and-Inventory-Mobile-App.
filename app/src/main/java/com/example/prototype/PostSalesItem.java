package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class PostSalesItem extends AppCompatActivity {
    TextInputEditText textInputEditTextSupplierName, textInputEditTextItemName, textInputEditTextItemPrice, textInputEditTextItemDescription;
    Button  buttonPost, buttonBack;
    String SupplierName, ItemName, ItemDescription, itemId;
    int ItemPrice;
    private static final String post_url = "http://192.168.220.66/PrototypeApi/addSupplierItem.php";
    private static final String update_post_url = "http://192.168.220.66/PrototypeApi/updateSupplierItem.php";
    private boolean isEditingMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sales_item);

        textInputEditTextSupplierName = findViewById(R.id.etSupplierName);
        textInputEditTextItemName = findViewById(R.id.etSalesItemName);
        textInputEditTextItemPrice = findViewById(R.id.etSalesItemPrice);
        textInputEditTextItemDescription = findViewById(R.id.etSalesItemDescription);
        buttonPost = findViewById(R.id.buttonPostItem);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> back());

        // Check if the activity was started in editing mode
        Intent intent = getIntent();
        if (intent.hasExtra("Item Id")) {
            isEditingMode = true;

            itemId = intent.getStringExtra("Item Id");
            String supplierName = intent.getStringExtra("Supplier Name");
            String itemName = intent.getStringExtra("Item Name");
            int itemPrice = intent.getIntExtra("Item Price", 0);
            String itemDescription = intent.getStringExtra("Item Description");

            textInputEditTextSupplierName.setText(supplierName);
            textInputEditTextItemName.setText(itemName);
            textInputEditTextItemPrice.setText(String.valueOf(itemPrice));
            textInputEditTextItemDescription.setText(itemDescription);
        }

        // Set Supplier Name field with the username from the session manager
        textInputEditTextSupplierName.setText(SessionManager.getInstance(this).getUsername());

        buttonPost.setOnClickListener(v -> {
            SupplierName = String.valueOf(textInputEditTextSupplierName.getText());
            ItemName = String.valueOf(textInputEditTextItemName.getText());
            ItemPrice = Integer.parseInt(String.valueOf(textInputEditTextItemPrice.getText()));
            ItemDescription = String.valueOf(textInputEditTextItemDescription.getText());

            if (isEditingMode) {
                updatePostItem(itemId);
            }else {
                if(validateInputs()) {
                    postItem();
                }
            }
        });
    }

    private void postItem() {
        // Obtain the supplierId from the SessionManager
        int supplierId = SessionManager.getInstance(getApplicationContext()).getSupplierId();

        //Create new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, post_url, response -> {
            // Handle response from the server
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
                params.put("supplierId", String.valueOf(supplierId));
                params.put("supplierName", SupplierName);
                params.put("supplierItemName", ItemName);
                params.put("supplierItemPrice", String.valueOf(ItemPrice));
                params.put("supplierItemDescription", ItemDescription);
                return params;
            }
        };

        //Add StringRequest to Request Queue to execute network request
        queue.add(request);
    }

    private void updatePostItem(String itemId) {
        // Create a new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, update_post_url, response -> {
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
                params.put("supplierName", SupplierName);
                params.put("supplierItemName", ItemName);
                params.put("supplierItemPrice", String.valueOf(ItemPrice));
                params.put("supplierItemDescription", ItemDescription);
                params.put("supplierItemId", itemId);
                return params;
            }
        };

        //Add StringRequest to Request Queue to execute network request
        queue.add(request);
    }

    private boolean validateInputs() {
        if(SupplierName.isEmpty()) {
            textInputEditTextSupplierName.setError("This field cannot be empty");
            textInputEditTextSupplierName.requestFocus();
            return false;
        }

        if(ItemName.isEmpty()) {
            textInputEditTextItemName.setError("This field cannot be empty");
            textInputEditTextItemName.requestFocus();
            return false;
        }

        if(ItemPrice == 0) {
            textInputEditTextItemPrice.setError("This is not an appropriate value for this field");
            textInputEditTextItemPrice.requestFocus();
            return false;
        }

        if(ItemDescription.isEmpty()) {
            textInputEditTextItemDescription.setError("This field cannot be empty");
            textInputEditTextItemDescription.requestFocus();
            return false;
        }

        return true;
    }

    private void back() {
        Intent intent = new Intent(this, MyItems.class);
        startActivity(intent);
        finish();
    }
}