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

public class MyItems extends AppCompatActivity {
    Button buttonPost, buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.my_items);

        buttonPost = findViewById(R.id.buttonPost);
        buttonView = findViewById(R.id.buttonViewItems);

        buttonPost.setOnClickListener(v -> PostItem());

        buttonView.setOnClickListener(v -> ViewItem());
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
    }

    private void MyOrdersPage() {
        Toast.makeText(this, "Orders Page!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyOrders.class);
        startActivity(intent);
    }

    private void ViewItem() {
        Intent intent = new Intent(this, ViewSalesItem.class);
        startActivity(intent);
    }

    private void PostItem() {
        Intent intent = new Intent(this, PostSalesItem.class);
        startActivity(intent);
    }
}