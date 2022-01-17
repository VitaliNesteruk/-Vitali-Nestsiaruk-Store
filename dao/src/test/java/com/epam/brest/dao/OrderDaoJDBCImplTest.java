package com.epam.brest.dao;

import com.epam.brest.model.Order;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class OrderDaoJDBCImplTest {

    @InjectMocks
    private OrderDaoJDBCImpl orderDaoJDBC;

    // Object created by Mockito
    @Mock
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Captor
    private  ArgumentCaptor<RowMapper<Order>> captorMapper;

    @Captor
    private  ArgumentCaptor<SqlParameterSource> captorSource;

    @AfterEach
    public void check(){
        Mockito.verifyNoMoreInteractions(namedParameterJdbcTemplate);
    }

    @Test
    public void findAll(){

        String sql = "select";
        ReflectionTestUtils.setField(orderDaoJDBC, "sqlGetAllOrders", sql);
        Order order = new Order();
        List<Order> list = Collections.singletonList(order);

        Mockito.when(namedParameterJdbcTemplate.query(any(),
                ArgumentMatchers.<RowMapper<Order>>any())).thenReturn(list);

        List<Order> result = orderDaoJDBC.findAll();

        Mockito.verify(namedParameterJdbcTemplate).query(eq(sql), captorMapper.capture());

        RowMapper<Order> mapper = captorMapper.getValue();
        Assertions.assertNotNull(mapper);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertSame(order, result.get(0));
    }

    @Test
    public void getOrderById(){
        String sql = "get";
        ReflectionTestUtils.setField(orderDaoJDBC, "sqlGetOrderById", sql);
        int id = 0;
        Order order = new Order();

        Mockito.when(namedParameterJdbcTemplate.queryForObject(
                any(),
                ArgumentMatchers.<SqlParameterSource>any(),
                ArgumentMatchers.<RowMapper<Order>>any())).thenReturn(order);

        Order result = orderDaoJDBC.getOrderById(id);

        Mockito.verify(namedParameterJdbcTemplate)
                .queryForObject(eq(sql), captorSource.capture(), captorMapper.capture());
        SqlParameterSource source = captorSource.getValue();
        RowMapper<Order> mapper = captorMapper.getValue();

        Assertions.assertNotNull(source);
        Assertions.assertNotNull(mapper);

        Assertions.assertNotNull(result);
        Assertions.assertSame(order, result);
    }
}
