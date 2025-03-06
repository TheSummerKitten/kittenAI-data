package com.kitten.domain.order.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartEntity {
    /**
     * 用户的openid
     */
    private String openid;

    private String productId;

    @Override
    public String toString() {
        return "ShopCartEntity{" +
                "openid='" + openid + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
