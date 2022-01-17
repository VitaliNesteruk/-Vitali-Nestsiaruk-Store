package com.epam.brest.service.impl;

import com.epam.brest.model.Order;
import com.epam.brest.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:service-context-test.xml"})
@Transactional
class OrderServiceImplIT {

    @Autowired
    OrderService orderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldCount() {
        assertNotNull(orderService);
        Integer quantity = orderService.count();
        assertNotNull(quantity);
        assertTrue(quantity > 0);
        assertEquals(Integer.valueOf(3), quantity);
    }

    @Test
    void create() {
        assertNotNull(orderService);
        Integer orderSizeBefore = orderService.count();
        assertNotNull(orderSizeBefore);
        Order order = new Order("Santa Blue");
        Integer newOrderId = orderService.create(order);
        assertNotNull(newOrderId);
        assertEquals(orderSizeBefore, orderService.count() - 1);
    }
}