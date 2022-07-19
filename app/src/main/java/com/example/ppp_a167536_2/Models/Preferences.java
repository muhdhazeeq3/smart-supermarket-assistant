package com.example.ppp_a167536_2.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Preferences {
    public String language;

    public Preferences() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Preferences(String language) {
        this.language = language;
    }

}
