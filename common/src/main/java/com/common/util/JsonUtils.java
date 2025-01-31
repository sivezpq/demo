package com.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtils {
    /**
     * 校验字符串是否是json
     *
     * @param jsonStr jsonStr
     * @return
     */
    public static boolean isJson(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }
}
