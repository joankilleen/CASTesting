package org.musicstore.domain;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.musicstore.domain.entities.Album;
import org.musicstore.domain.entities.MusicOrder;
import org.musicstore.domain.repositories.MusicOrderRepository;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(CdiRunner.class)
public class OrderServiceTest {

    @Inject
    OrderServiceBean orderService;

    @Produces
    @ProducesAlternative
    @Mock
    PriceCalculator priceCalculatorMock;

    @Produces
    @ProducesAlternative
    @Mock
    MusicOrderRepository mockOrderRepository; // This is a stub. We need the entityManager so that all dependencies can be resolved


    @Test
    public void finalAmountShouldBeSetAfterAddingAlbums(){

        Double expectedPrice = 10d;

        // Set up test
        when(priceCalculatorMock.calculatePrice(any(MusicOrder.class))).thenReturn(expectedPrice);
        MusicOrder musicOrder = new MusicOrder(); // use a real entity

        orderService.setCurrentOrder(musicOrder);

        // Trigger actual test
        orderService.addAlbums(new ArrayList<Album>());

        // Verify: Inspect the state at the end of the test
        assertThat(orderService.getCurrentOrder().getFinalAmount(), is(10d));
    }


    @Test
    public void shouldSetFinalAmountWhenOrderIsCreated(){

        Double expectedPrice = 10d;

        // Set up test
        when(priceCalculatorMock.calculatePrice(any(MusicOrder.class))).thenReturn(expectedPrice);
        MusicOrder musicOrderMock = mock(MusicOrder.class); // entities can also be mocked

        orderService.setCurrentOrder(musicOrderMock);

        // Trigger actual test
        orderService.addAlbums(new ArrayList<Album>());

        // Verify: Ensure interactions between collaborators
        verify(priceCalculatorMock).calculatePrice(any(MusicOrder.class));
        verify(musicOrderMock).setFinalAmount(expectedPrice);
    }
}
