package com.kitten.domain.order.service;


import com.alipay.api.AlipayApiException;
import com.kitten.dataTypes.common.Constants;
import com.kitten.dataTypes.exception.ChatGPTException;
import com.kitten.domain.order.model.entity.*;
import com.kitten.domain.order.model.valobj.OrderStatusVO;
import com.kitten.domain.order.model.valobj.PayStatusVO;
import com.kitten.domain.order.repository.IOrderRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
public abstract class AbstractOrderService implements IOrderService{

    @Resource
    protected IOrderRepository orderRepository;

    // 抽象类默认实现
    @Override
    public PayOrderEntity createOrder(ShopCartEntity entity) {
        try {
            // 基础信息
            String openid = entity.getOpenid();
            String productId = entity.getProductId();
            // 1.查询当前用户是否存在调单和未支付订单         order_status=0
            UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(entity);
            if (null != unpaidOrderEntity && PayStatusVO.WAIT.equals(unpaidOrderEntity.getPayStatus())) {  // 存在未支付订单
                log.info("当前用户存在未支付订单，订单号：{}，商品ID：{}", unpaidOrderEntity.getOrderId(), productId);
                return PayOrderEntity.builder()
                        .openid(openid)
                        .orderId(unpaidOrderEntity.getOrderId())
                        .payUrl(unpaidOrderEntity.getPayUrl())
                        .payStatus(unpaidOrderEntity.getPayStatus())
                        .build();
            }

            // 2. 查询productId对应的商品信息, 聚合订单
            ProductEntity productEntity = orderRepository.queryProduct(productId);
            if (!productEntity.isAvailable()) { // 商品是否有效
                throw new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
            }

            // 3. 保存订单
            OrderEntity orderEntity = this.doSaveOrder(openid, productEntity);
            // 4.创建支付
            PayOrderEntity payOrderEntity = this.doPrePayOrder(openid, orderEntity.getOrderId(), productEntity.getProductName(), orderEntity.getTotalAmount());
            log.info("创建订单 - 完成，支付单：{}，orderId：{}", openid, orderEntity.getOrderId());

            return payOrderEntity;
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }



    // 子类实现
    protected abstract OrderEntity doSaveOrder(String openid, ProductEntity productEntity);

    protected abstract PayOrderEntity doPrePayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) throws AlipayApiException;



}
