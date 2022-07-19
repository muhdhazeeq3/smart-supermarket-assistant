package com.example.ppp_a167536_2.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Barang {

    public String iname;
    public double iprice;
    public String icategory;
    public String iimage;
    public String ibarcodeid;
    public int iquantity;
    public String ilocation;

    public Barang(){ }

    public String getIlocation() {
        return ilocation;
    }

    public void setIlocation(String ilocation) {
        this.ilocation = ilocation;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public void setIprice(double iprice) {
        this.iprice = iprice;
    }

    public void setIquantity(int iquantity) {
        this.iquantity = iquantity;
    }

    public String getIname() {
        return iname;
    }

    public double getIprice() {
        return iprice;
    }

    public int getIquantity() {
        return iquantity;
    }

    public String getIimage() {
        return iimage;
    }

    public void setIimage(String iimage) {
        this.iimage = iimage;
    }

    public Barang(String iname, double iprice){
        this.iname = iname;
        this.iprice = iprice;
    }

    public Barang(String iname, double iprice, int iquantity){
        this.iname = iname;
        this.iprice = iprice;
        this.iquantity = iquantity;
    }

    public Barang(String ibarcodeid, String iimage, String icategory, String iname, double iprice){
        this.ibarcodeid = ibarcodeid;
        this.iimage = iimage;
        this.icategory = icategory;
        this.iname = iname;
        this.iprice = iprice;
    }
}
