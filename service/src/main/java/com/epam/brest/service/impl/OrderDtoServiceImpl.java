package com.epam.brest.service.impl;

import com.epam.brest.dao.OrderDtoDao;
import com.epam.brest.model.dto.OrderDto;
import com.epam.brest.service.OrderDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderDtoServiceImpl implements OrderDtoService {

    private final OrderDtoDao orderDtoDao;

    public OrderDtoServiceImpl(OrderDtoDao orderDtoDao) {
        this.orderDtoDao = orderDtoDao;
    }

    public List<OrderDto> findAllWithTotalPrice() {
        return orderDtoDao.findAllWithTotalPrice();
    }
}
