package com.bookmyshow.services;

import com.bookmyshow.enums.BookingStatus;
import com.bookmyshow.models.Booking;
import com.bookmyshow.models.Movie;
import com.bookmyshow.models.Show;
import com.bookmyshow.models.Theatre;

import java.util.List;

public class AdminService {
    
    // In memory store references (Ideally injected DAOs)
    private List<Show> activeShows;
    private List<Booking> allBookings;

    public AdminService(List<Show> activeShows, List<Booking> allBookings) {
        this.activeShows = activeShows;
        this.allBookings = allBookings;
    }

    public void removeMovie(Movie movie) {
        System.out.println("Admin triggered remove for Movie: " + movie.getTitle());
        
        // 1. Soft Delete
        movie.setActive(false);
        System.out.println("Movie Soft Deleted.");

        // 2. Cascading Cancellation for Future Shows
        for (Show show : activeShows) {
            if (show.getMovie().getId().equals(movie.getId())) {
                System.out.println("Found future show: " + show.getId() + ". Cancelling...");
                cancelShowAndRefund(show);
            }
        }
    }

    private void cancelShowAndRefund(Show show) {
        for (Booking booking : allBookings) {
            if (booking.getShow().getId().equals(show.getId()) && booking.getStatus() == BookingStatus.CONFIRMED) {
                // 3. Async Refund trigger (Simulated)
                booking.setStatus(BookingStatus.CANCELLED);
                System.out.println("Triggered Async Refund for Booking: " + booking.getId() + " to customer " + booking.getCustomer().getName());
            }
        }
    }
}
