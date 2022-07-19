package com.example.ppp_a167536_2.Interface;

import com.example.ppp_a167536_2.Models.Barang;

import java.util.List;

public interface IFirebaseCartListener {
    void onFirebaseLoadSuccess (List<Barang> itemGroupList);
    void onFirebaseLoadFailure (String message);
}