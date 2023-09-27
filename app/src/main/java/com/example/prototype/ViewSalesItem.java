package com.example.prototype;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewSalesItem extends AppCompatActivity {

    private static final String view_item_url = "http://192.168.220.66/PrototypeApi/viewSalesItem.php";
    RecyclerView recyclerView;
    @SuppressWarnings("rawtypes")
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SupplierData> supplierData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sales_item);

        recyclerView = findViewById(R.id.sales_items_rv_layout);
        supplierData = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PostRvAdapter(getApplicationContext(), supplierData);
        recyclerView.setAdapter(adapter);

        viewItems();
    }

    private void viewItems() {
        // Get the supplierId from the SessionManager
        int desiredSupplierId = SessionManager.getInstance(this).getSupplierId();

        MySingleton mySingleton = MySingleton.getInstance(this);
        RequestQueue queue = mySingleton.getRequestQueue();

        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, view_item_url, null, response -> {
            Log.d("volley", "response: " + response.toString());

            supplierData.clear();

            try {
                ArrayList<SupplierData> filteredData = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject data = response.getJSONObject(i);
                    int supplierId = data.getInt("supplierId");

                    if (supplierId == desiredSupplierId) {
                        SupplierData sd = new SupplierData();
                        sd.setItemId(data.getString("supplierItemId"));
                        sd.setSupplierName(data.getString("supplierName"));
                        sd.setSupplierItemName(data.getString("supplierItemName"));
                        sd.setSupplierItemPrice(data.getInt("supplierItemPrice"));
                        sd.setSupplierItemDescription(data.getString("supplierItemDescription"));
                        filteredData.add(sd);
                    }
                }

                supplierData.addAll(filteredData);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error", "Error parsing JSON response");
            }
        }, error -> {
            // Handle error
            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Error", error.getMessage());
        });

        queue.add(arrayRequest);
    }

}