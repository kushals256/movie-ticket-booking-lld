package com.bookmyshow.models;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Booking> bookings;

    public Customer(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.bookings = new ArrayList<>();
    }

    public List<Booking> getBookings() {
        return bookings;
    }
    
    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }
}
