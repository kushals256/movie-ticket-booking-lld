package com.bookmyshow.models;

public class Theatre {
    private String id;
    private String name;
    private City city;
    private boolean isActive;

    public Theatre(String id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.isActive = true;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public City getCity() { return city; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
