package com.bookmyshow.strategies;

import com.bookmyshow.models.Show;
import com.bookmyshow.models.ShowSeat;
import java.time.DayOfWeek;

public class WeekendPricingStrategy implements PricingStrategy {
    private static final double SURGE_MULTIPLIER = 1.20;

    @Override
    public double calculatePrice(ShowSeat seat, Show show, double currentComputedPrice) {
        DayOfWeek day = show.getStartTime().getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return currentComputedPrice * SURGE_MULTIPLIER;
        }
        return currentComputedPrice;
    }
}
