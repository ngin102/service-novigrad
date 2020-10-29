package com.example.a2105projectgroup13;

public class Service {
    private String name;
    private String price;

    public Service(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public Double getPrice() { return Double.parseDouble(price); }
}
