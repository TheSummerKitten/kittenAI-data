package com.kitten.domain.order.service;

import com.kitten.domain.order.model.entity.ProductEntity;

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


}
