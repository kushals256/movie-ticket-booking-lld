package com.bookmyshow.models;

import com.bookmyshow.enums.ScreenType;

public class Screen {
    private String id;
    private Theatre theatre;
    private String name;
    private ScreenType type;

    public Screen(String id, Theatre theatre, String name, ScreenType type) {
        this.id = id;
        this.theatre = theatre;
        this.name = name;
        this.type = type;
    }

    public String getId() { return id; }
    public Theatre getTheatre() { return theatre; }
    public String getName() { return name; }
    public ScreenType getType() { return type; }
}
