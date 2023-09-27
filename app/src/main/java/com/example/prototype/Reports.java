package com.example.prototype;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Reports extends AppCompatActivity {
    Button purchaseReport, inventoryReport;
    public final String purchase_report_url = "http://192.168.220.66/PrototypeApi/generateReport.php";
    public final String inventory_report_url = "http://192.168.220.66/PrototypeApi/generateInventoryReport.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView customTitleTextView = findViewById(R.id.custom_title_textview);
        customTitleTextView.setText(R.string.reports);

        purchaseReport = findViewById(R.id.purchasesReport);
        inventoryReport = findViewById(R.id.inventoryReport);
        
        purchaseReport.setOnClickListener(view -> pReport());
        inventoryReport.setOnClickListener(view -> iReport());
    }
    private void openPdfViewer(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where a PDF viewer is not available on the device
            Toast.makeText(this, "No PDF viewer app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void pReport() {
        int farmerId = SessionManager.getInstance(this).getFarmerId();

        // Create new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, purchase_report_url,
                response -> {
                    Log.d("JSON_RESPONSE", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String pdfUrl = jsonObject.getString("pdf_url");
                        Log.d("JSON_RESPONSE", pdfUrl);

                        // Now, call openPdfViewer with the PDF URL
                        openPdfViewer(pdfUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("farmerId", String.valueOf(farmerId));
                return params;
            }
        };

        queue.add(request);
    }

    private void iReport() {
        int farmerId = SessionManager.getInstance(this).getFarmerId();

        // Create new RequestQueue for managing network requests
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create new StringRequest with a request method and response listener
        StringRequest request = new StringRequest(Request.Method.POST, inventory_report_url,
                response -> {
                    Log.d("JSON_RESPONSE", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String pdfUrl = jsonObject.getString("pdf_url");
                        Log.d("JSON_RESPONSE", pdfUrl);

                        // Now, call openPdfViewer with the PDF URL
                        openPdfViewer(pdfUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("farmerId", String.valueOf(farmerId));
                return params;
            }
        };

        queue.add(request);
    }

}