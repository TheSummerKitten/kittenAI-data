package com.kitten.trigger.http;

import com.kitten.dataTypes.common.Constants;
import com.kitten.dataTypes.model.Response;

import com.kitten.domain.auth.service.IAuthService;
import com.kitten.domain.order.model.entity.ProductEntity;
import com.kitten.domain.order.service.IOrderService;
import com.kitten.trigger.http.dto.SaleProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/${app.config.api-version}/sale/")
public class SaleController {

    @Resource
    private IAuthService authService;
    @Resource
    private IOrderService orderService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ssXXX"); // 例如: 2022-01-01T00:00:00+08:00

    @GetMapping("query_product_list")
    public Response<List<SaleProductDTO>> queryProductList(@RequestHeader("Authorization") String token) {
        try {
            //1. token校验
            boolean success = authService.checkToken(token);
            log.info("校验结果:{}" , success);
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
            //
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

}
