
package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricingRatesManager {

    private Map<ChargingLocation, List<PricingRates>> pricingRates;

    public PricingRatesManager() {
        this.pricingRates = new HashMap<>();
    }

    public PricingRatesManager createPricingRates(
            ChargingLocation location, PricingRates rates) {

        if (location == null) {
            throw new IllegalArgumentException("Unknown Charging Location");
        }

        if (!pricingRates.containsKey(location)) {
            pricingRates.put(location, new ArrayList<>());
        }

        pricingRates.get(location).add(rates);

        return this;
    }

    public PricingRates getCurrentPricingRates(ChargingLocation location) {

        return getPricingRatesAt(location, LocalDateTime.now());
    }

    public PricingRates getPricingRatesAt(
            ChargingLocation location, LocalDateTime date) {

        if (location == null) {
            throw new IllegalArgumentException("Unknown Charging Location");
        }

        List<PricingRates> rates = pricingRates.get(location);

        PricingRates validRates = null;

        if (rates != null) {
            for (PricingRates rate : rates) {

                if (!rate.getValidFrom().isAfter(date)) {

                    if (validRates == null
                            || rate.getValidFrom().isAfter(validRates.getValidFrom())) {

                        validRates = rate;
                    }
                }
            }
        }

        if (validRates == null) {
            throw new IllegalArgumentException("No valid Pricing Rates");
        }

        return validRates;
    }

    @Override
    public String toString() {
        return this.pricingRates.toString();
    }
}
