package com.epam.brest.web_app;

import com.epam.brest.model.Order;
import com.epam.brest.service.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Date;

import static com.epam.brest.model.constants.OrderConstants.ORDER_SHIPPER_SIZE;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
@Transactional
public class OrderControllerIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void shouldReturnOrdersPage() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/orders")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("orders", hasItem(
                        allOf(
                                hasProperty("orderId", is(1)),
                                hasProperty("shipper", is("Savushkin Product"))
                                //hasProperty("totalPrice", is(BigDecimal.valueOf(1014.00)))
                                //hasProperty("date", is(2021-10-17))
                        )
                )))
                .andExpect(model().attribute("orders", hasItem(
                        allOf(
                                hasProperty("orderId", is(2)),
                                hasProperty("shipper", is("Perfect"))
                                //hasProperty("totalPrice", is(BigDecimal.valueOf(500))),
                                //hasProperty("date", is(new Date(2021-9-21)))
                        )
                )))
                .andExpect(model().attribute("orders", hasItem(
                        allOf(
                                hasProperty("orderId", is(3)),
                                hasProperty("shipper", is("Belita"))
                                //hasProperty("totalPrice", is(BigDecimal.valueOf(500))),
                                //hasProperty("date", is(new Date(2021-9-21)))
                        )
                )));

    }

    @Test
    void shouldAddOrder() throws Exception {
        //WHEN
        assertNotNull(orderService);
        Integer orderSizeBefore = orderService.count();
        assertNotNull(orderSizeBefore);
        //Order order = new Order("Santa Blue","2021-10-17");
        Order order = new Order("Santa Blue");

        //THEN
        //Integer newOrderId = orderService.create(order);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("shipper", order.getShipper())
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders"))
                .andExpect(redirectedUrl("/orders"));

        //VERIFY
        assertEquals(orderSizeBefore, orderService.count() - 1);
    }

    @Test
    void shouldFailAddOrderOnEmptyName() throws Exception {
        // WHEN
        Order order = new Order("");

        // THEN
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/order")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("shipper", order.getShipper())
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(
                        model().attributeHasFieldErrors(
                                "order", "shipper"
                        )
                );
    }

    @Test
    public void shouldOpenEditOrderPageById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/order/1")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("order"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("order", hasProperty("orderId", is(1))))
                .andExpect(model().attribute("order", hasProperty("shipper", is("Savushkin Product"))));
    }

    @Test
    public void shouldUpdateOrderAfterEdit() throws Exception {

        String testName = RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/order/1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("orderId", "1")
                                .param("shipper", testName)
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/orders"))
                .andExpect(redirectedUrl("/orders"));

        Order order = orderService.getOrderById(1);
        assertNotNull(order);
        assertEquals(testName, order.getShipper());
    }

    @Test
    public void shouldDeleteOrder() throws Exception {

        Integer countBefore = orderService.count();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/order/3/delete")
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/orders"))
                .andExpect(redirectedUrl("/orders"));

        // verify database size
        Integer countAfter = orderService.count();
        Assertions.assertEquals(countBefore - 1, countAfter);

    }
}
