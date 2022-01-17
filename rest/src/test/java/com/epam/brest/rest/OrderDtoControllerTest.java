package com.epam.brest.rest;

import com.epam.brest.model.dto.OrderDto;
import com.epam.brest.service.OrderDtoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;


@ExtendWith(MockitoExtension.class)
class OrderDtoControllerTest {

    @InjectMocks
    private OrderDtoController orderDtoController;

    @Mock
    private OrderDtoService orderDtoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderDtoController)
                .build();
    }

    @AfterEach
    public void end() {
        Mockito.verifyNoMoreInteractions(orderDtoService);
    }

    @Test
    public void shouldFindAllWithTotalPrice() throws Exception {

        Mockito.when(orderDtoService.findAllWithTotalPrice()).thenReturn(Arrays.asList(create(0), create(1)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/order_dtos")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shipper", Matchers.is("d0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].shipper", Matchers.is("d1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].totalPrice", Matchers.is(101)))
        ;

        Mockito.verify(orderDtoService).findAllWithTotalPrice();
    }

    private OrderDto create(int index) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(index);
        orderDto.setShipper("d" + index);
        orderDto.setTotalPrice(BigDecimal.valueOf(100 + index));
        return orderDto;
    }
}