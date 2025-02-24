package sube.interviews.mareoenvios.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import sube.interviews.mareoenvios.entity.*;
import sube.interviews.mareoenvios.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShippingServiceTest {

    private ShippingService shippingService;
    private ShippingRepository shippingRepository;
    private ShippingItemRepository shippingItemRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

        productRepository = mock(ProductRepository.class);
        customerRepository = mock(CustomerRepository.class);
        shippingRepository = mock(ShippingRepository.class);
        shippingItemRepository = mock(ShippingItemRepository.class);
        shippingService = new ShippingService(shippingRepository, customerRepository, shippingItemRepository, productRepository);
    }

    @Test
    void getShipping_shouldReturnShippingResponse_whenShippingExists() {
        Long shippingId = 1L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(shipping));
        when(shippingItemRepository.findByShippingId(shippingId)).thenReturn(new ArrayList<>());

        ShippingResponse result = shippingService.getShipping(shippingId);

        Assertions.assertNotNull(result);
        assertEquals(shipping, result.getShipping());
        verify(shippingRepository).findById(shippingId);
        verify(shippingItemRepository).findByShippingId(shippingId);
    }

    @Test
    void getShipping_shouldReturnEmptyResponse_whenShippingNotFound() {
        Long shippingId = 1L;
        when(shippingRepository.findById(shippingId)).thenReturn(Optional.empty());

        ShippingResponse result = shippingService.getShipping(shippingId);

        Assertions.assertNull(result.getShipping());
    }

    @Test
    void getShippingsBetweenDates_shouldReturnShippingResponses() {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now();
        Shipping shipping = new Shipping();
        shipping.setSendDate(LocalDate.now());
        List<Shipping> shippings = List.of(shipping);

        when(shippingRepository.findBySendDateBetween(startDate, endDate)).thenReturn(shippings);

        List<ShippingResponse> result = shippingService.getShippingsBetweenDates(startDate, endDate);

        Assertions.assertNotNull(result);
        assertEquals(1, result.size());
        verify(shippingRepository).findBySendDateBetween(startDate, endDate);
    }

    @Test
    void changeState_shouldChangeShippingState_whenValidTransition() {
        Long shippingId = 1L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.INICIAL);
        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(shipping));
        when(shippingRepository.save(any(Shipping.class))).thenReturn(shipping);
        Shipping result = shippingService.changeState(shippingId, State.ENTREGADO_AL_CORREO);
        Assertions.assertNotNull(result, "El resultado no debe ser null");
        assertEquals(State.ENTREGADO_AL_CORREO, result.getState(), "El estado no ha sido actualizado correctamente");
        verify(shippingRepository).save(any(Shipping.class));
    }

    @Test
    void createShippingRequest_shouldCreateShipping_whenCustomerDoesNotExist() {
        ShippingRequestDTO request = new ShippingRequestDTO();
        request.setCustomerAddress("Address");
        request.setCustomerFirstName("John");
        request.setCustomerLastName("Doe");
        request.setItems(new ArrayList<>());

        when(customerRepository.findByAddress(request.getCustomerAddress())).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer());
        when(shippingRepository.save(any(Shipping.class))).thenReturn(new Shipping());

        ResponseEntity result = shippingService.createShippingRequest(request);

        assertEquals("Shipping Created with id null", result.getBody());
        verify(customerRepository).save(any(Customer.class));
        verify(shippingRepository).save(any(Shipping.class));
    }

    @Test
    void createShippingRequest_shouldThrowException_whenProductNotFound() {
        ShippingRequestDTO request = new ShippingRequestDTO();
        request.setCustomerAddress("Address");
        request.setCustomerFirstName("John");
        request.setCustomerLastName("Doe");

        ShippingItemDTO itemDTO = new ShippingItemDTO();
        itemDTO.setProductId(1L);
        request.setItems(List.of(itemDTO));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> shippingService.createShippingRequest(request));
    }


}

