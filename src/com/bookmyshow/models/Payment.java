package com.bookmyshow.models;

import com.bookmyshow.enums.PaymentMode;
import com.bookmyshow.enums.PaymentStatus;

public class Payment {
    private String id;
    private Booking booking;
    private double amount;
    private PaymentMode method;
    private PaymentStatus status;
    private String transactionId;

    public Payment(String id, Booking booking, double amount, PaymentMode method) {
        this.id = id;
        this.booking = booking;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
    }

    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getId() { return id; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
    public PaymentMode getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
}
