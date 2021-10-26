package com.qygly.demo;

/**
 *
 */

import com.qygly.shared.interaction.external.SsoValidationRequestBody;
import com.qygly.shared.interaction.external.SsoValidationResponseBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * SSO验证方控制器。
 */
@RestController
public class DemoController {

    @PostMapping("validate")
    public SsoValidationResponseBody validate(@RequestBody(required = true) SsoValidationRequestBody requestBody) {

        // 获取平台回传的凭证：
        String orgCode = requestBody.orgCode;
        String userCode = requestBody.userCode;
        String ssoToken = requestBody.ssoToken;

        // 进行验证。逻辑自行按需实现。

        SsoValidationResponseBody responseBody = new SsoValidationResponseBody();
        // 随机模拟成功和失败：
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis % 2 == 0) {
            // 验证失败：
            responseBody.errMsg = "验证失败了！";
            responseBody.succ = false;
        } else {
            // 验证成功：
            responseBody.succ = true;
        }
        return responseBody;
    }

}