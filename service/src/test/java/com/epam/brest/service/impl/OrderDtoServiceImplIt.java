package com.epam.brest.service.impl;

import com.epam.brest.model.dto.OrderDto;
import com.epam.brest.service.OrderDtoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:service-context-test.xml"})
@Transactional
class OrderDtoServiceImplIt {

    @Autowired
    OrderDtoService orderDtoService;

    @Test
    public void shouldFindAllWithTotalPrice() {
        List<OrderDto> orders= orderDtoService.findAllWithTotalPrice();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
        assertTrue(orders.get(0).getTotalPrice().intValue() > 0);
    }
}
