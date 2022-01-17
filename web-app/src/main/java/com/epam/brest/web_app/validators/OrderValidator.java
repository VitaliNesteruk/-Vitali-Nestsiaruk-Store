package com.epam.brest.web_app.validators;

import com.epam.brest.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.epam.brest.model.constants.OrderConstants.ORDER_SHIPPER_SIZE;

@Component
public class OrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "shipper", "shipper.empty");
            Order order = (Order) target;

        if (StringUtils.hasLength(order.getShipper())
                && order.getShipper().length() > ORDER_SHIPPER_SIZE) {
            errors.rejectValue("shipper", "shipper.maxSize");
        }
    }
}
