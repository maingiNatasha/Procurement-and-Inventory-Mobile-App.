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

public class PurchaseActivity extends AppCompatActivity {
    private static final String view_orders_url = "http://192.168.220.66/PrototypeApi/viewPurchaseOrder.php";
    RecyclerView recyclerView;
    @SuppressWarnings("rawtypes")
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PurchaseData> purchaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procure);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.purchases);

        recyclerView = findViewById(R.id.rview_layout);
        purchaseData = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PurchasesRvAdapter(getApplicationContext(), purchaseData);
        recyclerView.setAdapter(adapter);

        loadOrders();
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
    }

    private void inventoryPage() {
        Toast.makeText(this, "Inventory Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    private void supplierPage() {
        Toast.makeText(this, "Supplier Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SupplierActivity.class);
        startActivity(intent);
    }

    private void homePage() {
        Toast.makeText(this, "Home Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void loadOrders() {
        // Get the farmerId from the SessionManager
        int desiredFarmerId = SessionManager.getInstance(this).getFarmerId();

        MySingleton mySingleton = MySingleton.getInstance(this);
        RequestQueue queue = mySingleton.getRequestQueue();

        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, view_orders_url, null, response -> {
            Log.d("volley", "response: " + response.toString());

            purchaseData.clear();

            try {
                ArrayList<PurchaseData> filteredData = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject data = response.getJSONObject(i);
                    int farmerId = data.getInt("farmerId");

                    if (farmerId == desiredFarmerId) {
                        PurchaseData pd = new PurchaseData();

                        pd.setpOrderId(data.getString("orderId"));
                        pd.setpItemName(data.getString("orderItemName"));
                        pd.setpItemTotal(data.getInt("orderTotalPrice"));
                        pd.setpItemQuantity(data.getInt("orderItemQuantity"));
                        pd.setpItemUnit(data.getString("orderItemUnit"));
                        pd.setPaymentMethod(data.getString("paymentMethod"));
                        pd.setpSupplier(data.getString("orderSupplierName"));

                        filteredData.add(pd);
                    }
                }

                purchaseData.addAll(filteredData);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.d("volley", "error: " + error.getMessage()));

        queue.add(arrayRequest);
    }
}