package com.bookmyshow.strategies;

import com.bookmyshow.models.Show;
import com.bookmyshow.models.ShowSeat;

// A simple example for a dynamically added/removed pricing rule
public class FlashSalePricingStrategy implements PricingStrategy {
    private static final double DISCOUNT_MULTIPLIER = 0.80;
    
    @Override
    public double calculatePrice(ShowSeat seat, Show show, double currentComputedPrice) {
        // e.g., if a global flag says flash sale is active
        // for simplicity, we mock an active sale:
        return currentComputedPrice * DISCOUNT_MULTIPLIER;
    }
}
