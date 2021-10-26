package com.qygly.demo.ext;

import com.qygly.ext.jar.helper.ExtJarHelper;
import com.qygly.ext.jar.helper.util.JsonUtil;

import java.util.Date;
import java.util.Map;

/**
 *
 */
public class DemoExt2 {

    /**
     * 逻辑扩展。
     * 用于扩展API接口。
     * 在启动调试或正式部署后，当外部程序先登录（及保持心跳）、再通过POST访问/qygly-dict/invokeExtApi，可以触发本逻辑扩展。通过FeignClient可以以Java形式方便调用。
     */
    public void ext() {
        // 获取输入：
        Map<String, Object> map = ExtJarHelper.extApiParamMap.get();// 输入参数的map。
        String json = JsonUtil.toJson(map);
        Input input = JsonUtil.fromJson(json, Input.class);

        // 逻辑处理：
        Output output = new Output();
        output.codeName = input.code + "-" + input.name;
        output.birthDate = new Date();
        output.male = true;
        output.height = 1.75;

        // 返回输出：
        // 转换为Map再设置到返回值；若直接将对象设置到返回值，调试时（通过MQ返回给平台）可能无法解析出相应的类：
        Map outputMap = JsonUtil.fromJson(JsonUtil.toJson(output), Map.class);
        ExtJarHelper.returnValue.set(outputMap);
    }

    /**
     * 输入的类。
     */
    public static class Input {
        public String code;
        public String name;
    }

    /**
     * 输出的类。
     */
    public static class Output {
        public String codeName;
        public Date birthDate;
        public Boolean male;
        public Double height;
    }
}
