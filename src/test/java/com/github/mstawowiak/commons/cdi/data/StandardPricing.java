/*
 * This code is unpublished proprietary trade secret of
 * Visiona Sp. z o.o., ul. Życzkowskiego 14, 31-864 Kraków, Poland.
 *
 * This code is protected under Act on Copyright and Related Rights
 * and may be used only under the terms of license granted by
 * Visiona Sp. z o.o., ul. Życzkowskiego 14, 31-864 Kraków, Poland.
 *
 * Above notice must be preserved in all copies of this code.
 */

package com.github.mstawowiak.commons.cdi.data;

import java.math.BigDecimal;

@Standard
public class StandardPricing implements Pricing {

    public BigDecimal cost() {
        return BigDecimal.ONE;
    }
}
