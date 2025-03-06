package com.kitten.infrastructure.repository;

import com.kitten.dataTypes.enums.OpenAIProductEnableModel;
import com.kitten.domain.order.model.aggregate.CreateOrderAggregate;
import com.kitten.domain.order.model.entity.*;
import com.kitten.domain.order.model.valobj.PayStatusVO;
import com.kitten.domain.order.repository.IOrderRepository;
import com.kitten.infrastructure.dao.IOpenAIOrderDao;
import com.kitten.infrastructure.dao.IOpenAIProductDao;
import com.kitten.infrastructure.po.OpenAIOrderPO;
import com.kitten.infrastructure.po.OpenAIProductPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOpenAIProductDao openAIProductDao;

    @Resource
    private IOpenAIOrderDao openAIOrderDao;

    /**
     * 商品查询
     * @return
     */
    @Override
    public List<ProductEntity> queryProductList() {
        // 查询数据库PO
        List<OpenAIProductPO> openAIProductPOList = openAIProductDao.queryProductList();
        // PO转换ENTITY
        ArrayList<ProductEntity> entityList = new ArrayList<>(openAIProductPOList.size());
        for (OpenAIProductPO openAIProductPO : openAIProductPOList) {
            ProductEntity entity = new ProductEntity();
            entity.setProductId(openAIProductPO.getProductId());
            entity.setProductName(openAIProductPO.getProductName());
            entity.setProductDesc(openAIProductPO.getProductDesc());
            entity.setQuota(openAIProductPO.getQuota());
            entity.setPrice(openAIProductPO.getPrice());
            entityList.add(entity);
        }
        return entityList;
    }

    /**
     * 未支付订单查询
     * @param entity : openid + productId
     * @return
     */
    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity entity) {
        // 构造PO实体
        OpenAIOrderPO openAIOrderPOReq = new OpenAIOrderPO();
        openAIOrderPOReq.setOpenid(entity.getOpenid());
        openAIOrderPOReq.setProductId(Integer.parseInt(entity.getProductId()));
        // 查询数据库PO
        OpenAIOrderPO openAIOrderPORes = openAIOrderDao.queryUnpaidOrder(openAIOrderPOReq);

        if (null == openAIOrderPORes) {
            return null;
        }

        return UnpaidOrderEntity.builder()
                .openid(openAIOrderPORes.getOpenid())
                .orderId(openAIOrderPORes.getOrderId())
                .totalAmount(openAIOrderPORes.getTotalAmount())
                .productName(openAIOrderPORes.getProductName())
                .productUrl(openAIOrderPORes.getPayUrl())
                .payStatus(PayStatusVO.get(openAIOrderPORes.getPayStatus()))
                .build();
    }

    /**
     * 商品详情查询
     * @param productId
     * @return
     */
    @Override
    public ProductEntity queryProduct(String productId) {
        OpenAIProductPO openAIProductPO = openAIProductDao.queryProductByProductId(productId);
        log.info("queryProductByProductId: {}", openAIProductPO);
        ProductEntity entity = new ProductEntity();
        entity.setProductId(openAIProductPO.getProductId());
        entity.setProductName(openAIProductPO.getProductName());
        entity.setProductDesc(openAIProductPO.getProductDesc());
        entity.setQuota(openAIProductPO.getQuota());
        entity.setPrice(openAIProductPO.getPrice());
        entity.setEnable(OpenAIProductEnableModel.get(openAIProductPO.getIsEnabled()));
        return entity;
    }

    /**
     * 保存订单
     * @param aggregate
     */
    @Override
    public void saveOrder(CreateOrderAggregate aggregate) {
        String openid = aggregate.getOpenid();
        ProductEntity product = aggregate.getProduct();
        OrderEntity order = aggregate.getOrder();
        OpenAIOrderPO openAIOrderPO = new OpenAIOrderPO();
        openAIOrderPO.setOpenid(openid);
        openAIOrderPO.setProductId(product.getProductId());
        openAIOrderPO.setProductName(product.getProductName());
        openAIOrderPO.setProductQuota(product.getQuota());
        openAIOrderPO.setOrderId(order.getOrderId());
        openAIOrderPO.setOrderTime(order.getOrderTime());
        openAIOrderPO.setOrderStatus(order.getOrderStatus().getCode());
        openAIOrderPO.setTotalAmount(order.getTotalAmount());
        openAIOrderPO.setPayType(order.getPayTypeVO().getCode());
        openAIOrderPO.setPayStatus(PayStatusVO.WAIT.getCode());
        openAIOrderDao.insert(openAIOrderPO);
    }

    /**
     * 更新订单支付信息
     * @param entity
     */
    @Override
    public void updateOrderPayInfo(PayOrderEntity entity) {
        OpenAIOrderPO po = new OpenAIOrderPO();
        po.setOpenid(entity.getOpenid());
        po.setOrderId(entity.getOrderId());
        po.setPayUrl(entity.getPayUrl());
        po.setPayStatus(entity.getPayStatus().getCode());
        openAIOrderDao.updateOrderPayInfo(po);
    }

    @Override
    public void changeOrderPaySuccess(String orderId) {
        openAIOrderDao.changeOrderPaySuccess(orderId);
    }
}
