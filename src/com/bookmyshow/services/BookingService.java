package com.bookmyshow.services;

import com.bookmyshow.enums.BookingStatus;
import com.bookmyshow.enums.SeatStatus;
import com.bookmyshow.exceptions.SeatTemporarilyUnavailableException;
import com.bookmyshow.models.Booking;
import com.bookmyshow.models.Customer;
import com.bookmyshow.models.Show;
import com.bookmyshow.models.ShowSeat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private PriceCalculationEngine pricingEngine;
    
    // In-memory store to simulate database
    private List<Booking> bookingsDB = new ArrayList<>();

    public BookingService(PriceCalculationEngine pricingEngine) {
        this.pricingEngine = pricingEngine;
    }

    public Booking createBooking(Customer customer, Show show, List<ShowSeat> requestedSeats) {
        List<ShowSeat> lockedSeats = new ArrayList<>();

        for (ShowSeat seat : requestedSeats) {
            // Attempt to acquire DB Row Lock using pessimistic locking simulation
            if (seat.acquireRowLock()) {
                lockedSeats.add(seat);
                
                // If it's locked but not AVAILABLE (e.g., someone else locked it right before us)
                if (seat.getStatus() != SeatStatus.AVAILABLE) {
                    rollbackLocks(lockedSeats);
                    throw new SeatTemporarilyUnavailableException("Seat " + seat.getSeat().getId() + " is already booked or blocked.");
                }
            } else {
                // Failed to acquire lock
                rollbackLocks(lockedSeats);
                throw new SeatTemporarilyUnavailableException("System busy: Could not lock seat " + seat.getSeat().getId() + ".");
            }
        }

        // --- Critical Section Start: At this point, the current thread owns all locks for requestedSeats ---
        
        double totalAmount = 0.0;
        for (ShowSeat seat : lockedSeats) {
            seat.setStatus(SeatStatus.BLOCKED);
            seat.setLockedByUserId(customer.getId());
            seat.setLockExpiryTime(LocalDateTime.now().plusMinutes(5)); // TTL for 5 minutes
            
            // Calculate final price dynamically before booking
            pricingEngine.calculateAndSetFinalPrice(seat, show);
            totalAmount += seat.getFinalPrice();
        }
        
        // --- Critical Section End: We can release row locks now that status is safely changed ---
        releaseLocks(lockedSeats);

        // Create the booking object
        String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking newBooking = new Booking(bookingId, customer, show, requestedSeats, totalAmount);
        newBooking.setStatus(BookingStatus.PENDING);
        
        customer.addBooking(newBooking);
        bookingsDB.add(newBooking);

        return newBooking;
    }

    private void rollbackLocks(List<ShowSeat> lockedSeats) {
        for (ShowSeat seat : lockedSeats) {
            seat.releaseRowLock();
        }
    }

    private void releaseLocks(List<ShowSeat> lockedSeats) {
        for (ShowSeat seat : lockedSeats) {
            seat.releaseRowLock();
        }
    }
}
