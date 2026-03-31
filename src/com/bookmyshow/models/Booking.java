package com.bookmyshow.models;

import com.bookmyshow.enums.BookingStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private String id;
    private Customer customer;
    private Show show;
    private List<ShowSeat> bookedSeats;
    private double totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private List<Payment> payments;

    public Booking(String id, Customer customer, Show show, List<ShowSeat> bookedSeats, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.payments = new ArrayList<>();
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Show getShow() { return show; }
    public List<ShowSeat> getBookedSeats() { return bookedSeats; }
    public double getTotalAmount() { return totalAmount; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Payment> getPayments() { return payments; }
    public void addPayment(Payment payment) { this.payments.add(payment); }
}
