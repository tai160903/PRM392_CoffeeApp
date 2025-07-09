package com.example.prm392_coffeeapp.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items",
foreignKeys = {
    @ForeignKey(
        entity = Order.class,
        parentColumns = "uuid",
        childColumns = "order_uuid",
        onDelete = ForeignKey.CASCADE

    ),
    @ForeignKey(
        entity = Product.class,
        parentColumns = "uuid",
        childColumns = "product_uuid",
        onDelete = ForeignKey.CASCADE
    )
})
public class OrderItem {
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    @NonNull
    private String uuid;
    @ColumnInfo(name = "order_uuid")

    private String orderUuid;

    @ColumnInfo(name = "product_uuid")
    private String productUuid;
    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "price")
    private double price;

    public OrderItem(String uuid, String orderUuid, String productUuid, int quantity, double price) {
        this.uuid = uuid;
        this.orderUuid = orderUuid;
        this.productUuid = productUuid;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
