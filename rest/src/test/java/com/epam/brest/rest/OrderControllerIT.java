package com.epam.brest.rest;

import com.epam.brest.model.Order;
import com.epam.brest.rest.exception.CustomExceptionHandler;
import com.epam.brest.rest.exception.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static com.epam.brest.model.constants.OrderConstants.ORDER_SHIPPER_SIZE;
import static com.epam.brest.rest.exception.CustomExceptionHandler.ORDER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
public class OrderControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderControllerIT.class);

    public static final String ORDERS_ENDPOINT = "/orders";

    @Autowired
    private OrderController orderController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    MockMvcOrderService orderService = new MockMvcOrderService();

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void shouldFindAllOrders() throws Exception {

        // given
        Order order = new Order(RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE));
        Integer id = orderService.create(order);

        // when
        List<Order> orders = orderService.findAll();

        // then
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        Order order = new Order(RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE));
        Integer id = orderService.create(order);
        assertNotNull(id);
    }

    @Test
    public void shouldFindOrderById() throws Exception {

        // given
        Order order = new Order(RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE));
        Integer id = orderService.create(order);

        assertNotNull(id);

        // when
        Optional<Order> optionalOrder = orderService.findById(id);

        // then
        assertTrue(optionalOrder.isPresent());
        assertEquals(optionalOrder.get().getOrderId(), id);
        assertEquals(order.getShipper(), optionalOrder.get().getShipper());
    }

    @Test
    public void shouldUpdateOrder() throws Exception {

        // given
        Order order = new Order(RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE));
        Integer id = orderService.create(order);
        assertNotNull(id);

        Optional<Order> orderOptional = orderService.findById(id);
        assertTrue(orderOptional.isPresent());

        orderOptional.get().
                setShipper(RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE));

        // when
        int result = orderService.update(orderOptional.get());

        // then
        assertTrue(1 == result);

        Optional<Order> updatedOrderOptional = orderService.findById(id);
        assertTrue(updatedOrderOptional.isPresent());
        assertEquals(updatedOrderOptional.get().getOrderId(), id);
        assertEquals(updatedOrderOptional.get().getShipper(),orderOptional.get().getShipper());

    }

    @Test
    public void shouldDeleteOrder() throws Exception {
        // given
        Order order = new Order(RandomStringUtils.randomAlphabetic(ORDER_SHIPPER_SIZE));
        Integer id = orderService.create(order);

        List<Order> orders = orderService.findAll();
        assertNotNull(orders);

        // when
        int result = orderService.delete(id);

        // then
        assertTrue(1 == result);

        List<Order> currentOrders = orderService.findAll();
        assertNotNull(currentOrders);

        assertTrue(orders.size()-1 == currentOrders.size());
    }

    @Test
    public void shouldReturnOrderNotFoundError() throws Exception {

        LOGGER.debug("shouldReturnOrderNotFoundError()");
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get(ORDERS_ENDPOINT + "/999999")
                                .accept(MediaType.APPLICATION_JSON)
                        ).andExpect(status().isNotFound())
                        .andReturn().getResponse();
        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getMessage(), ORDER_NOT_FOUND);
    }


    class MockMvcOrderService {

        public List<Order> findAll() throws Exception {
            LOGGER.debug("findAll()");
            MockHttpServletResponse response = mockMvc.perform(get(ORDERS_ENDPOINT)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Order>>() {});
        }

        public Optional<Order> findById(Integer id) throws Exception {

            LOGGER.debug("findById({})", id);
            MockHttpServletResponse response = mockMvc.perform(get(ORDERS_ENDPOINT + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Order.class));
        }

        public Integer create(Order order) throws Exception {

            LOGGER.debug("create({})", order);
            String json = objectMapper.writeValueAsString(order);
            MockHttpServletResponse response =
                    mockMvc.perform(post(ORDERS_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int update(Order order) throws Exception {

            LOGGER.debug("update({})", order);
            MockHttpServletResponse response =
                    mockMvc.perform(put(ORDERS_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(order))
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int delete(Integer orderId) throws Exception {

            LOGGER.debug("delete(id:{})", orderId);
            MockHttpServletResponse response = mockMvc.perform(
                            MockMvcRequestBuilders.delete(new StringBuilder(ORDERS_ENDPOINT).append("/")
                                            .append(orderId).toString())
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}
