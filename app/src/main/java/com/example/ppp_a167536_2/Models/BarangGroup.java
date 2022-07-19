package com.example.ppp_a167536_2.Models;

import java.util.ArrayList;

public class BarangGroup {
    private ArrayList<Barang> listCart;

    public BarangGroup() {
    }

    public BarangGroup(ArrayList<Barang> listCart) {
        this.listCart = listCart;
    }

    public ArrayList<Barang> getListItem() {
        return listCart;
    }

    public void setListCart(ArrayList<Barang> listCart) {
        this.listCart = listCart;
    }
}