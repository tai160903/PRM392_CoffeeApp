package com.example.prm392_coffeeapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_coffeeapp.entity.Product;


@Dao
public interface ProductDao {
    @Insert
    void insertProduct(Product product);

    @Query("SELECT * FROM products")
    Product[] getAllProducts();

    @Query("SELECT * FROM products WHERE uuid = :productId")
    Product getProductById(String productId);

    @Query("DELETE FROM products WHERE uuid = :productId")
    void deleteProductById(String productId);

    @Query("UPDATE products SET name = :name, price = :price, description = :description WHERE uuid = :productId")
    void updateProduct(String productId, String name, double price, String description);

}
