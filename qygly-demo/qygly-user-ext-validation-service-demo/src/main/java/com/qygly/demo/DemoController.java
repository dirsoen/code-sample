package com.qygly.demo;

/**
 *
 */

import com.qygly.shared.interaction.external.UserExtValidationRequestBody;
import com.qygly.shared.interaction.external.UserExtValidationResponseBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部验证控制器。
 */
@RestController
public class DemoController {
    @PostMapping("validate")
    public UserExtValidationResponseBody validate(@RequestBody(required = true) UserExtValidationRequestBody requestBody) {

        String username = requestBody.username;// 拿到用户名
        String password = requestBody.password;// 拿到密码（明文）

        UserExtValidationResponseBody responseBody = new UserExtValidationResponseBody();

        // 随机模拟成功和失败：
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis % 2 == 0) {
            responseBody.succ = false;
            responseBody.errMsg = "因为某些原因，您的登录失败了！故意显示的用户名：" + username + "，密码：" + password;
        } else {
            responseBody.succ = true;
        }
        return responseBody;
    }

}