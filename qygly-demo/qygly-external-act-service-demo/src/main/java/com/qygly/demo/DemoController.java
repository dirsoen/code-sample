package com.qygly.demo;

/**
 *
 */

import com.qygly.shared.interaction.InvokeActResult;
import com.qygly.shared.interaction.external.ExternalActRequestBody;
import com.qygly.shared.interaction.external.ExternalActResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部操作控制器。
 */
@RestController
public class DemoController {

//    旧版：
//    @PostMapping("doAct")
//    public RemoteActResponseBody doAct(@RequestBody RemoteActRequestBody requestBody) {
//
//        // 1、从参数requestBody，可以获取到诸多上下文信息。具体的诸多属性可以调试进来看看。如：
//        System.out.println(requestBody.loginInfo.getUser().getId());//当前用户id。
//        System.out.println(requestBody.entityRecordList);//当前选择的记录列表，若没选则为空
//        System.out.println(requestBody.entityRecordList.get(0).getNewId());//当前选择的记录列表的第1条的id。
//        System.out.println(requestBody.entityRecordList.get(0).getNewValue("TEST_CLS_ID"));//当前选择的记录列表的第1条的TEST_CLS_ID属性的值。
//        System.out.println(requestBody.entityRecordList.get(0).getNewDisplayText("TEST_CLS_ID"));//当前选择的记录列表的第1条的TEST_CLS_ID属性的显示文本。
//
//        // 返回操作失败信息，让平台的数据库进行回滚，并弹框显示定制错误信息：
////        RemoteActResponseBody responseBody = new RemoteActResponseBody();
////        responseBody.succ = false;
////        responseBody.errMsg = "我的外部操作报错了！";
//
//        // 返回操作成功信息，让平台的数据库进行提交，并弹框显示定制成功信息：
//        RemoteActResponseBody responseBody = new RemoteActResponseBody();
//        responseBody.succ = true;
//        responseBody.data = new InvokeActProcResult();
//        responseBody.data.setMsgList(new ArrayList<>());
//        responseBody.data.getMsgList().add("我的远程操作成功了（第1行信息）！");
//        responseBody.data.getMsgList().add("我的远程操作成功了（第2行信息）！");
//
//        return responseBody;
//    }

    //    新版：
    @PostMapping("doAct")
    public ExternalActResponseBody doAct(@RequestBody(required = true) ExternalActRequestBody requestBody) {

        // 1、从参数requestBody，可以获取到诸多上下文信息。具体的诸多属性可以调试进来看看。如：
        System.out.println(requestBody.loginInfo.userId);//当前用户id。
        System.out.println(requestBody.entityRecordList);//当前选择的记录列表，若没选则为空。
        System.out.println(requestBody.entityRecordList.get(0).csCommId);//当前选择的记录列表的第1条的id。
        System.out.println(requestBody.entityRecordList.get(0).valueMap.get("TEST_CLS_ID"));//当前选择的记录列表的第1条的TEST_CLS_ID属性的值。
        // 新版，为了性能提升，textMap没有回传过来，从而始终为null；若调用textMap.get("TEST_CLS_ID")会触发NullPointerException异常：
        // System.out.println(requestBody.entityRecordList.get(0).textMap.get("TEST_CLS_ID"));//当前选择的记录列表的第1条的TEST_CLS_ID属性的显示文本。

        ExternalActResponseBody responseBody = new ExternalActResponseBody();

        // 随机模拟成功和失败：
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis % 2 == 0) {
            // 返回操作失败信息，让平台的数据库进行回滚，并弹框显示定制错误信息：
            responseBody.succ = false;
            responseBody.errMsg = "我的外部操作报错了！";
        } else {
            // 返回操作成功信息，让平台的数据库进行提交，并弹框显示定制成功信息：
            responseBody.succ = true;
            responseBody.data = new InvokeActResult();
            responseBody.data.msg = "我的外部操作成功了！"
                    + "用户ID：" + requestBody.loginInfo.userId
                    + "，记录ID：" + requestBody.entityRecordList.get(0).csCommId
                    + "，属性TEST_SCHOOL_ID值：" + requestBody.entityRecordList.get(0).valueMap.get("TEST_CLS_ID")
            ;
        }
        return responseBody;
    }

}