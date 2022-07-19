package com.example.ppp_a167536_2.Interface;

import com.example.ppp_a167536_2.Models.ItemGroup;

import java.util.List;

public interface IFirebaseLoadListener {
    void onFirebaseLoadSuccess (List<ItemGroup> itemGroupList);
    void onFirebaseLoadFailure (String message);
}
