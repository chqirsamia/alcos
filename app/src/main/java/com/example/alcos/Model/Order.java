package com.example.alcos.Model;

public class Order {
    String total;
    String name;
    String prenom;
    String date;
    String time;
    String adresse;
    String email;
    String phone;
    String etat;
    String uid;
    String id;
    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order(String total, String name, String prenom, String date, String time, String adresse, String email, String phone, String etat, String uid, String id) {
        this.total = total;
        this.name = name;
        this.prenom = prenom;
        this.date = date;
        this.time = time;
        this.adresse = adresse;
        this.email = email;
        this.phone = phone;
        this.etat = etat;
        this.uid = uid;
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
