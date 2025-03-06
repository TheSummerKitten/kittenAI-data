package com.kitten.domain.order.service;

import com.kitten.domain.order.model.entity.PayOrderEntity;
import com.kitten.domain.order.model.entity.ProductEntity;
import com.kitten.domain.order.model.entity.ShopCartEntity;

import java.util.List;

/**
 * 订单领域接口
 */
public interface IOrderService {


    /**
     * 查询商品列表
     * @return
     */
    List<ProductEntity> queryProductList();

    /**
     * 添加商品到购物车, 返回下单后的支付单
     * @param entity
     * @return
     */
    PayOrderEntity createOrder(ShopCartEntity entity);

    /**
     * 更新订单支付状态为已支付
     * @param orderId
     */
    void changeOrderPaySuccess(String orderId);
}
