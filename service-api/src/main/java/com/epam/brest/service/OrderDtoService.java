package com.epam.brest.service;

import com.epam.brest.model.dto.OrderDto;

import java.util.List;

public interface OrderDtoService {

    /**
     * Get list of order Dto.
     *
     * @return list of order Dto.
     */
    List<OrderDto> findAllWithTotalPrice();
}
