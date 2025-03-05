package com.kitten.infrastructure.repository;

import com.kitten.domain.order.model.entity.ProductEntity;
import com.kitten.domain.order.repository.IOrderRepository;
import com.kitten.infrastructure.dao.IOpenAIProductDao;
import com.kitten.infrastructure.po.OpenAIProductPO;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOpenAIProductDao openAIProductDao;


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
}
