package sube.interviews.mareoenvios.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.entity.ShippingResponse;
import sube.interviews.mareoenvios.entity.State;
import sube.interviews.mareoenvios.service.ShippingService;

@SpringBootTest
@AutoConfigureMockMvc
class ShippingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShippingService shippingService;

    @Test
    void getShipping_found() throws Exception {
        Long shippingId = 1L;
        ShippingResponse response = new ShippingResponse();
        when(shippingService.getShipping(shippingId)).thenReturn(response);

        mockMvc.perform(get("/shipping/info/{shippingId}", shippingId))
                .andExpect(status().isOk());
    }

    @Test
    void getShipping_notFound() throws Exception {
        Long shippingId = 2L;
        when(shippingService.getShipping(shippingId)).thenReturn(null);

        mockMvc.perform(get("/shipping/info/{shippingId}", shippingId))
                .andExpect(status().isOk());
    }

    @Test
    void getShippingsBetweenDates_returnsList() throws Exception {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        List<ShippingResponse> responses = Arrays.asList(new ShippingResponse(), new ShippingResponse());
        when(shippingService.getShippingsBetweenDates(start, end)).thenReturn(responses);

        mockMvc.perform(get("/shipping/info/{sendDateFrom}/{sendDateTo}", start, end))
                .andExpect(status().isOk());
    }

    @Test
    void getShippingState_returnsList() throws Exception {
        State state = State.INICIAL;
        when(shippingService.getShippingsByStates(state)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/shipping/info/state/{state}", state))
                .andExpect(status().isNoContent());
    }

    @Test
    void changeStateToDelivered_success() throws Exception {
        Long shippingId = 1L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.EN_CAMINO);

        when(shippingService.changeState(shippingId, State.ENTREGADO)).thenReturn(shipping);

        mockMvc.perform(get("/shipping/transition/delivered/{shippingId}", shippingId))
                .andExpect(status().isOk())
                .andExpect(content().string("State updated successfully"));
    }

    @Test
    void changeStateToDelivered_invalidTransition() throws Exception {
        Long shippingId = 2L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.INICIAL);

        when(shippingService.changeState(shippingId, State.ENTREGADO)).thenThrow(new IllegalStateException("Invalid state transition"));

        mockMvc.perform(get("/shipping/transition/delivered/{shippingId}", shippingId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid state transition"));
    }

    @Test
    void changeStateToSendToMail_success() throws Exception {
        Long shippingId = 1L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.ENTREGADO_AL_CORREO);

        when(shippingService.changeState(shippingId, State.ENTREGADO_AL_CORREO)).thenReturn(shipping);

        mockMvc.perform(get("/shipping/transition/sendToMail/{shippingId}", shippingId))
                .andExpect(status().isOk())
                .andExpect(content().string("State updated successfully"));
    }

    @Test
    void changeStateToSendToMail_invalidTransition() throws Exception {
        Long shippingId = 2L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.ENTREGADO);

        when(shippingService.changeState(shippingId, State.ENTREGADO_AL_CORREO))
                .thenThrow(new IllegalStateException("Invalid state transition"));

        mockMvc.perform(get("/shipping/transition/sendToMail/{shippingId}", shippingId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid state transition"));
    }

    @Test
    void changeStateToInTravel_success() throws Exception {
        Long shippingId = 3L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.EN_CAMINO);

        when(shippingService.changeState(shippingId, State.EN_CAMINO)).thenReturn(shipping);

        mockMvc.perform(get("/shipping/transition/inTravel/{shippingId}", shippingId))
                .andExpect(status().isOk())
                .andExpect(content().string("State updated successfully"));
    }

    @Test
    void changeStateToInTravel_invalidTransition() throws Exception {
        Long shippingId = 4L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.INICIAL);

        when(shippingService.changeState(shippingId, State.EN_CAMINO))
                .thenThrow(new IllegalStateException("Invalid state transition"));

        mockMvc.perform(get("/shipping/transition/inTravel/{shippingId}", shippingId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid state transition"));
    }

    @Test
    void changeStateToCancelled_success() throws Exception {
        Long shippingId = 5L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.CANCELADO);

        when(shippingService.changeState(shippingId, State.CANCELADO)).thenReturn(shipping);

        mockMvc.perform(get("/shipping/transition/cancelled/{shippingId}", shippingId))
                .andExpect(status().isOk())
                .andExpect(content().string("State updated successfully"));
    }

    @Test
    void changeStateToCancelled_invalidTransition() throws Exception {
        Long shippingId = 6L;
        Shipping shipping = new Shipping();
        shipping.setId(shippingId);
        shipping.setState(State.ENTREGADO);

        when(shippingService.changeState(shippingId, State.CANCELADO))
                .thenThrow(new IllegalStateException("Invalid state transition"));

        mockMvc.perform(get("/shipping/transition/cancelled/{shippingId}", shippingId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid state transition"));
    }

}
