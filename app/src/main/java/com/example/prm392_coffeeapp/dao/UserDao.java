package com.example.prm392_coffeeapp.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_coffeeapp.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User getUserByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM users WHERE role = :role")
    User[] getUsersByRole(String role);

    @Query("SELECT * FROM users WHERE uuid = :uuid")
    User getUserByUuid(String uuid);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    @Query("DELETE FROM users WHERE uuid = :uuid")
    void deleteUserByUuid(String uuid);

    @Query("UPDATE users SET username = :username, password = :password, role = :role WHERE uuid = :uuid")
    void updateUser(String uuid, String username, String password, String role);



}
