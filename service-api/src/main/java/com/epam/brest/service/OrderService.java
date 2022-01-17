package com.epam.brest.service;

import com.epam.brest.model.Order;

import java.util.List;

public interface OrderService {

    /**
     * Find all orders.
     *
     * @return orders list.
     */
    List<Order> findAll();

    /**
     * Find order by Id.
     *
     * @param orderId order Id.
     * @return order
     */
    Order getOrderById(Integer orderId);

    /**
     * Persist new order.
     *
     * @param order order.
     * @return persisted order id.
     */
    Integer create(Order order);

    /**
     * Update order.
     *
     * @param order order.
     * @return number of updated records in the database.
     */
    Integer update(Order order);

    /**
     * Delete order.
     *
     * @param orderId order id.
     * @return number of updated records in the database.
     */
    Integer delete(Integer orderId);

    /**
     * Count order.
     *
     * @return quantity of the orders.
     */
    Integer count();
}
