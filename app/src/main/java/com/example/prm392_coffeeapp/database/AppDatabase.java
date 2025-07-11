package com.example.prm392_coffeeapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prm392_coffeeapp.dao.CartItemDao;
import com.example.prm392_coffeeapp.dao.OrderDao;
import com.example.prm392_coffeeapp.dao.OrderItemDao;
import com.example.prm392_coffeeapp.dao.ProductDao;
import com.example.prm392_coffeeapp.dao.UserDao;
import com.example.prm392_coffeeapp.entity.CartItem;
import com.example.prm392_coffeeapp.entity.Order;
import com.example.prm392_coffeeapp.entity.OrderItem;
import com.example.prm392_coffeeapp.entity.Product;
import com.example.prm392_coffeeapp.entity.User;

@Database(entities = {User.class, Product.class, Order.class, OrderItem.class, CartItem.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase getInstance(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, "coffee_database")
                .fallbackToDestructiveMigration()
                .build();
    }
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderItemDao orderItemDao();
    public abstract CartItemDao cartItemDao();
}
