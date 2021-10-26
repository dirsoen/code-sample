package com.qygly.demo;

/**
 *
 */

import com.qygly.demo.dto.Result;
import com.qygly.ext.rest.helper.feign.client.DictServiceFeignClient;
import com.qygly.rpc.param.dict.InvokeExtApiParam;
import com.qygly.shared.QyglyResponseBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示：调用扩展API。
 */
@RestController
public class DemoController3 {

    /**
     * 调用平台的扩展API的FeignClient。
     */
    @Autowired
    DictServiceFeignClient dictServiceFeignClient;

    /**
     * 调用扩展API。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/invokeExtApi
     */
    @GetMapping("invokeExtApi")
    public Result invokeExtApi() {

        InvokeExtApiParam param = new InvokeExtApiParam();
        param.extApiCode = "test";
        param.extApiParamMap = new HashMap<>(2);
        param.extApiParamMap.put("code", "123");
        param.extApiParamMap.put("name", "张三");

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<Map<String, Object>> responseBody = dictServiceFeignClient.invokeExtApi(param);

        String info = "扩展API返回值为：" + responseBody.result;
        return new Result(info);
    }

}