package com.qygly.demo;

/**
 *
 */

import com.qygly.demo.dto.Result;
import com.qygly.ext.rest.helper.feign.client.DataServiceFeignClient;
import com.qygly.rpc.param.data.InvokeActByCodeParam;
import com.qygly.rpc.param.data.InvokeActParam;
import com.qygly.shared.QyglyResponseBody;
import com.qygly.shared.interaction.InvokeActResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 演示：调用操作。
 */
@RestController
public class DemoController2 {

    /**
     * 调用平台的操作的FeignClient。
     */
    @Autowired
    DataServiceFeignClient dataServiceFeignClient;

    /**
     * 调用操作。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/invokeAct
     */
    @GetMapping("invokeAct")
    public Result invokeAct() {
        InvokeActParam param = new InvokeActParam();
        param.sevId = "D2A0CC6278FB11E9B7A0025041000001";// “测试班级”单实体视图的ID
        param.actId = "E6EF29B7597A11EB988900269E0B548C";// “测试生成5个学生”操作的ID
        // 若操作与选择无关，则不传idList；若操作与选择相关，则要传idList：
        param.idList = new ArrayList<>(1);
        param.idList.add("0C9EF91A79C111E99795025041000001");
        // 若单实体实体启用了“标准版本检查”、且操作与选择相关，则还要传verList：
//        param.verList = new ArrayList<>();
//        param.verList.add(1);

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<InvokeActResult> responseBody = dataServiceFeignClient.invokeAct(param);

        String info = "调用操作的返回值为：" + responseBody.result.msg;
        return new Result(info);
    }

    /**
     * 调用操作（根据代码）。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/invokeActByCode
     */
    @GetMapping("invokeActByCode")
    public Result invokeActByCode() {
        InvokeActByCodeParam param = new InvokeActByCodeParam();
        param.sevCode = "DEFAULT_SEV_FOR_TEST_CLS";// “测试班级”单实体视图的代码
        param.actCode = "TEST_1";// “测试生成5个学生”操作的代码
        // 若操作与选择无关，则不传idList；若操作与选择相关，则要传idList：
        param.idList = new ArrayList<>(1);
        param.idList.add("0C9EF91A79C111E99795025041000001");
        // 若单实体实体启用了“标准版本检查”、且操作与选择相关，则还要传verList：
//        param.verList = new ArrayList<>();
//        param.verList.add(1);

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<InvokeActResult> responseBody = dataServiceFeignClient.invokeActByCode(param);

        String info = "调用操作的返回值为：" + responseBody.result.msg;
        return new Result(info);
    }

}