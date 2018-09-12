package com.lyl.husky.core.executor.handler;

import com.lyl.husky.core.executor.handler.impl.DefaultExecutorServiceHandler;
import com.lyl.husky.core.executor.handler.impl.DefaultJobExceptionHandler;
import com.lyl.husky.core.util.json.GsonFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public final class JobProperties {

    private EnumMap<JobPropertiesEnum, String> map = new EnumMap<>(JobPropertiesEnum.class);

    /**
     * 设置作业属性
     */
    public void put(final String key, final String value){
        JobPropertiesEnum jobPropertiesEnum = JobPropertiesEnum.from(key);
        if (null == jobPropertiesEnum || null == value){
            return;
        }
        map.put(jobPropertiesEnum, value);
    }

    /**
     * 获取作业属性
     */
    public String get(final JobPropertiesEnum jobPropertiesEnum){
        return map.containsKey(jobPropertiesEnum) ? map.get(jobPropertiesEnum) : jobPropertiesEnum.getDefauleValue();
    }

    /**
     * 获取所有键
     */
    public String json(){
        Map<String, String> jsonMap = new LinkedHashMap<>(JobPropertiesEnum.values().length, 1);
        for (JobPropertiesEnum each : JobPropertiesEnum.values()){
            jsonMap.put(each.getKey(), get(each));
        }
        return GsonFactory.getGson().toJson(jsonMap);
    }

    /**
     * 作业属性枚举
     */
    @RequiredArgsConstructor
    @Getter
    public enum JobPropertiesEnum{

        /**
         * 作业异常处理器
         */
        JOB_EXCEPTION_HANDLER("job_exception_handler", JobExceptionHandler.class, DefaultJobExceptionHandler.class.getCanonicalName()),

        /**
         * 线程池服务处理器.
         */
        EXECUTOR_SERVICE_HANDLER("executor_service_handler", ExecutorServiceHandler.class, DefaultExecutorServiceHandler.class.getCanonicalName());


        private final String key;
        private final Class<?> classType;
        private final String defauleValue;

        /**
         * 通过属性键获取枚举
         */
        public static JobPropertiesEnum from(final String key) {
            for (JobPropertiesEnum each : JobPropertiesEnum.values()) {
                if (each.getKey().equals(key)) {
                    return each;
                }
            }
            return null;
        }

    }

}
