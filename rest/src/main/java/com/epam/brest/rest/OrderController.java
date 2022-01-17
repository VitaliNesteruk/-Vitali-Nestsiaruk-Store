package com.epam.brest.rest;

import com.epam.brest.dao.OrderDaoJDBCImpl;
import com.epam.brest.model.Order;
import com.epam.brest.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class OrderController {

    private final Logger logger = LogManager.getLogger(OrderDaoJDBCImpl.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/orders")
    public final Collection<Order> orders() {

        logger.debug("orders()");
        return orderService.findAll();
    }

    @GetMapping(value = "/orders/{id}")
    public final Order getOrderById(@PathVariable Integer id) {

        logger.debug("getOrderById({})", id);
        Order order = orderService.getOrderById(id);
        return order;
    }

    @PostMapping(path = "/orders", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> createOrder(@RequestBody Order order) {

        logger.debug("createOrder({})", order);
        Integer id = orderService.create(order);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/orders", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateOrder(@RequestBody Order order) {

        logger.debug("updateOrder({})", order);
        int result = orderService.update(order);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deleteOrder(@PathVariable Integer id) {

        int result = orderService.delete(id);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
