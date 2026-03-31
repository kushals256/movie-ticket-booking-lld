package com.bookmyshow.services;

import com.bookmyshow.models.Show;
import com.bookmyshow.models.ShowSeat;
import com.bookmyshow.strategies.PricingStrategy;

import java.util.ArrayList;
import java.util.List;

public class PriceCalculationEngine {
    private List<PricingStrategy> strategies;

    public PriceCalculationEngine() {
        this.strategies = new ArrayList<>();
    }

    public void addStrategy(PricingStrategy strategy) {
        this.strategies.add(strategy);
    }

    public void calculateAndSetFinalPrice(ShowSeat seat, Show show) {
        double currentPrice = seat.getBasePrice();
        
        for (PricingStrategy strategy : strategies) {
            currentPrice = strategy.calculatePrice(seat, show, currentPrice);
        }
        
        seat.setFinalPrice(currentPrice);
    }
}
