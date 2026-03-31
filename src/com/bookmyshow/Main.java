package com.bookmyshow;

import com.bookmyshow.enums.*;
import com.bookmyshow.exceptions.SeatTemporarilyUnavailableException;
import com.bookmyshow.models.*;
import com.bookmyshow.services.*;
import com.bookmyshow.strategies.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Booting BookMyShow System ---");

        // 1. Initialize core system components
        PriceCalculationEngine pricingEngine = new PriceCalculationEngine();
        pricingEngine.addStrategy(new WeekendPricingStrategy()); // 20% Surge on weekends

        BookingService bookingService = new BookingService(pricingEngine);
        PaymentService paymentService = new PaymentService();

        // 2. Setup dummy data
        City bangalore = new City("C1", "Bangalore", "Karnataka");
        Theatre theatre = new Theatre("T1", "PVR Cinemas", bangalore);
        Screen screen1 = new Screen("SC1", theatre, "Screen 1 IMAX", ScreenType.IMAX);
        
        Seat a1 = new Seat("S-A1", screen1, 1, 1);
        Seat a2 = new Seat("S-A2", screen1, 1, 2);

        Movie avengers = new Movie("M1", "Avengers: Endgame", "English", "Action", 180);
        
        // Target show is on a Saturday (To trigger Weekend Pricing)
        LocalDateTime targetTime = LocalDateTime.of(2026, 4, 18, 18, 0); // April 18, 2026 is Saturday
        Show eveningShow = new Show("SH1", avengers, screen1, targetTime, targetTime.plusHours(3));

        ShowSeat showSeatA1 = new ShowSeat("SS1", eveningShow, a1, SeatType.DIAMOND, 500.0);
        ShowSeat showSeatA2 = new ShowSeat("SS2", eveningShow, a2, SeatType.DIAMOND, 500.0);

        Customer alice = new Customer("U1", "Alice", "alice@email.com", "9999999999");
        Customer bob = new Customer("U2", "Bob", "bob@email.com", "8888888888");

        System.out.println("--- Setup Complete --- \n");
        System.out.println("Attempting Concurrency Test: Alice and Bob both trying to book Seat A1 and A2 precisely at the same millisecond...\n");

        // 3. Concurrency Simulation (Racing condition on pessimistic lock)
        Runnable aliceTask = () -> {
            try {
                System.out.println("[Thread-Alice] Attempting to book...");
                Booking b = bookingService.createBooking(alice, eveningShow, Arrays.asList(showSeatA1, showSeatA2));
                System.out.println("[Thread-Alice] Booking created successfully! Status: " + b.getStatus() + ", Total Price: " + b.getTotalAmount());
                
                System.out.println("[Thread-Alice] Processing Payment...");
                paymentService.processPayment(b, PaymentMode.UPI);
                System.out.println("[Thread-Alice] Payment Success! Final Booking Status: " + b.getStatus());
                
            } catch (SeatTemporarilyUnavailableException e) {
                System.out.println("[Thread-Alice] FAILED: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Thread-Alice] ERROR: " + e.getMessage());
            }
        };

        Runnable bobTask = () -> {
            try {
                System.out.println("[Thread-Bob] Attempting to book...");
                Booking b = bookingService.createBooking(bob, eveningShow, Arrays.asList(showSeatA1, showSeatA2));
                System.out.println("[Thread-Bob] Booking created successfully! Status: " + b.getStatus());
            } catch (SeatTemporarilyUnavailableException e) {
                System.out.println("[Thread-Bob] FAILED: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Thread-Bob] ERROR: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(aliceTask);
        Thread t2 = new Thread(bobTask);

        // Start exactly together
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        
        System.out.println("\n--- Concurrency Test Finished ---");
        System.out.println("Final state of ShowSeat A1: " + showSeatA1.getStatus());
        System.out.println("Final state of ShowSeat A2: " + showSeatA2.getStatus());
        
        System.out.println("\n(Base Price was 500 x 2 = 1000. Note the dynamic surge pricing computed via Strategy pattern applied to Alice's success response)");
    }
}
