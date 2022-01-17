package com.epam.brest.dao;

import com.epam.brest.model.dto.OrderDto;

import java.util.List;

/**
 * OrderDto DAO Interface.
 */
public interface OrderDtoDao {
    /**
     * Get all orders with total price by order.
     *
     * @return orders list.
     */
    List<OrderDto> findAllWithTotalPrice();
}
