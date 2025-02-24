package sube.interviews.mareoenvios.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import sube.interviews.mareoenvios.entity.*;
import sube.interviews.mareoenvios.repository.CustomerRepository;
import sube.interviews.mareoenvios.repository.ProductRepository;
import sube.interviews.mareoenvios.repository.ShippingItemRepository;
import sube.interviews.mareoenvios.repository.ShippingRepository;

import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final ShippingItemRepository shippingItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ShippingService.class);

    public ShippingService(ShippingRepository shippingRepository, CustomerRepository customerRepository, ShippingItemRepository shippingItemRepository, ProductRepository productRepository) {
        this.shippingRepository = shippingRepository;
        this.customerRepository = customerRepository;
        this.shippingItemRepository = shippingItemRepository;
        this.productRepository = productRepository;

    }

    @Cacheable(value = "shipping", key = "#shippingId")
    public ShippingResponse getShipping(Long shippingId) {
        ShippingResponse shippingResponse = new ShippingResponse();
        Shipping shipping = shippingRepository.findById(shippingId).orElse(null);
        shippingResponse.setShipping(shipping);
        shippingResponse.setShippingItems(createShippingItemResponses(shippingItemRepository.findByShippingId(shippingId)));

        return shippingResponse;

    }


    public List<ShippingResponse> getShippingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Shipping> shippings = shippingRepository.findBySendDateBetween(startDate, endDate);
        return createShippingResponses(shippings);
    }


    public List<ShippingResponse> getShippingsByStates(State state) {
        List<Shipping> shippings = shippingRepository.findByState(state);
        return createShippingResponses(shippings);
    }


    List<ShippingResponse> createShippingResponses(List<Shipping> shippings) {

        List<ShippingResponse> responses = new ArrayList<>();
        shippings.forEach(shipping -> {
                    ShippingResponse shippingResponse = new ShippingResponse();
                    shippingResponse.setShipping(shipping);
                    shippingResponse.setShippingItems(createShippingItemResponses(shippingItemRepository.findAllById(Collections.singleton(shipping.getId()))));
                    responses.add(shippingResponse);
                }
        );
        return responses;
    }

    List<ShippingItemResponse> createShippingItemResponses(List<ShippingItem> shippingItems) {
        List<ShippingItemResponse> responses = new ArrayList<>();
        shippingItems.forEach(shippingItem -> {
            ShippingItemResponse shippingItemResponse = new ShippingItemResponse();
            shippingItemResponse.setShipping_id(shippingItem.getId());
            shippingItemResponse.setProduct(shippingItem.getProduct());
            shippingItemResponse.setProductCount(shippingItem.getProductCount());
            responses.add(shippingItemResponse);
        });
        return responses;
    }


    @Retryable(value = {IllegalStateException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Shipping changeState(Long shippingId, State newState) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));
        if (shipping.getState().canTransitionTo(newState)) {
            shipping.setState(newState);
            return shippingRepository.save(shipping);
        } else {
            throw new IllegalStateException("Invalid state transition from " + shipping.getState() + " to " + newState);
        }
    }


    public List<Object[]> getTop3() {
        Pageable top3 = PageRequest.of(0, 3);
        return shippingItemRepository.findTop3MostShippedProducts(top3);


    }

    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Transactional
    public ResponseEntity createShippingRequest(ShippingRequestDTO request) {

        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findByAddress(request.getCustomerAddress()));

        Customer customer = existingCustomer.orElseGet(() -> {
            Customer newCustomer = new Customer();
            newCustomer.setFirstName(request.getCustomerFirstName());
            newCustomer.setLastName(request.getCustomerLastName());
            newCustomer.setAddress(request.getCustomerAddress());
            newCustomer.setCity(request.getCustomerCity());
            return customerRepository.save(newCustomer);
        });

        Shipping shipping = new Shipping();
        shipping.setCustomer(customer);
        shipping.setSendDate(LocalDate.now());
        shipping.setState(State.INICIAL);

        shipping = shippingRepository.save(shipping);
        for (ShippingItemDTO itemDTO : request.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ShippingItem shippingItem = new ShippingItem();
            shippingItem.setProduct(product);
            shippingItem.setProductCount(itemDTO.getProductCount());
            shippingItem.setShipping(shipping);

            shippingItemRepository.save(shippingItem);
        }

        return ResponseEntity.ok("Shipping Created with id " + shipping.getId());
    }
}
