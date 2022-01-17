package com.epam.brest.service.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;

public class OrderNotFoundException extends EmptyResultDataAccessException {
    public OrderNotFoundException(Integer id) {
        super("Order not found for id: " + id, 1);
    }
}
