package com.example.prm392_coffeeapp.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_coffeeapp.entity.Order;

@Dao
public interface OrderDao {
    @Insert
    void insertOrder(Order order);

    @Query("SELECT * FROM orders")
    Order[] getAllOrders();

    @Query("SELECT * FROM orders WHERE uuid = :orderId")
    Order getOrderById(int orderId);

    @Query("DELETE FROM orders WHERE uuid = :orderId")
    void deleteOrderById(int orderId);

    @Query("UPDATE orders SET status = :status WHERE uuid = :orderId")
    void updateOrderStatus(int orderId, String status);

}
