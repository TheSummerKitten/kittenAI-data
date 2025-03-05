package com.kitten.domain.order.service;

import com.kitten.domain.order.model.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService extends AbstractOrderService{


    @Override
    public List<ProductEntity> queryProductList() {
        return orderRepository.queryProductList();
    }

}
