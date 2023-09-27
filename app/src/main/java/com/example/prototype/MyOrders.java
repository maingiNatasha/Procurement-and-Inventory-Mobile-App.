package com.example.prototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MyOrders extends AppCompatActivity {
    private static final String view_orders_url = "http://192.168.220.66/PrototypeApi/viewPurchaseOrder.php";
    RecyclerView recyclerView;
    @SuppressWarnings("rawtypes")
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PurchaseData> purchaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.orders);

        recyclerView = findViewById(R.id.supplier_order_rv_layout);
        purchaseData = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SupplierOrdersRvAdapter(purchaseData, getApplicationContext());
        recyclerView.setAdapter(adapter);

        viewOrders();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            MyOrdersPage();
        }
        else if (item.getItemId() == R.id.item2) {
            MyItemsPage();
        }
        else if (item.getItemId() == R.id.home) {
            homePage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void homePage() {
        Toast.makeText(this, "Home Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SupplierHomePage.class);
        startActivity(intent);
    }

    private void MyItemsPage() {
        Toast.makeText(this, "Items Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyItems.class);
        startActivity(intent);
    }

    private void MyOrdersPage() {
        Toast.makeText(this, "Orders Page!", Toast.LENGTH_SHORT).show();
    }

    private void viewOrders() {
        // Get the supplierName from the SessionManager
        String desiredSupplierName = SessionManager.getInstance(this).getUsername();

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
                    String supplierName = data.getString("orderSupplierName");

                    if (supplierName.equals(desiredSupplierName)) {
                        PurchaseData pd = new PurchaseData();
                        pd.setpOrderId(data.getString("orderId"));
                        pd.setpItemName(data.getString("orderItemName"));
                        pd.setpItemTotal(data.getInt("orderTotalPrice"));
                        pd.setpItemQuantity(data.getInt("orderItemQuantity"));
                        pd.setpItemUnit(data.getString("orderItemUnit"));
                        pd.setPaymentMethod(data.getString("paymentMethod"));
                        pd.setpSupplier(data.getString("orderSupplierName"));
                        pd.setpCustomer(data.getString("orderCustomerName"));
                        pd.setpStatus(data.getString("orderStatus"));

                        filteredData.add(pd);
                    }
                }

                purchaseData.addAll(filteredData);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error", "Error parsing JSON response");
            }

            adapter.notifyDataSetChanged();
        }, error -> Log.d("volley", "error: " + error.getMessage()));

        queue.add(arrayRequest);
    }
}