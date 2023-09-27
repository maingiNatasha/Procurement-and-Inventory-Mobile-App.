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

public class ViewInventory extends AppCompatActivity {
    private static final String view_inventory_url = "http://192.168.220.66/PrototypeApi/viewInventory.php";
    RecyclerView recyclerView;
    @SuppressWarnings("rawtypes")
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ModelData> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        recyclerView = findViewById(R.id.rview_layout);
        items = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RvAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);

        viewInventory();
    }

    private void viewInventory() {
        // Get the farmerId from the SessionManager
        int desiredFarmerId = SessionManager.getInstance(this).getFarmerId();

        MySingleton mySingleton = MySingleton.getInstance(this);
        RequestQueue queue = mySingleton.getRequestQueue();

        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, view_inventory_url, null, response -> {
            Log.d("volley", "response: " + response.toString());

            items.clear();

            try {
                ArrayList<ModelData> filteredData = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject data = response.getJSONObject(i);
                    int farmerId = data.getInt("farmerId");

                    if (farmerId == desiredFarmerId) {
                        ModelData md = new ModelData();

                        md.setItemId(data.getString("itemId"));
                        md.setItemName(data.getString("itemName"));
                        md.setItemType(data.getString("itemType"));
                        md.setItemQuantity(data.getInt("itemQuantity"));
                        md.setItemMinQuantity(data.getInt("itemMinQuantity"));
                        md.setItemUnit(data.getString("itemUnit"));

                        filteredData.add(md);
                    }
                }

                items.addAll(filteredData);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error", "Error parsing JSON response");
            }

        }, error -> Log.d("volley", "error: " + error.getMessage()));

        queue.add(arrayRequest);
    }
}
