package com.epam.brest.service.impl;

import com.epam.brest.dao.OrderDao;
import com.epam.brest.model.Order;
import com.epam.brest.service.OrderService;
import com.epam.brest.service.exceptions.OrderNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    private final OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        logger.trace("findAll()");
        return orderDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Integer orderId) {
        logger.debug("Get order by id = {}", orderId);
        try {
            return this.orderDao.getOrderById(orderId);
        } catch (EmptyResultDataAccessException e) {
            throw new OrderNotFoundException(orderId);
        }
    }

    @Override
    @Transactional
    public Integer create(Order order) {
        logger.debug("create({})", order);
        return this.orderDao.create(order);
    }

    @Override
    @Transactional
    public Integer update(Order order) {
        logger.debug("update({})", order);
        return this.orderDao.update(order);
    }

    @Override
    @Transactional
    public Integer delete(Integer orderId) {
        logger.debug("delete order with id = {}", orderId);
        return this.orderDao.delete(orderId);
    }

    @Override
    @Transactional(readOnly = true )
    public Integer count() {
        logger.debug("count()");
        return this.orderDao.count();
    }
}
