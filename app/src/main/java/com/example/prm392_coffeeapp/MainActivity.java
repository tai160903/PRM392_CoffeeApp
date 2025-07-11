package com.example.prm392_coffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_coffeeapp.dao.ProductDao;
import com.example.prm392_coffeeapp.dao.UserDao;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.Product;
import com.example.prm392_coffeeapp.entity.User;
import com.example.prm392_coffeeapp.management.ManagerActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        userDao = AppDatabase.getInstance(this).userDao();

        initialProducts();

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User user = userDao.getUserByUsernameAndPassword(username, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        if ("admin".equals(user.getRole())) {
                            startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                            finish();
                        } else if ("user".equals(user.getRole())) {
                             startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Unknown user role", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void initialProducts() {
        ProductDao productDao = AppDatabase.getInstance(this).productDao();
        // clear existing products
//        new Thread(() -> productDao.deleteAllProducts()).start();
        Product[] products = new Product[]{
                new Product(UUID.randomUUID().toString(), "Espresso", "Strong and bold espresso shot", 2.5, "https://res.cloudinary.com/dpd9had6j/image/upload/v1752211174/a0dd6099-1985-481b-89b5-ecf6eb00c7ec.png"),
                new Product(UUID.randomUUID().toString(), "Cappuccino", "Espresso with steamed milk and foam", 3.0, "https://res.cloudinary.com/dpd9had6j/image/upload/v1752211193/98b15f10-c2b1-4b14-99a9-ff7c19e2cd37.png"),
                new Product(UUID.randomUUID().toString(), "Latte", "Smooth blend of espresso and steamed milk", 3.5, "https://res.cloudinary.com/dpd9had6j/image/upload/v1752211314/1f2ae885-238f-44b8-8cde-f257ec4de68f.png"),
                new Product(UUID.randomUUID().toString(), "Mocha", "Espresso with chocolate and steamed milk", 4.0, "https://res.cloudinary.com/dpd9had6j/image/upload/v1752211368/54c3e3c7-428f-40fc-a309-f133e56d66ee.png"),
                new Product(UUID.randomUUID().toString(), "Americano", "Espresso with added hot water", 2.8, "https://res.cloudinary.com/dpd9had6j/image/upload/v1752211370/de8ed277-b4f4-426c-84d8-3884acf3dc12.png")
        };
        // check if products already exist

        new Thread(() -> {
            Product[] existingProducts = productDao.getAllProducts();
            if (existingProducts.length == 0) {
                for (Product p : products) {
                    productDao.insertProduct(p);
                }
            }
        }).start();
    }

}
