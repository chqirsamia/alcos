package com.example.alcos.Model;

public class Produit {
    private String id_produit;
    private String categorie;
    private String code;
    private String nom;
    private String description;
    private String image;
    private String prix;
    private String option;
    private String type;
    private String value;
    private String quantite;
    private String option2;
    private String type2;
    private String value2;

    public Produit(String id_produit, String categorie, String code, String nom, String description
            , String image, String prix, String option,
                   String type, String value, String quantite, String option2, String type2, String value2) {
        this.id_produit = id_produit;
        this.categorie = categorie;
        this.code = code;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.prix = prix;
        this.option = option;
        this.type = type;
        this.value = value;
        this.quantite = quantite;
        this.option2 = option2;
        this.type2 = type2;
        this.value2 = value2;
    }

    public Produit(){}
    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom_produit) {
        this.nom = nom_produit;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix_unitaire) {
        this.prix = prix_unitaire;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }



}
