package com.kitten.domain.order.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.kitten.domain.order.model.aggregate.CreateOrderAggregate;
import com.kitten.domain.order.model.entity.OrderEntity;
import com.kitten.domain.order.model.entity.PayOrderEntity;
import com.kitten.domain.order.model.entity.ProductEntity;
import com.kitten.domain.order.model.entity.ShopCartEntity;
import com.kitten.domain.order.model.valobj.OrderStatusVO;
import com.kitten.domain.order.model.valobj.PayStatusVO;
import com.kitten.domain.order.model.valobj.PayTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderService extends AbstractOrderService{

    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;
    @Resource
    private AlipayClient alipayClient;

    @Override
    public List<ProductEntity> queryProductList() {
        return orderRepository.queryProductList();
    }



    /**
     * 创建订单
     * @param openid
     * @param productEntity
     * @return
     */
    @Override
    protected OrderEntity doSaveOrder(String openid, ProductEntity productEntity) {
        OrderEntity orderEntity = new OrderEntity();
        // 数据库建立了幂等拦截, 有重复id会主键冲突
//        orderEntity.setOrderId(String.valueOf(System.currentTimeMillis()));
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderStatus(OrderStatusVO.CREATE);
        orderEntity.setTotalAmount(productEntity.getPrice());
        orderEntity.setPayTypeVO(PayTypeVO.ALIPAY);
        // 聚合信息
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .openid(openid)
                .product(productEntity)
                .order(orderEntity)
                .build();
        // 保存订单
        // 保存订单；订单和支付，是2个操作。
        // 一个是数据库操作，一个是HTTP操作。所以不能一个事务处理，只能先保存订单再操作创建支付单，如果失败则需要任务补偿
        orderRepository.saveOrder(aggregate);
        return orderEntity;
    }

    /**
     * 预支付订单
     * @param openid
     * @param orderId
     * @param productName
     * @param totalAmount
     * @return
     */
    @Override
    protected PayOrderEntity doPrePayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContent.put("total_amount", totalAmount.toString());
        bizContent.put("subject", productName);
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        // 订单创建成功, 等待支付
        PayOrderEntity entity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(form)
                .payStatus(PayStatusVO.WAIT)
                .build();
        // 更新订单信息
        orderRepository.updateOrderPayInfo(entity);
        return entity;
    }

    /**
     * 更新订单支付状态为成功
     * @param orderId
     */
    @Override
    public void changeOrderPaySuccess(String orderId) {
        orderRepository.changeOrderPaySuccess(orderId);
    }

    /**
     * 发货
     * @param orderId
     */
    @Override
    public void deliverGoods(String orderId) {
        orderRepository.deliverGoods(orderId);
    }


}
