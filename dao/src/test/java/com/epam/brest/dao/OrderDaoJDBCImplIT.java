package com.epam.brest.dao;

import com.epam.brest.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-jdbc-conf.xml"})
@Transactional
@Rollback
class OrderDaoJDBCImplIT {

    private final Logger logger = LogManager.getLogger(OrderDaoJDBCImplIT.class);

    private OrderDaoJDBCImpl orderDaoJDBC;

    public OrderDaoJDBCImplIT(@Autowired OrderDao orderDaoJDBC) {
        this.orderDaoJDBC = (OrderDaoJDBCImpl) orderDaoJDBC;
    }

    @Test
    void findAll() {
        logger.debug("Execute test: findAll()");
        assertNotNull(orderDaoJDBC);
        assertNotNull(orderDaoJDBC.findAll());
    }

    @Test
    void create() {
        assertNotNull(orderDaoJDBC);
        int ordersSizeBefore = orderDaoJDBC.count();
        Order order = new Order("Santa Fish");
        Integer newOrderId = orderDaoJDBC.create(order);
        assertNotNull(newOrderId);
        assertEquals((int) ordersSizeBefore, orderDaoJDBC.count() - 1);
    }

    @Test
    void getOrderById() {
        List<Order> orders = orderDaoJDBC.findAll();
        if (orders.size() == 0) {
            orderDaoJDBC.create(new Order("TEST ORDER"));
            orders = orderDaoJDBC.findAll();
        }

        Order orderSrc = orders.get(0);
        Order orderDst = orderDaoJDBC.getOrderById(orderSrc.getOrderId());
        assertEquals(orderSrc.getShipper(), orderDst.getShipper());
    }

    @Test
    void updateOrder() {
        List<Order> orders = orderDaoJDBC.findAll();
        if (orders.size() == 0) {
            orderDaoJDBC.create(new Order("TEST ORDER"));
            orders = orderDaoJDBC.findAll();
        }

        Order orderSrc = orders.get(0);
        orderSrc.setShipper(orderSrc.getShipper() + "_TEST");
        orderDaoJDBC.update(orderSrc);

        Order orderDst = orderDaoJDBC.getOrderById(orderSrc.getOrderId());
        assertEquals(orderSrc.getShipper(), orderDst.getShipper());
    }

    @Test
    void deleteOrder() {
        orderDaoJDBC.create(new Order("TEST ORDER"));
        List<Order> orders = orderDaoJDBC.findAll();

        orderDaoJDBC.delete(orders.get(orders.size() - 1).getOrderId());
        assertEquals(orders.size() - 1, orderDaoJDBC.findAll().size());
    }

    @Test
    void shouldCount() {
        assertNotNull(orderDaoJDBC);
        Integer quantity = orderDaoJDBC.count();
        assertNotNull(quantity);
        assertTrue(quantity > 0);
        assertEquals(Integer.valueOf(3), quantity);
    }
}