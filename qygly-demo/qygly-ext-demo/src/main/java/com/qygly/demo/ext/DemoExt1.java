package com.qygly.demo.ext;

import com.qygly.ext.jar.helper.ExtJarHelper;
import com.qygly.shared.QyglyException;
import com.qygly.shared.ad.att.VarInfo;
import com.qygly.shared.ad.entity.EntityInfo;
import com.qygly.shared.ad.login.LoginInfo;
import com.qygly.shared.ad.sev.SevInfo;
import com.qygly.shared.interaction.EntityRecord;
import com.qygly.shared.interaction.InvokeActResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *
 */
public class DemoExt1 {
    /**
     * 逻辑扩展。
     * 用于：
     * 1、增删改（之前或之后）；
     * 2、操作（之前或之后）。
     * 在启动调试或正式部署后，当用户增删改相应的数据、或执行相应的操作，可以触发本逻辑扩展。
     */
    public void ext() {
        // 可先获取诸多上下文对象及工具：
        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();// 用于访问平台的数据库。
        SevInfo sevInfo = ExtJarHelper.sevInfo.get();// 获取单实体视图信息。
        EntityInfo entityInfo = sevInfo.entityInfo;// 获取实体信息。
        String entCode = entityInfo.code;// 获取实体代码。
        EntityRecord entityRecord = ExtJarHelper.entityRecordList.get().get(0);// 获取平台当前操作的数据（批量时可能多条）。
        Map<String, Object> valueMap = entityRecord.valueMap;// 获取数据的值Map。
        RestTemplate restTemplate = ExtJarHelper.restTemplate.get();// 用于向外部系统发送HTTP请求。
        StringRedisTemplate stringRedisTemplate = ExtJarHelper.stringRedisTemplate.get();// 用于访问平台的REDIS。
        Map<String, VarInfo> varInfoMap = ExtJarHelper.varInfoMap.get();// 获取操作的弹框的变量。
        LoginInfo loginInfo = ExtJarHelper.loginInfo.get();// 获取登录信息。

        // 逻辑处理：
        // 随机模拟成功和失败：
        long currentTimeMillis = System.currentTimeMillis();
        long mod = currentTimeMillis % 5;
        if (mod == 0) {
            // 若希望平台整体回滚，抛出异常即可：
            // 调试模式下，其实并不能实现整体回滚的效果；因为平台已经提交了，才进入到本地断点；本地也没有开启事务。
            // 部署到平台后，则可整体回滚。
            throw new QyglyException("故意的异常！");
        } else {
            // 修改数据：
            // 活用entCode、拼接SQL，可以实现同样一段代码在多处复用。
            // 若UPDATE语句用于插入之前、删除之后，那么当前数据这时并不存在于数据库中，UPDATE不到当前数据：
            int update = jdbcTemplate.update("UPDATE " + entCode + " T SET T.TEST_TEXT_LONG='测试的长文本！' WHERE T.ID=?", entityRecord.csCommId);
            // 若扩展用于插入之前、修改之前，可以不使用SQL语句来修改数据，直接放入值Map即可，会和平台自身的SQL作为1条执行：
            valueMap.put("TEST_TEXT_LONG", "测试的长文本！");

            System.out.println(update);
        }

        // 若扩展用于操作（之后），还可以设置返回的消息、是否刷新当前数据，等：
        if ("1".equals("2")) {
            InvokeActResult invokeActResult = new InvokeActResult();
            invokeActResult.msg = "我的操作成功了！";
            invokeActResult.reFetchData = true;// 刷新本页所有数据。不加这句，则只刷新选择的数据。
            ExtJarHelper.returnValue.set(invokeActResult);
        }
    }
}
