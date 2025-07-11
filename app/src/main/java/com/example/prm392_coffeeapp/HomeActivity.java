package com.example.prm392_coffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_coffeeapp.adapter.ProductCustomerAdapter;
import com.example.prm392_coffeeapp.dao.CartItemDao;
import com.example.prm392_coffeeapp.dao.ProductDao;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.CartItem;
import com.example.prm392_coffeeapp.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerCustomerProducts;
    private ProductCustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerCustomerProducts = findViewById(R.id.recyclerCustomerProducts);
        recyclerCustomerProducts.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<Product> productList;
            try {
                ProductDao productDao = AppDatabase.getInstance(getApplicationContext()).productDao();
                productList = Arrays.asList(productDao.getAllProducts());
            } catch (Exception e) {
                productList = Collections.emptyList();
                e.printStackTrace();
            }

            List<Product> finalProductList = productList;
            runOnUiThread(() -> {
                adapter = new ProductCustomerAdapter(HomeActivity.this, finalProductList);
                recyclerCustomerProducts.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                recyclerCustomerProducts.setAdapter(adapter);

                adapter.setOnItemClickListener(product -> {
                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra("product_id", product.getUuid());
                    startActivity(intent);
                });
            });
        }).start();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            String name = getResources().getResourceEntryName(id);
            if (id == R.id.nav_home) {
                Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_cart) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Profile is not implemented yet", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
