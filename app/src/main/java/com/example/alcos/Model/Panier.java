package com.example.alcos.Model;

public class Panier {
    private String pid;
    private String name;
    private String total;
    private String image;
    private String price;
    private String qty;
    private String value;
    private String value2;
    private String type;
    private String type2;
    private String option2;
    private String option;
    private String radioid;
    private String radioid2;
    public Panier() {
    }

    public String getRadioid() {
        return radioid;
    }

    public void setRadioid(String radioid) {
        this.radioid = radioid;
    }

    public String getRadioid2() {
        return radioid2;
    }

    public void setRadioid2(String radioid2) {
        this.radioid2 = radioid2;
    }

    public Panier(String pid, String name, String total, String image, String price, String qty, String value, String value2, String type, String type2, String option2, String option, String radioid, String radioid2) {
        this.pid = pid;
        this.name = name;
        this.total = total;
        this.image = image;
        this.price = price;
        this.qty = qty;
        this.value = value;
        this.value2 = value2;
        this.type = type;
        this.type2 = type2;
        this.option2 = option2;
        this.option = option;
        this.radioid = radioid;
        this.radioid2 = radioid2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
