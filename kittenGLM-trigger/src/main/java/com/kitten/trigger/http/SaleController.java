package com.kitten.trigger.http;

import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.eventbus.EventBus;
import com.kitten.dataTypes.common.Constants;
import com.kitten.dataTypes.model.Response;

import com.kitten.domain.auth.service.IAuthService;
import com.kitten.domain.order.model.entity.PayOrderEntity;
import com.kitten.domain.order.model.entity.ProductEntity;
import com.kitten.domain.order.model.entity.ShopCartEntity;
import com.kitten.domain.order.service.IOrderService;
import com.kitten.trigger.http.dto.SaleProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/${app.config.api-version}/sale/")
public class SaleController {

    @Resource
    private IAuthService authService;
    @Resource
    private IOrderService orderService;
    @Resource
    private EventBus eventBus;

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ssXXX"); // 例如: 2022-01-01T00:00:00+08:00

    @GetMapping("query_product_list")
    public Response<List<SaleProductDTO>> queryProductList(@RequestHeader("Authorization") String token) {
        try {
            //1. token校验
            boolean success = authService.checkToken(token);
            log.info("token校验结果:{}" , success);
            if (!success) {
                return Response.<List<SaleProductDTO>>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            //2. 查询product
            List<ProductEntity> productEntityList = orderService.queryProductList();
            ArrayList<SaleProductDTO> mallProductsDTOS = new ArrayList<>();
            for (ProductEntity entity : productEntityList) {
                SaleProductDTO dto = SaleProductDTO.builder()
                        .productId(entity.getProductId())
                        .productName(entity.getProductName())
                        .productDesc(entity.getProductDesc())
                        .price(entity.getPrice())
                        .quota(entity.getQuota())
                        .build();
                mallProductsDTOS.add(dto);
            }
            // 3. 返回结果
            return Response.<List<SaleProductDTO>>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(mallProductsDTOS)
                    .build();
        } catch (Exception e) {
            log.error("商品查询失败", e);
            return Response.<List<SaleProductDTO>>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


    // 用户下单
    @PostMapping("create_pay_order")
    public Response<String> createPayOrder(
            @RequestHeader("Authorization") String token,
            @RequestParam String productId
    ) {
        try {
            // 1. token校验
            boolean success = authService.checkToken(token);
            log.info("校验结果:{}" , success);
            if (!success) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            // 2. token解析
            String openid = authService.openid(token);
            assert null != openid; // 断言，确保不会为null
            log.info("用户openid:{} , 下单商品productId:{}", openid, productId);
            // 3. 提交订单
            ShopCartEntity entity = ShopCartEntity.builder()
                    .openid(openid)
                    .productId(productId)
                    .build();
            PayOrderEntity payOrderEntity = orderService.createOrder(entity);
            log.info("用户商品下单, openid:{}, productId:{}, orderId:{} ", openid, productId, payOrderEntity.getOrderId());
            // 4. 返回结果
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(payOrderEntity.getPayUrl())
                    .build();
        } catch (Exception e) {
            log.error("下单失败", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
    // 支付回调接口
    @PostMapping("pay_notify")
    public Response<String> payNotify(HttpServletRequest request) {
        try {
            log.info("支付回调接口, 回调结果:{}", request.getParameter("trade_status"));
            if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }
                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String aliPayTradeNo = params.get("trade_no");

                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    log.info("支付回调，交易名称: {}", params.get("subject"));
                    log.info("支付回调，交易状态: {}", params.get("trade_status"));
                    log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
                    log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
                    log.info("支付回调，交易金额: {}", params.get("total_amount"));
                    log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
                    log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
                    log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
                    log.info("支付回调，支付回调，更新订单 {}", tradeNo);
                    // 更新订单为已支付
                    orderService.changeOrderPaySuccess(tradeNo);
                    // 推送消息【自己的业务场景中可以使用MQ消息】
                    eventBus.post(tradeNo);
                }
            }
            // 3. 返回结果
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("支付回调失败", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }










}
