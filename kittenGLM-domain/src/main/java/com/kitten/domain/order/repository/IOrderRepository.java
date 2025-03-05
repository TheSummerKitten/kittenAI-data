package com.kitten.domain.order.repository;

import com.kitten.domain.order.model.entity.ProductEntity;

import java.util.List;

public interface IOrderRepository {

    List<ProductEntity> queryProductList();




}
