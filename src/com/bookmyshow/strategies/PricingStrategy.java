package com.bookmyshow.strategies;

import com.bookmyshow.models.ShowSeat;
import com.bookmyshow.models.Show;

public interface PricingStrategy {
    double calculatePrice(ShowSeat seat, Show show, double currentComputedPrice);
}
