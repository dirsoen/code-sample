package com.qygly.demo;

/**
 *
 */

import com.qygly.demo.dto.Result;
import com.qygly.ext.rest.helper.feign.client.DataServiceFeignClient;
import com.qygly.rpc.param.data.DeleteBySevParam;
import com.qygly.rpc.param.data.FetchBySevParam;
import com.qygly.rpc.param.data.InsertMultipleBySevParam;
import com.qygly.rpc.param.data.InsertSingleBySevParam;
import com.qygly.rpc.param.data.UpdateBySevParam;
import com.qygly.shared.QyglyResponseBody;
import com.qygly.shared.interaction.EntityRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 演示：增删改查。
 */
@RestController
public class DemoController1 {

    /**
     * 调用平台的增删改查的FeignClient。
     */
    @Autowired
    DataServiceFeignClient dataServiceFeignClient;


    /**
     * 插入单条。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/insertSingle
     */
    @GetMapping("insertSingle")
    public Result insertSingle() {

        InsertSingleBySevParam param = new InsertSingleBySevParam();
        param.sevId = "A2F1A9EF79F411E99795025041000001";// 测试学生的单实体视图的ID
        param.valueMap = new HashMap<>();
        param.valueMap.put("NAME", "学生");
        param.valueMap.put("TEST_INT", 123);
        param.valueMap.put("TEST_DOUBLE", 555.666);
        param.valueMap.put("TEST_BOOL", true);
        param.valueMap.put("TEST_DATE", "2020-12-31");
        param.valueMap.put("TEST_TIME", "11:12:13");
        param.valueMap.put("TEST_DTTM", "2020-12-31 11:12:13");

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<String> responseBody = dataServiceFeignClient.insertSingleBySev(param);

        String info = "已插入单条数据，ID为：" + responseBody.result;
        return new Result(info);
    }

    /**
     * 插入多条。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/insertMultiple
     */
    @GetMapping("insertMultiple")
    public Result insertMultiple() {

        InsertMultipleBySevParam param = new InsertMultipleBySevParam();
        param.sevId = "A2F1A9EF79F411E99795025041000001";// 测试学生的单实体视图的ID
        param.valueMapList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> valueMap = new HashMap<String, Object>();
            valueMap = new HashMap<String, Object>();
            param.valueMapList.add(valueMap);

            valueMap.put("CODE", "TEST");
            valueMap.put("NAME", "学生" + i);
        }

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<List<String>> responseBody = dataServiceFeignClient.insertMultipleBySev(param);

        String info = "已插入多条数据，ID列表为：" + responseBody.result;
        return new Result(info);
    }

    /**
     * 修改多条。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/update
     */
    @GetMapping("update")
    public Result update() {
        UpdateBySevParam param = new UpdateBySevParam();
        param.sevId = "A2F1A9EF79F411E99795025041000001";// 测试学生的单实体视图的ID
        // 可以传入记录的ID、以及WHERE语句，将共同过滤：
        param.id = null;
        param.additonalWhereClause = "T.CODE=? AND T.NAME LIKE CONCAT(?,'%')";
        param.additonalWhereClauseParamValueList = Arrays.asList("TEST", "学生");
        param.valueMap = new HashMap<>();
        param.valueMap.put("NAME", "学生" + "（已修改）");

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<Integer> responseBody = dataServiceFeignClient.updateBySev(param);

        String info = "已修改" + responseBody.result + "条数据。";
        return new Result(info);
    }

    /**
     * 删除多条。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/delete
     */
    @GetMapping("delete")
    public Result delete() {
        DeleteBySevParam param = new DeleteBySevParam();
        param.sevId = "A2F1A9EF79F411E99795025041000001";// 测试学生的单实体视图的ID
        // 可以传入记录的ID、以及WHERE语句，将共同过滤：
        param.id = null;
        param.additonalWhereClause = "T.CODE=? AND T.NAME LIKE CONCAT(?,'%')";
        param.additonalWhereClauseParamValueList = Arrays.asList("TEST", "学生");

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<Integer> responseBody = dataServiceFeignClient.deleteBySev(param);

        String info = "已删除" + responseBody.result + "条数据。";
        return new Result(info);
    }

    /**
     * 查询多条。
     * 启动后，通过GET如下地址访问：
     * http://localhost:11115/select
     */
    @GetMapping("select")
    public Result select() {
        FetchBySevParam param = new FetchBySevParam();
        param.sevId = "A2F1A9EF79F411E99795025041000001";// 测试学生的单实体视图的ID
        // 可以传入记录的ID、以及WHERE语句，将共同过滤：
        param.id = null;
        param.additonalWhereClause = "T.NAME=?";
        param.additonalWhereClauseParamValueList = Arrays.asList("学生");

        // 只要如下方法能够返回，就一定成功了；否则，会抛出异常：
        QyglyResponseBody<List<EntityRecord>> responseBody = dataServiceFeignClient.fetchBySev(param);

        List<EntityRecord> entityRecordList = responseBody.result;
        String info = "已查得" + (entityRecordList == null ? 0 : entityRecordList.size()) + "条数据。";
        return new Result(info);
    }

}