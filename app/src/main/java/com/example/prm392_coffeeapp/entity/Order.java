package com.example.prm392_coffeeapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    @NonNull
    private String uuid;
    @ColumnInfo(name = "user_uuid")
    private String userUuid;
    @ColumnInfo(name = "date_time")
    private String dateTime;
    @ColumnInfo(name = "total_price")
    private String totalPrice;
    @ColumnInfo(name = "status")
    private String status;

    public Order(@NonNull String uuid, String userUuid, String dateTime, String totalPrice, String status) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
