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

public class SupplierHomePage extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridView gridView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.home_page);

        gridView = findViewById(R.id.home_grid_view);
        sessionManager = new SessionManager(getApplicationContext());

        ArrayList<HomeItem> homeItemsList = new ArrayList<>();
        homeItemsList.add(new HomeItem("View my orders", R.drawable.inventory));
        homeItemsList.add(new HomeItem("Upload item for sale", R.drawable.procurement));
        homeItemsList.add(new HomeItem("View Reports", R.drawable.report));
        homeItemsList.add(new HomeItem("Log out", R.drawable.logout));

        HomeAdapter adapter = new HomeAdapter(this, homeItemsList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click based on position
            if (position == 0) {
                MyOrdersPage();
            } else if (position == 1) {
                MyItemsPage();
            } else if (position == 2) {
                report();
            } else if (position == 3) {
                logOut();
            }

        });
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

    private void homePage() { Toast.makeText(this, "Home Page!", Toast.LENGTH_SHORT).show();}

    private void MyItemsPage() {
        Toast.makeText(this, "Items Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyItems.class);
        startActivity(intent);
    }

    private void MyOrdersPage() {
        Toast.makeText(this, "Orders Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyOrders.class);
        startActivity(intent);
    }

    private void report() {
        Toast.makeText(this, "Reports Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SupplierReports.class);
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