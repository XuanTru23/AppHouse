package com.example.apphouse.model;

public class House {
    String idHouse;
    String idUser;
    String title;
    String address;
    String price;
    String bed;
    String bath;
    String wifi;
    String typeHouse;
    String description;
    String imageUrl;
    String idLikeHouse;

    public House(){}


    public House(String idHouse, String idUser, String title, String address, String price, String bed, String bath, String wifi, String typeHouse, String description, String imageUrl, String idLikeHouse) {
        this.idHouse = idHouse;
        this.idUser = idUser;
        this.title = title;
        this.address = address;
        this.price = price;
        this.bed = bed;
        this.bath = bath;
        this.wifi = wifi;
        this.typeHouse = typeHouse;
        this.description = description;
        this.imageUrl = imageUrl;
        this.idLikeHouse = idLikeHouse;
    }

    public String getIdLikeHouse() {
        return idLikeHouse;
    }

    public void setIdLikeHouse(String idLikeHouse) {
        this.idLikeHouse = idLikeHouse;
    }

    public String getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(String idHouse) {
        this.idHouse = idHouse;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getBath() {
        return bath;
    }

    public void setBath(String bath) {
        this.bath = bath;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getTypeHouse() {
        return typeHouse;
    }

    public void setTypeHouse(String typeHouse) {
        this.typeHouse = typeHouse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
