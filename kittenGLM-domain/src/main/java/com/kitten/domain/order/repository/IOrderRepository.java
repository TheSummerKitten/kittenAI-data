package com.kitten.domain.order.repository;

import com.kitten.domain.order.model.aggregate.CreateOrderAggregate;
import com.kitten.domain.order.model.entity.PayOrderEntity;
import com.kitten.domain.order.model.entity.ProductEntity;
import com.kitten.domain.order.model.entity.ShopCartEntity;
import com.kitten.domain.order.model.entity.UnpaidOrderEntity;

import java.util.List;

public interface IOrderRepository {

    List<ProductEntity> queryProductList();

    // 查询未支付订单  ShopCartEntity: openid, productId
    UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity entity);

    /**
     * 查询商品详情
     * @param productId
     * @return
     */
    ProductEntity queryProduct(String productId);

    /**
     * 创建订单
     * @param aggregate
     */
    void saveOrder(CreateOrderAggregate aggregate);

    /**
     * 更新订单支付信息
     * @param entity
     */
    void updateOrderPayInfo(PayOrderEntity entity);

    /**
     * 更新订单支付状态为成功
     * @param orderId
     */
    void changeOrderPaySuccess(String orderId);
}
