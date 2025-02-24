package sube.interviews.mareoenvios.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import sube.interviews.mareoenvios.entity.*;
import sube.interviews.mareoenvios.repository.*;
import sube.interviews.mareoenvios.service.ShippingService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = {"/schema.sql", "/data.sql"})
class ShippingServiceIntegrationTest {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShippingItemRepository shippingItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void changeState_shouldUpdateState_whenValidTransition() {
        Long shippingId = 1L;
        Shipping shipping = shippingRepository.findById(shippingId).orElseThrow();

        assertNotNull(shipping);
        assertEquals(State.INICIAL, shipping.getState());

        Shipping updatedShipping = shippingService.changeState(shippingId, State.ENTREGADO_AL_CORREO);

        assertNotNull(updatedShipping);
        assertEquals(State.ENTREGADO_AL_CORREO, updatedShipping.getState());
    }

    @Test
    void getShipping_shouldReturnShippingDetails() {
        Long shippingId = 1L;
        ShippingResponse response = shippingService.getShipping(shippingId);

        assertNotNull(response);
        assertNotNull(response.getShipping());
        assertEquals(shippingId, response.getShipping().getId());
        assertEquals(1, response.getShippingItems().size());
    }

    @Test
    void getShippingsBetweenDates_shouldReturnShippings() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        List<ShippingResponse> shippings = shippingService.getShippingsBetweenDates(startDate, endDate);

        assertNotNull(shippings);
        assertFalse(shippings.isEmpty());
    }

    @Test
    void createShippingRequest_shouldCreateShipping() {
        ShippingRequestDTO request = new ShippingRequestDTO();
        request.setCustomerAddress("456 Elm St");
        request.setCustomerFirstName("Jane");
        request.setCustomerLastName("Smith");
        request.setItems(List.of(
                new ShippingItemDTO(1L, 3),
                new ShippingItemDTO(2L, 2)
        ));

        ResponseEntity response = shippingService.createShippingRequest(request);

        assertEquals("Shipping Created with id 2", response.getBody());
    }
}

