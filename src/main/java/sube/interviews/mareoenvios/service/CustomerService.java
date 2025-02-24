package sube.interviews.mareoenvios.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Customer getCustomer(Long id) {
        logger.info("Buscando customer con id: {}", id);
        return customerRepository.findById(id)
                .orElseGet(() -> {
                    logger.warn("Customer con id {} no encontrado", id);
                    return null;
                });
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


}
