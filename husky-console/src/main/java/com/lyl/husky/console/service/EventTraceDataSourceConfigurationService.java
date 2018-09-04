/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.lyl.husky.console.service;

import com.lyl.husky.console.domain.EventTraceDataSourceConfiguration;
import com.lyl.husky.console.domain.EventTraceDataSourceConfigurations;

import java.util.Optional;

/**
 * 事件追踪数据源配置服务.
 */
public interface EventTraceDataSourceConfigurationService {
    
    /**
     * 读取全部事件追踪数据源配置.
     */
    EventTraceDataSourceConfigurations loadAll();
    
    /**
     * 读取事件追踪数据源配置.
     */
    EventTraceDataSourceConfiguration load(String name);
    
    /**
     * 查找事件追踪数据源配置.
     */
    EventTraceDataSourceConfiguration find(final String name, final EventTraceDataSourceConfigurations configs);
    
    /**
     * 读取已连接的事件追踪数据源配置.
     */
    Optional<EventTraceDataSourceConfiguration> loadActivated();
    
    /**
     * 添加事件追踪数据源配置.
     */
    boolean add(EventTraceDataSourceConfiguration config);
    
    /**
     * 删除事件追踪数据源配置.
     */
    void delete(String name);
}
