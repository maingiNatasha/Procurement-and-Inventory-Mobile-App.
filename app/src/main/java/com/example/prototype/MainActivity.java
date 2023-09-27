package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridView gridView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.home_page);

        gridView = findViewById(R.id.home_grid_view);
        sessionManager = new SessionManager(getApplicationContext());

        ArrayList<HomeItem> homeItemsList = new ArrayList<>();
        homeItemsList.add(new HomeItem("Inventory Management", R.drawable.inventory));
        homeItemsList.add(new HomeItem("View Available Suppliers", R.drawable.suppliers));
        homeItemsList.add(new HomeItem("Purchase Order History", R.drawable.procurement));
        homeItemsList.add(new HomeItem("View Reports", R.drawable.report));
        homeItemsList.add(new HomeItem("Log out", R.drawable.logout));

        HomeAdapter adapter = new HomeAdapter(this, homeItemsList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click based on position
            if (position == 0) {
                inventoryPage();
            } else if (position == 1) {
                supplierPage();
            } else if (position == 2) {
                procurementPage();
            } else if (position == 3) {
                report();
            } else if (position == 4) {
                logOut();
            }
        });

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
        Intent intent = new Intent(this, SupplierActivity.class);
        startActivity(intent);
    }

    private void homePage() {
        Toast.makeText(this, "Home Page!", Toast.LENGTH_SHORT).show();
    }

    private void report() {
        Toast.makeText(this, "Reports Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Reports.class);
        startActivity(intent);
    }

    private void logOut() {
        sessionManager.clearSession();
        Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}