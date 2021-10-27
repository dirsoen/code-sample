package com.qygly.demo.ext;

import com.qygly.ext.jar.helper.ExtJarHelper;
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

    public void createWorkNo(String workNoPrefix) {
        List<EntityRecord> entityRecordList = ExtJarHelper.entityRecordList.get();
        StringRedisTemplate redisTemplate = ExtJarHelper.stringRedisTemplate.get();

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
        workNo = getWorkNo(workNoPrefix, num);

        if (entityRecordList != null) {
            for (EntityRecord entityRecord : entityRecordList) {
                entityRecord.valueMap.put("CODE", workNo);

                entityRecord.extraEditableAttCodeList = new ArrayList<>();
                entityRecord.extraEditableAttCodeList.add("CODE");
            }
        }
    }

    private String getWorkNoPrefix(String workNoPrefix, LocalDateTime now){
        return workNoPrefix+ now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD));
    }

    private String getWorkNo(String workNoPrefix,Integer num) {
        return workNoPrefix + String.format("%03d", num);
    }

    private Long getExpireAtTime(LocalDateTime now){
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ChronoUnit.MILLIS.between(LocalDateTime.now(),midnight);
    }
}
