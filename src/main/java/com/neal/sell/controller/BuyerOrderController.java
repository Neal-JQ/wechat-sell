package com.neal.sell.controller;

import com.neal.sell.converter.OrderForm2OrderDTOConverter;
import com.neal.sell.dto.OrderDTO;
import com.neal.sell.enums.ResultEnum;
import com.neal.sell.exception.SellException;
import com.neal.sell.form.OrderForm;
import com.neal.sell.service.BuyerService;
import com.neal.sell.service.OrderService;
import com.neal.sell.utils.ResultVoUtil;
import com.neal.sell.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order/")
public class BuyerOrderController {

    private Logger logger = LoggerFactory.getLogger(BuyerOrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            logger.error("创建订单 - 参数不正确 orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            logger.error("创建订单 - 购物车为空 orderForm={}", orderForm);
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderService.create(orderDTO);
        Map<String, String> result = new HashMap<>();
        result.put("orderId", createResult.getOrderId());
        return ResultVoUtil.success(result);
    }

    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid")String openid,
                                         @RequestParam(value = "page", defaultValue = "0")Integer page,
                                         @RequestParam(value = "size", defaultValue = "10")Integer size){
        Page<OrderDTO> orderList = orderService.findList(openid, PageRequest.of(page, size));
        return ResultVoUtil.success(orderList);
    }

    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid")String openid,
                                     @RequestParam(value = "orderId")String orderId){
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVoUtil.success(orderDTO);
    }

    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid")String openid,
                           @RequestParam(value = "orderId")String orderId){
        buyerService.cancelOrder(openid, orderId);
        return ResultVoUtil.success();
    }
}
