package sube.interviews.mareoenvios.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCustomer_found() throws Exception {
        Long id = 1L;
        Customer customer = new Customer(id, "Belen", "Pérez", "Calle Falsa 123", "Ciudad");
        when(customerService.getCustomer(id)).thenReturn(customer);

        mockMvc.perform(get("/customer/info/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Belen"))
                .andExpect(jsonPath("$.lastName").value("Pérez"));
    }

    @Test
    void getCustomer_notFound() throws Exception {
        Long id = 2L;
        when(customerService.getCustomer(id)).thenReturn(null);

        mockMvc.perform(get("/customer/info/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getCustomers_returnsList() throws Exception {
        Customer c1 = new Customer(1L, "Belen", "Pérez", "Calle Falsa 123", "Ciudad");
        Customer c2 = new Customer(2L, "Ana", "García", "Avenida Siempre Viva", "Ciudad2");
        when(customerService.getCustomers()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/customer/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Belen"))
                .andExpect(jsonPath("$[1].firstName").value("Ana"));
    }
}
