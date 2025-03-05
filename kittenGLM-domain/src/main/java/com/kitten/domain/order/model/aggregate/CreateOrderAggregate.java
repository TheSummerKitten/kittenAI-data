package com.kitten.domain.order.model.aggregate;

import com.kitten.domain.order.model.entity.OrderEntity;
import com.kitten.domain.order.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderAggregate {

    private String openid;

    private ProductEntity product;

    private OrderEntity order;
}
