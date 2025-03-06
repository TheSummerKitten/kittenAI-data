package com.kitten.domain.order.model.entity;


import com.kitten.domain.order.model.valobj.PayStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayOrderEntity {
    /**
     * 用户id
     */
    private String openid;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 支付地址
     */
    private String payUrl;
    /**
     * 支付状态: 0-等待支付, 1-支付完成, 2-支付失败, 3-放弃支付
     */
    private PayStatusVO payStatus;

}
