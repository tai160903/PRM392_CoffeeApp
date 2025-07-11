package com.example.prm392_coffeeapp.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_coffeeapp.R;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.Product;

public class EditProductActivity extends AppCompatActivity {

    private EditText etProductName, etProductDescription, etProductPrice;
    private Button btnSaveProduct;

    private AppDatabase db;
    private String productUuid;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        db = AppDatabase.getInstance(this);

        productUuid = getIntent().getStringExtra("productUuid");
        System.out.println("Product UUID: " + productUuid);
        if (productUuid == null || productUuid.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load dữ liệu trong background
        new Thread(() -> {
            product = db.productDao().getProductById(productUuid);
            runOnUiThread(() -> {
                if (product != null) {
                    etProductName.setText(product.getName() != null ? product.getName() : "");
                    etProductDescription.setText(product.getDescription() != null ? product.getDescription() : "");
                    etProductPrice.setText(String.valueOf(product.getPrice()));
                }
            });
        }).start();

        btnSaveProduct.setOnClickListener(view -> {
            String name = etProductName.getText().toString().trim();
            String desc = etProductDescription.getText().toString().trim();
            String priceStr = etProductPrice.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (product == null) {
                Toast.makeText(this, "Không tìm thấy sản phẩm để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }

            product.setName(name);
            product.setDescription(desc);
            product.setPrice(price);

            btnSaveProduct.setEnabled(false); // Prevent multiple clicks

            new Thread(() -> {
                db.productDao().updateProduct(
                    product.getUuid(),
                    product.getName(),
                    product.getPrice(),
                    product.getDescription()
                );
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}
