package sube.interviews.mareoenvios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.repository.CustomerRepository;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void getCustomer_found() {
        Long id = 1L;
        Customer customer = new Customer(id, "Juan", "Pérez", "Calle Falsa 123", "Ciudad");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        Customer result = customerService.getCustomer(id);
        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void getCustomer_notFound() {
        Long id = 2L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        Customer result = customerService.getCustomer(id);
        assertNull(result);
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void getCustomers_returnsList() {
        Customer c1 = new Customer(1L, "Juan", "Pérez", "Calle Falsa 123", "Ciudad");
        Customer c2 = new Customer(2L, "Ana", "García", "Avenida Siempre Viva", "Ciudad2");
        when(customerRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
        List<Customer> customers = customerService.getCustomers();
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findAll();
    }
}
