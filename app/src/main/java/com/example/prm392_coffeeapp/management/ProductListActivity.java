package com.example.prm392_coffeeapp.management;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.prm392_coffeeapp.R;
import com.example.prm392_coffeeapp.adapter.ProductManagerAdapter;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.Product;

import java.util.*;

public class ProductListActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1001;
    private Uri selectedImageUri = null;
    private ImageView previewImage;

    private RecyclerView recyclerProducts;
    private Button btnAddProduct;
    private List<Product> productList;
    private ProductManagerAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        db = AppDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar); // Fix: import and use correct Toolbar
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        initCloudinary();

        productList = new ArrayList<>();
        adapter = new ProductManagerAdapter(productList, new ProductManagerAdapter.OnProductActionListener() {
            @Override
            public void onEdit(Product product) {
                Toast.makeText(ProductListActivity.this, "Chỉnh sửa: " + product.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(Product product) {
                new AlertDialog.Builder(ProductListActivity.this)
                        .setTitle("Xoá sản phẩm")
                        .setMessage("Bạn có chắc muốn xoá " + product.getName() + "?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            db.productDao().deleteProductById(product.getUuid());
                            reloadProducts();
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }
        });

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducts.setAdapter(adapter);

        btnAddProduct.setOnClickListener(v -> showAddProductDialog());
        new Thread(() -> {
            productList.addAll(Arrays.asList(db.productDao().getAllProducts()));
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    private void showAddProductDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);

        EditText edtName = dialogView.findViewById(R.id.etProductName);
        EditText edtDesc = dialogView.findViewById(R.id.etProductDescription);
        EditText edtPrice = dialogView.findViewById(R.id.etProductPrice);
        previewImage = dialogView.findViewById(R.id.imgProduct);

        // Set image picker on the image view
        previewImage.setOnClickListener(v -> pickImage());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Thêm sản phẩm mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", null)
                .setNegativeButton("Huỷ", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Override "Thêm" để validate & upload ảnh
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();
            String priceText = edtPrice.getText().toString().trim();

            if (name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri == null) {
                Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            MediaManager.get().upload(selectedImageUri)
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {}

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {}

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String imageUrl = resultData.get("secure_url").toString();

                            Product newProduct = new Product(
                                    UUID.randomUUID().toString(),
                                    name,
                                    desc,
                                    price,
                                    imageUrl
                            );

                            new Thread(() -> {
                                db.productDao().insertProduct(newProduct);
                                runOnUiThread(() -> {
                                    productList.add(newProduct);
                                    adapter.notifyItemInserted(productList.size() - 1);
                                    Toast.makeText(ProductListActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    selectedImageUri = null;
                                    dialog.dismiss();
                                });
                            }).start();
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Toast.makeText(ProductListActivity.this, "Lỗi upload ảnh", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {}
                    })
                    .dispatch();
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (previewImage != null) {
                previewImage.setImageURI(selectedImageUri);
            }
        }
    }

    private void reloadProducts() {
        new Thread(() -> {
            List<Product> products = Arrays.asList(db.productDao().getAllProducts());
            runOnUiThread(() -> {
                if (productList == null) {
                    productList = new ArrayList<>();
                }
                productList.clear();
                productList.addAll(products);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void initCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dpd9had6j");
        config.put("api_key", "777843265695127");
        config.put("api_secret", "XCJDuDXtMW7E1stnbJDJBZr5wD0");
        MediaManager.init(this, config);
    }
}
