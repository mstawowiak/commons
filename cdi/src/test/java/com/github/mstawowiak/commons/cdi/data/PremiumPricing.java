package com.github.mstawowiak.commons.cdi.data;

import java.math.BigDecimal;

@Premium
public class PremiumPricing implements Pricing {

    public BigDecimal cost() {
        return BigDecimal.TEN;
    }
}
