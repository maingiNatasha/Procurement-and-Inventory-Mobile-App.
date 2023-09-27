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

public class AddInventoryItem extends AppCompatActivity {
    TextInputEditText textInputEditTextItemName, textInputEditTextItemType, textInputEditTextItemQuantity, textInputEditTextItemMinQuantity, textInputEditTextItemUnit;
    Button buttonSaveItem, buttonViewItems;
    String itemName, itemType, itemUnit, itemId;
    int itemQuantity, itemMinQuantity;

    private static final String add_item_url = "http://192.168.220.66/PrototypeApi/addInventory.php";
    private static final String update_item_url = "http://192.168.220.66/PrototypeApi/updateItem.php";
    private boolean isEditingMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory_item);

        textInputEditTextItemName = findViewById(R.id.etItemName);
        textInputEditTextItemType = findViewById(R.id.etItemType);
        textInputEditTextItemQuantity = findViewById(R.id.etItemQuantity);
        textInputEditTextItemMinQuantity = findViewById(R.id.etItemMinQuantity);
        textInputEditTextItemUnit = findViewById(R.id.etItemUnit);
        buttonSaveItem = findViewById(R.id.buttonSaveItem);
        buttonViewItems = findViewById(R.id.buttonViewItems);

        // Check if the activity was started in editing mode
        Intent intent = getIntent();
        if (intent.hasExtra("itemId")) {
            isEditingMode = true;
            // Retrieve the item information from the intent
            itemId = intent.getStringExtra("itemId");
            String itemName = intent.getStringExtra("itemName");
            String itemType = intent.getStringExtra("itemType");
            int itemQuantity = intent.getIntExtra("itemQuantity", 0);
            int itemMinQuantity = intent.getIntExtra("itemMinQuantity", 0);
            String itemUnit = intent.getStringExtra("itemUnit");

            // Set the retrieved item information to the corresponding EditText fields
            textInputEditTextItemName.setText(itemName);
            textInputEditTextItemType.setText(itemType);
            textInputEditTextItemQuantity.setText(String.valueOf(itemQuantity));
            textInputEditTextItemMinQuantity.setText(String.valueOf(itemMinQuantity));
            textInputEditTextItemUnit.setText(itemUnit);
        }

        buttonViewItems.setOnClickListener(v -> {
            Intent intent2 = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(intent2);
            finish();
        });

        buttonSaveItem.setOnClickListener(v -> {
            itemName = String.valueOf(textInputEditTextItemName.getText());
            itemType = String.valueOf(textInputEditTextItemType.getText());
            itemQuantity = Integer.parseInt(String.valueOf(textInputEditTextItemQuantity.getText()));
            itemMinQuantity = Integer.parseInt(String.valueOf(textInputEditTextItemMinQuantity.getText()));
            itemUnit = String.valueOf(textInputEditTextItemUnit.getText());

            if (isEditingMode) {
                updateItem(itemId);
            }else {
                if(validateInputs()) {
                    addItem();
                }
            }
        });
    }
    
    private void addItem() {
        int farmerId = SessionManager.getInstance(getApplicationContext()).getFarmerId();

        //Create new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, add_item_url, response -> {
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
                params.put("itemName", itemName);
                params.put("itemType", itemType);
                params.put("itemQuantity", String.valueOf(itemQuantity));
                params.put("itemMinQuantity", String.valueOf(itemMinQuantity));
                params.put("itemUnit", itemUnit);
                params.put("farmerId", String.valueOf(farmerId));
                return params;
            }
        };

        //Add StringRequest to Request Queue to execute network request
        queue.add(request);
    }

    private void updateItem(String itemId) {
        // Create a new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, update_item_url, response -> {
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
                params.put("itemName", itemName);
                params.put("itemType", itemType);
                params.put("itemQuantity", String.valueOf(itemQuantity));
                params.put("itemMinQuantity", String.valueOf(itemMinQuantity));
                params.put("itemUnit", itemUnit);
                params.put("itemId", itemId);
                return params;
            }
        };

        //Add StringRequest to Request Queue to execute network request
        queue.add(request);
    }


    private boolean validateInputs() {
        if(itemName.isEmpty()) {
            textInputEditTextItemName.setError("Name cannot be empty");
            textInputEditTextItemName.requestFocus();
            return false;
        }

        if(itemType.isEmpty()) {
            textInputEditTextItemType.setError("Type Description cannot be empty");
            textInputEditTextItemType.requestFocus();
            return false;
        }

        if(itemMinQuantity == 0) {
            textInputEditTextItemMinQuantity.setError("You need to set a minimum quantity");
            textInputEditTextItemMinQuantity.requestFocus();
            return false;
        }

        if(itemUnit.isEmpty()) {
            textInputEditTextItemUnit.setError("Unit cannot be empty");
            textInputEditTextItemUnit.requestFocus();
            return false;
        }

        return true;
    }
}