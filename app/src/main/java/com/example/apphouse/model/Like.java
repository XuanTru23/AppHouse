package com.example.apphouse.model;

public class Like {
    String title_like;
    String name;
    String sdt;
    String id_like;
    String dia_chi;
    String gia;
    String id_nguoi_dang;

    public Like() {

    }

    public Like(String title_like, String name, String sdt, String id_like, String dia_chi, String gia, String id_nguoi_dang) {
        this.title_like = title_like;
        this.name = name;
        this.sdt = sdt;
        this.id_like = id_like;
        this.dia_chi = dia_chi;
        this.gia = gia;
        this.id_nguoi_dang = id_nguoi_dang;
    }

    public String getId_nguoi_dang() {
        return id_nguoi_dang;
    }

    public void setId_nguoi_dang(String id_nguoi_dang) {
        this.id_nguoi_dang = id_nguoi_dang;
    }

    public String getTitle_like() {
        return title_like;
    }

    public void setTitle_like(String title_like) {
        this.title_like = title_like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getId_like() {
        return id_like;
    }

    public void setId_like(String id_like) {
        this.id_like = id_like;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }
}
