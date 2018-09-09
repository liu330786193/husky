package com.lyl.husky.core.internal.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lyl.husky.core.config.JobTypeConfiguration;
import com.lyl.husky.core.config.LiteJobConfiguration;
import com.lyl.husky.core.util.json.AbstractJobConfigurationGsonTypeAdapter;
import com.lyl.husky.core.util.json.GsonFactory;
import jdk.nashorn.internal.scripts.JO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.JobName;
import java.io.IOException;
import java.util.Map;

import static com.lyl.husky.core.internal.config.LiteJobConfigurationConstants.*;

/**
 * Lite作业配置的Gson工厂
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LiteJobConfigurationGsonFactory {

    static {
        GsonFactory.registerTypeAdapter(LiteJobConfiguration.class, new LiteJobConfigurationGsonTypeAdapter());
    }

    /**
     * 将作业配置转换为JSON字符串.
     *
     * @param liteJobConfig 作业配置对象
     * @return 作业配置JSON字符串
     */
    public static String toJson(final LiteJobConfiguration liteJobConfig) {
        return GsonFactory.getGson().toJson(liteJobConfig);
    }

    /**
     * 将作业配置转换为JSON字符串.
     *
     * @param liteJobConfig 作业配置对象
     * @return 作业配置JSON字符串
     */
    public static String toJsonForObject(final Object liteJobConfig) {
        return GsonFactory.getGson().toJson(liteJobConfig);
    }

    /**
     * 将JSON字符串转换为作业配置.
     *
     * @param liteJobConfigJson 作业配置JSON字符串
     * @return 作业配置对象
     */
    public static LiteJobConfiguration fromJson(final String liteJobConfigJson) {
        return GsonFactory.getGson().fromJson(liteJobConfigJson, LiteJobConfiguration.class);
    }

    /**
     * Lite作业配置的Json转换配置器
     */
    static final class LiteJobConfigurationGsonTypeAdapter extends AbstractJobConfigurationGsonTypeAdapter<LiteJobConfiguration>{

        @Override
        protected void addToCustomizedValueMap(String jsonName, JsonReader in, Map<String, Object> customizedValueMap) throws IOException {
            switch (jsonName){
                case MONITOR_EXECUTION:
                    customizedValueMap.put(jsonName, in.nextBoolean());
                case MAX_TIME_DIFF_SECONDS:
                    customizedValueMap.put(jsonName, in.nextInt());
                case MONITOR_PORT:
                    customizedValueMap.put(jsonName, in.nextInt());
                    break;
                case JOB_SHARDING_STRATEGY_CLASS:
                    customizedValueMap.put(jsonName, in.nextString());
                    break;
                case RECONCILE_INTERVAL_MINUTES:
                    customizedValueMap.put(jsonName, in.nextInt());
                    break;
                case DISABLED:
                    customizedValueMap.put(jsonName, in.nextBoolean());
                    break;
                case OVERWRITE:
                    customizedValueMap.put(jsonName, in.nextBoolean());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }

        @Override
        protected LiteJobConfiguration getJobRootConfiguration(JobTypeConfiguration typeConfig, Map<String, Object> customizedValueMap) {
            LiteJobConfiguration.Builder builder = LiteJobConfiguration.newBuilder(typeConfig);
            if (customizedValueMap.containsKey(MONITOR_EXECUTION)) {
                builder.monitorExecution((boolean) customizedValueMap.get(MONITOR_EXECUTION));
            }
            if (customizedValueMap.containsKey(MAX_TIME_DIFF_SECONDS)) {
                builder.maxTimeDiffSeconds((int) customizedValueMap.get(MAX_TIME_DIFF_SECONDS));
            }
            if (customizedValueMap.containsKey(MONITOR_PORT)) {
                builder.monitorPort((int) customizedValueMap.get(MONITOR_PORT));
            }
            if (customizedValueMap.containsKey(JOB_SHARDING_STRATEGY_CLASS)) {
                builder.jobShardingStrategyClass((String) customizedValueMap.get(JOB_SHARDING_STRATEGY_CLASS));
            }
            if (customizedValueMap.containsKey(RECONCILE_INTERVAL_MINUTES)) {
                builder.reconcileIntervalMinutes((int) customizedValueMap.get(RECONCILE_INTERVAL_MINUTES));
            }
            if (customizedValueMap.containsKey(DISABLED)) {
                builder.disabled((boolean) customizedValueMap.get(DISABLED));
            }
            if (customizedValueMap.containsKey(OVERWRITE)) {
                builder.overwrite((boolean) customizedValueMap.get(OVERWRITE));
            }
            return builder.build();
        }

        @Override
        protected void writeCustomized(JsonWriter out, LiteJobConfiguration value) throws IOException {
            out.name(MONITOR_EXECUTION).value(value.isMonitorExecution());
            out.name(MAX_TIME_DIFF_SECONDS).value(value.getMaxTimeDiffSeconds());
            out.name(MONITOR_PORT).value(value.getMonitorPort());
            out.name(JOB_SHARDING_STRATEGY_CLASS).value(value.getJobShardingStrategyClass());
            out.name(RECONCILE_INTERVAL_MINUTES).value(value.getReconcileIntervalMinutes());
            out.name(DISABLED).value(value.isDisabled());
            out.name(OVERWRITE).value(value.isOverwrite());
        }
    }

}
