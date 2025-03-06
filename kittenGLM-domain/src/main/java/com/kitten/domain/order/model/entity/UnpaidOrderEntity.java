package com.kitten.domain.order.model.entity;


import com.kitten.domain.order.model.valobj.PayStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidOrderEntity {
    private String openid;

    private String orderId;

    private BigDecimal totalAmount;

    private String productName;
    /**
     * 支付地址, 创建支付后, 获取的url
     */
    private String productUrl;
    /**
     * 支付状态url, 支付完成后, 跳转到该url
     */
    private String payUrl;
    /**
     * 支付状态: 0-等待支付, 1-支付完成, 2-支付失败, 3-放弃支付
     */
    private PayStatusVO payStatus;

}
