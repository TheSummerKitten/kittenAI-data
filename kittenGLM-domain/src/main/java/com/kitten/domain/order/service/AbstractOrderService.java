package com.kitten.domain.order.service;


import com.kitten.domain.order.repository.IOrderRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
public abstract class AbstractOrderService implements IOrderService{

    @Resource
    protected IOrderRepository orderRepository;



}
