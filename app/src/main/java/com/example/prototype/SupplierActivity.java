package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SupplierActivity extends AppCompatActivity {
    private static final String supplier_url = "http://192.168.220.66/PrototypeApi/supplier.php";
    RecyclerView recyclerView;
    @SuppressWarnings("rawtypes")
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SupplierData> supplierData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.suppliers);

        recyclerView = findViewById(R.id.supplier_rv_layout);
        supplierData = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SupplierRvAdapter(getApplicationContext(), supplierData);
        recyclerView.setAdapter(adapter);

        loadSupplier();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            supplierPage();
        }
        else if (item.getItemId() == R.id.item2) {
            inventoryPage();
        }
        else if (item.getItemId() == R.id.item3) {
            procurementPage();
        }
        else if (item.getItemId() == R.id.home) {
            homePage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void procurementPage() {
        Toast.makeText(this, "Purchases Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PurchaseActivity.class);
        startActivity(intent);
    }

    private void inventoryPage() {
        Toast.makeText(this, "Inventory Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    private void supplierPage() {
        Toast.makeText(this, "Supplier Page!", Toast.LENGTH_SHORT).show();
    }

    private void homePage() {
        Toast.makeText(this, "Home Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadSupplier() {
        MySingleton mySingleton = MySingleton.getInstance(this);
        RequestQueue queue = mySingleton.getRequestQueue();

        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, supplier_url, null, response -> {
            Log.d("volley", "response: " + response.toString());

            supplierData.clear();

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject data = response.getJSONObject(i);
                    SupplierData sd = new SupplierData();

                    sd.setSupplierName(data.getString("supplierName"));
                    sd.setSupplierItemName(data.getString("supplierItemName"));
                    sd.setSupplierItemPrice(data.getInt("supplierItemPrice"));
                    sd.setSupplierItemDescription(data.getString("supplierItemDescription"));

                    supplierData.add(sd);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }, error -> Log.d("volley", "error: " + error.getMessage()));

        queue.add(arrayRequest);
    }
}
