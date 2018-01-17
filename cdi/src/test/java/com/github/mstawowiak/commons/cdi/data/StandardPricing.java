package com.github.mstawowiak.commons.cdi.data;

import java.math.BigDecimal;

@Standard
public class StandardPricing implements Pricing {

    public BigDecimal cost() {
        return BigDecimal.ONE;
    }
}
