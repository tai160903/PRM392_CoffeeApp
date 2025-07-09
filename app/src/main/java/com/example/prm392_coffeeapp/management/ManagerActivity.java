package com.example.prm392_coffeeapp.management;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.prm392_coffeeapp.R;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

public class ManagerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // Ánh xạ view
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        // Thiết lập toolbar như ActionBar
        setSupportActionBar(toolbar);

        // Cài đặt toggle để mở/đóng drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý sự kiện khi chọn menu trong drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_product) {
                startActivity(new Intent(ManagerActivity.this, ProductListActivity.class));
            } else if (id == R.id.nav_order) {
                startActivity(new Intent(ManagerActivity.this, OrderListActivity.class));
            } else if (id == R.id.nav_logout) {
                finish(); // hoặc: startActivity(new Intent(this, LoginActivity.class));
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Ghi đè nút Back để đóng drawer nếu đang mở
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
