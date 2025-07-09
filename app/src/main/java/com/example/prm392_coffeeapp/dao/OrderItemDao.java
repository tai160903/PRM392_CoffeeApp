package com.example.prm392_coffeeapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_coffeeapp.entity.OrderItem;

@Dao
public interface OrderItemDao {
    @Insert
    void insertOrderItem(OrderItem orderItem);

    @Query("SELECT * FROM order_items WHERE uuid = :orderId")
    OrderItem[] getOrderItemsByOrderId(int orderId);
}
