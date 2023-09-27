package com.example.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class InventoryActivity extends AppCompatActivity {
    Button buttonAdd, buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.inventory);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonView = findViewById(R.id.buttonView);

        buttonAdd.setOnClickListener(v -> addItemPage());
        buttonView.setOnClickListener(v -> viewItemPage());
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

    private void addItemPage() {
        Intent intent = new Intent(this, AddInventoryItem.class);
        startActivity(intent);
    }

    private void viewItemPage() {
        Intent intent = new Intent(this, ViewInventory.class);
        startActivity(intent);
    }
}