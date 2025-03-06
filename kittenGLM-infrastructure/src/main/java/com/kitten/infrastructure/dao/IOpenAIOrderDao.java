package com.kitten.infrastructure.dao;

import com.kitten.infrastructure.po.OpenAIOrderPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOpenAIOrderDao {

    // 查询未支付订单
    OpenAIOrderPO queryUnpaidOrder(OpenAIOrderPO openAIOrderPOReq);

    // 插入订单
    void insert(OpenAIOrderPO openAIOrderPO);

    void updateOrderPayInfo(OpenAIOrderPO po);
    // 更新订单支付状态为成功
    void changeOrderPaySuccess(String orderId);
}
