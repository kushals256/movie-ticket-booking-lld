package com.bookmyshow.models;

import com.bookmyshow.enums.SeatStatus;
import com.bookmyshow.enums.SeatType;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class ShowSeat {
    private String id;
    private Show show;
    private Seat seat;
    private SeatType seatType;
    private double basePrice;
    private double finalPrice;
    private SeatStatus status;
    private LocalDateTime lockExpiryTime;
    private String lockedByUserId;
    
    // Simulate Row-level Pessimistic Database Lock
    private final ReentrantLock dbLock = new ReentrantLock();

    public ShowSeat(String id, Show show, Seat seat, SeatType seatType, double basePrice) {
        this.id = id;
        this.show = show;
        this.seat = seat;
        this.seatType = seatType;
        this.basePrice = basePrice;
        this.finalPrice = basePrice; // default without strategies applied
        this.status = SeatStatus.AVAILABLE;
    }

    public boolean acquireRowLock() {
        return dbLock.tryLock();
    }
    
    public void releaseRowLock() {
        dbLock.unlock();
    }

    public String getId() { return id; }
    public Show getShow() { return show; }
    public Seat getSeat() { return seat; }
    public SeatType getSeatType() { return seatType; }
    public double getBasePrice() { return basePrice; }
    
    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    public LocalDateTime getLockExpiryTime() { return lockExpiryTime; }
    public void setLockExpiryTime(LocalDateTime lockExpiryTime) { this.lockExpiryTime = lockExpiryTime; }

    public String getLockedByUserId() { return lockedByUserId; }
    public void setLockedByUserId(String lockedByUserId) { this.lockedByUserId = lockedByUserId; }
}
