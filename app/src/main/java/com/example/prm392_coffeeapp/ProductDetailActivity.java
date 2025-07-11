package com.example.prm392_coffeeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.example.prm392_coffeeapp.dao.CartItemDao;
import com.example.prm392_coffeeapp.dao.ProductDao;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.CartItem;
import com.example.prm392_coffeeapp.entity.Product;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView tvProductName, tvProductPrice, tvProductDescription;
    private EditText etQuantity;
    private Button btnDecrease, btnIncrease, btnAddToCart;

    private Product currentProduct; // Store loaded product for cart logic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Detail");
        }

        imgProduct = findViewById(R.id.imgProductDetail);
        tvProductName = findViewById(R.id.tvProductNameDetail);
        tvProductPrice = findViewById(R.id.tvProductPriceDetail);
        tvProductDescription = findViewById(R.id.tvProductDescriptionDetail);

        etQuantity = findViewById(R.id.etQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        btnDecrease.setOnClickListener(v -> {
            int qty = Integer.parseInt(etQuantity.getText().toString());
            if (qty > 1) {
                etQuantity.setText(String.valueOf(qty - 1));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            int qty = Integer.parseInt(etQuantity.getText().toString());
            etQuantity.setText(String.valueOf(qty + 1));
        });

        btnAddToCart.setOnClickListener(v -> {
            int qty = Integer.parseInt(etQuantity.getText().toString());
            if (currentProduct == null) {
                Toast.makeText(this, "Product not loaded", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                CartItemDao cartItemDao = AppDatabase.getInstance(getApplicationContext()).cartItemDao();
                CartItem cartItem = new CartItem(
                        currentProduct.getUuid(),
                        currentProduct.getName(),
                        qty,
                        currentProduct.getPrice()
                );
                cartItemDao.insert(cartItem);
                runOnUiThread(() -> Toast.makeText(this, "Added " + qty + " to cart", Toast.LENGTH_SHORT).show());
            }).start();
        });

        String productId = getIntent().getStringExtra("product_id");
        if (productId == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(() -> {
            ProductDao productDao = AppDatabase.getInstance(getApplicationContext()).productDao();
            Product product = productDao.getProductById(productId);

            runOnUiThread(() -> {
                if (product != null) {
                    currentProduct = product;
                    tvProductName.setText(product.getName());
                    tvProductPrice.setText(product.getPrice() + "$");
                    tvProductDescription.setText(product.getDescription());
                    Glide.with(this)
                            .load(product.getImageUrl())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(imgProduct);
                } else {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
