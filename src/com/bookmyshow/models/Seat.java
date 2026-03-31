package com.bookmyshow.models;

public class Seat {
    private String id;
    private Screen screen;
    private int rowNumber;
    private int seatNumber;

    public Seat(String id, Screen screen, int rowNumber, int seatNumber) {
        this.id = id;
        this.screen = screen;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public String getId() { return id; }
    public Screen getScreen() { return screen; }
    public int getRowNumber() { return rowNumber; }
    public int getSeatNumber() { return seatNumber; }
}
