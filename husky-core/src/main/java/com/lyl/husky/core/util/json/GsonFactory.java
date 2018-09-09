package com.lyl.husky.core.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * Gson构建器
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonFactory {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();
    private static volatile Gson gson = GSON_BUILDER.create();

    /**
     * 注册GSON解析对象
     */
    public static synchronized void registerTypeAdapter(final Type type, final TypeAdapter typeAdapter){
        GSON_BUILDER.registerTypeAdapter(type, typeAdapter);
        gson = GSON_BUILDER.create();
    }

    /**
     * 获取Gson实例
     */
    public static Gson getGson(){
        return gson;
    }


}
