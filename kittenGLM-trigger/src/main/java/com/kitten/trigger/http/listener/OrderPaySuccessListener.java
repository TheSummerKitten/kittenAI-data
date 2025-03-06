package com.kitten.trigger.http.listener;

import com.google.common.eventbus.Subscribe;
import com.kitten.domain.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private IOrderService orderService;

    @Subscribe
    public void handleEvent(String orderId) {
        try {
            log.info("收到支付成功消息，发货 orderId：{}", orderId);
            orderService.deliverGoods(orderId);
        } catch (Exception e) {
            log.info("订单支付成功，发货失败 orderId：{}", orderId, e);
        }
    }

}
