package com.neal.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neal.sell.dto.OrderDTO;
import com.neal.sell.entity.OrderDetail;
import com.neal.sell.enums.ResultEnum;
import com.neal.sell.exception.SellException;
import com.neal.sell.form.OrderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class OrderForm2OrderDTOConverter {

    private static Logger logger = LoggerFactory.getLogger(OrderForm2OrderDTOConverter.class);

    public static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetails;
        try{
            orderDetails = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            logger.error("对象转换错误 string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetails);
        return orderDTO;
    }
}
