package com.qygly.demo.ext;

import com.qygly.ext.jar.helper.ExtJarHelper;
import com.qygly.shared.ad.entity.EntityInfo;
import com.qygly.shared.ad.sev.SevInfo;
import com.qygly.shared.interaction.EntityRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xuzhifeng
 * @Date 2021/10/27 3:26 下午
 */
public class WorkNoGenerationExt {

    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String BDZJ_ORKNOPREFIX = "BDZJ";
    public static final String CGZJ_ORKNOPREFIX = "CGZJ";
    public static final String JHMB_ORKNOPREFIX = "JHMB";

    public void createBDWorkNo(){
        createWorkNo(BDZJ_ORKNOPREFIX, "CODE");
    }

    public void createCGWorkNo(){
        createWorkNo(CGZJ_ORKNOPREFIX, "CODE");
    }

    public void createBJHWorkNo(){
        createWorkNo(JHMB_ORKNOPREFIX, "CODE");
    }

    private void createWorkNo(String workNoPrefix, String fieldName) {
        List<EntityRecord> entityRecordList = ExtJarHelper.entityRecordList.get();
        StringRedisTemplate redisTemplate = ExtJarHelper.stringRedisTemplate.get();

//        SevInfo sevInfo = ExtJarHelper.sevInfo.get();
//        EntityInfo entityInfo = sevInfo.entityInfo;
//        String code = entityInfo.code;

        //编号初始化
        LocalDateTime now = LocalDateTime.now();
        Integer num = 0;
        String workNo = getWorkNoPrefix(workNoPrefix, now);

        //加锁
        String redisNum = redisTemplate.opsForValue().get(workNo);
        if (Objects.isNull(redisNum)){
            //当天首次生成设置redis
            redisTemplate.opsForValue().set(workNo, num.toString(), getExpireAtTime(now), TimeUnit.MILLISECONDS);
        } else {
            num = Integer.valueOf(redisNum);
            ++num;
            //更新redis对应数值
            redisTemplate.opsForValue().set(workNo, num.toString(), 0);
        }
        //生成业务编号
        workNo = getWorkNo(workNo, num);

        if (entityRecordList != null) {
            for (EntityRecord entityRecord : entityRecordList) {
                entityRecord.valueMap.put(fieldName, workNo);
                entityRecord.extraEditableAttCodeList = new ArrayList<>();
                entityRecord.extraEditableAttCodeList.add(fieldName);
            }
        }
    }

    private String getWorkNoPrefix(String workNoPrefix, LocalDateTime now){
        return workNoPrefix+ now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD));
    }


    private Long getExpireAtTime(LocalDateTime now){
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ChronoUnit.MILLIS.between(LocalDateTime.now(),midnight);
    }

    private String getWorkNo(String workNoPrefix,Integer num) {
        return num < 1000 ? workNoPrefix + String.format("%03d", num) : workNoPrefix + num.toString();
    }
}
