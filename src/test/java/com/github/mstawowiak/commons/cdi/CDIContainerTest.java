package com.github.mstawowiak.commons.cdi;

import com.github.mstawowiak.commons.cdi.data.Premium;
import com.github.mstawowiak.commons.cdi.data.PremiumPricing;
import com.github.mstawowiak.commons.cdi.data.Pricing;
import com.github.mstawowiak.commons.cdi.data.Standard;
import com.github.mstawowiak.commons.cdi.data.StandardPricing;
import com.github.mstawowiak.commons.cdi.data.TestBean;
import java.math.BigDecimal;
import javax.enterprise.util.TypeLiteral;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for {@link CDIContainer}
 */
public class CDIContainerTest {

    @Rule
    public WeldInitiator weld = WeldInitiator.of(TestBean.class, StandardPricing.class, PremiumPricing.class);

    @Test
    public void shouldSelectFromCdiContainer() {
        TestBean bean = CDIContainer.select(TestBean.class);

        Assert.assertNotNull(bean);
        Assert.assertEquals("INITIALIZED", bean.getStatus());
    }

    @Test
    public void shouldSelectFromCdiContainerByTypeLiteral() {
        TestBean bean = CDIContainer.select(new TypeLiteral<TestBean>(){});

        Assert.assertNotNull(bean);
        Assert.assertEquals("INITIALIZED", bean.getStatus());
    }

    @Test
    public void shouldSelectFromCdiContainerWithQualifier() {
        Pricing standardPricing = CDIContainer.select(Pricing.class, Standard.Literal.INSTANCE);

        Assert.assertNotNull(standardPricing);
        Assert.assertEquals(BigDecimal.ONE, standardPricing.cost());

        Pricing premiumPricing = CDIContainer.select(Pricing.class, Premium.Literal.INSTANCE);

        Assert.assertNotNull(premiumPricing);
        Assert.assertEquals(BigDecimal.TEN, premiumPricing.cost());
    }

    @Test
    public void shouldSelectFromCdiContainerWithQualifierByTypeLiteral() {
        Pricing standardPricing = CDIContainer.select(new TypeLiteral<Pricing>(){}, Standard.Literal.INSTANCE);

        Assert.assertNotNull(standardPricing);
        Assert.assertEquals(BigDecimal.ONE, standardPricing.cost());

        Pricing premiumPricing = CDIContainer.select(new TypeLiteral<Pricing>(){}, Premium.Literal.INSTANCE);

        Assert.assertNotNull(premiumPricing);
        Assert.assertEquals(BigDecimal.TEN, premiumPricing.cost());
    }

}
