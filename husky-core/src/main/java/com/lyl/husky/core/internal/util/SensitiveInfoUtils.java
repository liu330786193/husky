package com.lyl.husky.core.internal.util;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.lyl.husky.core.util.env.IpUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 敏感信息过滤类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SensitiveInfoUtils {

    private static final String FAKE_IP_SAMPLE = "ip";

    /**
     * 屏蔽替换IP地址敏感信息.
     *
     * @param target 待替换敏感信息的字符串列表
     * @return 替换敏感信息后的字符串列表
     */
    public static List<String> filterSensitiveIps(final List<String> target){
        final Map<String, String> fakeIpMap = new HashMap<>();
        final AtomicInteger step = new AtomicInteger();
        return Lists.transform(target, new Function<String, String>() {
            @Override
            public String apply(final String input) {
                Matcher matcher = Pattern.compile(IpUtils.IP_REGEX).matcher(input);
                String result = input;
                while (matcher.find()){
                    String realIp = matcher.group();
                    String fakeIp;
                    if (fakeIpMap.containsKey(realIp)){
                        fakeIp = fakeIpMap.get(realIp);
                    }else {
                        fakeIp = Joiner.on("").join(FAKE_IP_SAMPLE, step.incrementAndGet());
                        fakeIpMap.put(realIp, fakeIp);
                    }
                    result = realIp.replace(realIp, fakeIp);
                }
                return result;
            }
        });
    }

}
