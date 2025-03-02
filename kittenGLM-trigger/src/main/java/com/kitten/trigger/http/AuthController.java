package com.kitten.trigger.http;

import com.alibaba.fastjson.JSON;
import com.kitten.dataTypes.common.Constants;
import com.kitten.dataTypes.model.Response;
import com.kitten.domain.auth.model.entity.AuthStateEntity;
import com.kitten.domain.auth.model.valobj.AuthTypeVO;
import com.kitten.domain.auth.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 鉴权服务
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/auth/")
public class AuthController {

    @Resource
    private IAuthService authService;

    @PostMapping("login")
    public Response<String> login(@RequestParam(value = "code") String code) {
        log.info("有用户请求登录, 鉴权: 验证码为 {}", code);

        try {
            AuthStateEntity authStateEntity = authService.doLogin(code);
            log.info("鉴权登录校验完成，验证码: {} 结果: {}", code, JSON.toJSONString(authStateEntity));

            // 鉴权失败, 拦截
            if (!AuthTypeVO.A0000.getCode().equals(authStateEntity.getCode())) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            // 鉴权成功
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(authStateEntity.getToken())
                    .build();

        } catch (Exception e) {
            log.error("登录鉴权失败, 验证码: {}", code);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping("testLogin")
    public String testLogin(@RequestParam(value = "code") String code) {
        return "success" + code;
    }

}
