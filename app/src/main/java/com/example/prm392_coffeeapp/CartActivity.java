package com.example.prm392_coffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_coffeeapp.adapter.CartAdapter;
import com.example.prm392_coffeeapp.dao.CartItemDao;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems);
        recyclerCartItems.setAdapter(cartAdapter);

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            new Thread(() -> {
                CartItemDao cartItemDao = AppDatabase.getInstance(getApplicationContext()).cartItemDao();
                cartItemDao.clearCart();
                runOnUiThread(() -> {
                    cartItems.clear();
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                    Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_cart) {
                // Already here
                return true;
            } else if (id == R.id.nav_profile) {
//                startActivity(new Intent(this, ProfileActivity.class));
                Toast.makeText(this, "Profile feature is not implemented yet", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadCartItems() {
        new Thread(() -> {
            CartItemDao cartItemDao = AppDatabase.getInstance(getApplicationContext()).cartItemDao();
            List<CartItem> items = cartItemDao.getAllCartItems();
            runOnUiThread(() -> {
                cartItems.clear();
                cartItems.addAll(items);
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            });
        }).start();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.price * item.quantity;
        }
        tvTotalPrice.setText("Total: $" + String.format("%.2f", total));
    }
}
