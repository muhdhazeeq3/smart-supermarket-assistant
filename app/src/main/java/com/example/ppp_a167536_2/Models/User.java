package com.example.ppp_a167536_2.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String name;
    public String phone;
    public String gender;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, String name, String phone, String gender) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

}

