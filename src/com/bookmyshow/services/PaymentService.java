package com.bookmyshow.services;

import com.bookmyshow.enums.BookingStatus;
import com.bookmyshow.enums.PaymentMode;
import com.bookmyshow.enums.PaymentStatus;
import com.bookmyshow.enums.SeatStatus;
import com.bookmyshow.models.Booking;
import com.bookmyshow.models.Payment;
import com.bookmyshow.models.ShowSeat;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentService {
    
    public Payment processPayment(Booking booking, PaymentMode mode) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in PENDING state. Cannot process payment.");
        }

        // Verify that seats haven't expired
        for (ShowSeat seat : booking.getBookedSeats()) {
            if (seat.getLockExpiryTime().isBefore(LocalDateTime.now())) {
                revertExpiredBooking(booking);
                throw new IllegalStateException("Payment time expired. Seats have been freed.");
            }
        }

        // Simulate third-party payment gateway delay
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment(paymentId, booking, booking.getTotalAmount(), mode);
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        payment.setStatus(PaymentStatus.SUCCESS);
        booking.addPayment(payment);

        // Mark the booking as Confirmed
        booking.setStatus(BookingStatus.CONFIRMED);
        
        // Mark seats as BOOKED permanently
        for (ShowSeat seat : booking.getBookedSeats()) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedByUserId(null); // No longer needed
            seat.setLockExpiryTime(null);
        }

        return payment;
    }

    private void revertExpiredBooking(Booking booking) {
        booking.setStatus(BookingStatus.EXPIRED);
        for (ShowSeat seat : booking.getBookedSeats()) {
            if (seat.getStatus() == SeatStatus.BLOCKED && seat.getLockedByUserId().equals(booking.getCustomer().getId())) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedByUserId(null);
                seat.setLockExpiryTime(null);
            }
        }
    }
}
