package com.epam.brest.dao;

import com.epam.brest.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class OrderDaoJDBCImpl implements OrderDao{

    private final Logger logger = LogManager.getLogger(OrderDaoJDBCImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Order> orderRowMapper = BeanPropertyRowMapper.newInstance(Order.class);

    //private final String SQL_ALL_ORDERS = "SELECT op.order_id, op.shipper, op.date FROM order_product op ORDER BY shipper";
    //private final String SQL_CREATE_ORDERS = "INSERT INTO order_product(shipper, date) VALUES(:shipper, :date)";
    @Value("${SQL_ORDER_COUNT}")
    public String sqlOrderCount;

    @Value("${SQL_ALL_ORDERS}")
    private String sqlGetAllOrders;

    @Value("${SQL_CREATE_ORDER}")
    private String sqlCreateOrder;

    @Value("${SQL_ORDER_BY_ID}")
    private String sqlGetOrderById;

    @Value("${SQL_UPDATE_SHIPPER}")
    private String sqlUpdateShipper;

    @Value("${SQL_DELETE_ORDER_BY_ID}")
    private String sqlDeleteOrderById;

    public OrderDaoJDBCImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Order> findAll() {
        logger.debug("Start: findAll()");
        return namedParameterJdbcTemplate.query(sqlGetAllOrders, orderRowMapper);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        logger.debug("Get order by id = {}", orderId);
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("orderId", orderId);
        return namedParameterJdbcTemplate.queryForObject(sqlGetOrderById, sqlParameterSource, new OrderRowMapper());
    }

    @Override
    public Integer create(Order order) {
        logger.debug("Create order: {}", order);
//         SqlParameterSource sqlParameterSource =
//                 new MapSqlParameterSource("shipper", order.getShipper());
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("shipper", order.getShipper())
                        .addValue("date", order.getDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sqlCreateOrder, sqlParameterSource, keyHolder);
        return (Integer) keyHolder.getKey();
    }

    @Override
    public Integer update(Order order) {
        logger.debug("Update order: {}", order);
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("shipper", order.getShipper())
                        .addValue("orderId", order.getOrderId())
                        .addValue("date", order.getDate());
        return namedParameterJdbcTemplate.update(sqlUpdateShipper, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer orderId) {
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("orderId", orderId);
        return namedParameterJdbcTemplate.update(sqlDeleteOrderById, sqlParameterSource);
    }

    @Override
    public Integer count() {
        logger.debug("count()");
        return namedParameterJdbcTemplate
                .queryForObject(sqlOrderCount, new MapSqlParameterSource(), Integer.class);
    }

    private class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet resultSet, int i) throws SQLException {
            Order order = new Order();
            order.setOrderId(resultSet.getInt("order_id"));
            order.setShipper(resultSet.getString("shipper"));
            //order.setDate(resultSet.getObject("date",LocalDate.class));
            return order;
        }
    }
}
