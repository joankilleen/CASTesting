package org.musicstore.domain;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.musicstore.domain.entities.MusicOrder;
import org.musicstore.domain.repositories.MusicOrderRepository;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.anyString;

@RunWith(CdiRunner.class)
public class PriceCalculatorTest {

    @Inject
    PriceCalculator priceCalculator;

    @Produces
    @ProducesAlternative
    @Mock
    MusicOrderRepository orderRepositoryMock; // CDI-Unit/Mockito will create a mock

    @Test
    public void shouldApplyStandardDiscount() throws Exception {

        Double totalAmount = 10d;

        // Set up stubs
        MusicOrder order = mock(MusicOrder.class);
        when(order.getEmail()).thenReturn("my_email");
        when(order.getTotalAmount()).thenReturn(totalAmount);

        when(orderRepositoryMock.getOrdersByEmail(anyString())).thenReturn(new ArrayList<MusicOrder>());

        // Trigger the actual test
        Double calculatedPrice = priceCalculator.calculatePrice(order);

        // We check the final result of price calculation
        assertThat(calculatedPrice, is(totalAmount * PriceCalculator.STANDARD_DISCOUNT_FACTOR));
    }

    @Test
    public void shouldApplyExtraDiscount() throws Exception {

        Double totalAmount = 10d;

        // TODO: Implement the business logic and test
        Double calculatedPrice = 8d;

        // We check the final result of price calculation
        assertEquals(new Double(totalAmount * PriceCalculator.EXTRA_DISCOUNT_FACTOR), calculatedPrice);
    }

}
