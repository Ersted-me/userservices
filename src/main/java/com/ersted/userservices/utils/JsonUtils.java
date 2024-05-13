package com.ersted.userservices.utils;

import com.ersted.userservices.adapter.LocalDateTimeTypeAdapter;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtils {
    private static Map<String, Object> nestedMap(String[] keys, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        if (keys.length == 1) {
            map.put(keys[0], value);
            return map;
        }
        map.put(keys[0], nestedMap(Arrays.copyOfRange(keys, 1, keys.length), value));
        return map;
    }

    public static String diffJsonLikeString(Object left, Object right) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        String leftJson = gson.toJson(left);
        String rightJson = gson.toJson(right);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();

        Map<String, Object> leftMap = gson.fromJson(leftJson, type);
        Map<String, Object> rightMap = gson.fromJson(rightJson, type);

        Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
        Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);
        MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);

        Map<String, Object> diffMap = difference.entriesDiffering().entrySet().stream().map(entry -> {
                    String[] keys = entry.getKey().split("/");
                    Object value = entry.getValue().leftValue();
                    return JsonUtils.nestedMap(keys, value);
                })
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, o2) -> {
                    ((Map) o).putAll((Map) o2);
                    return o;
                }));
        return gson.toJson(diffMap);
    }
}
/*

difference.entriesDiffering().entrySet().stream().map(entry -> {
    String[] keys = entry.getKey().split("/");
    MapDifference.ValueDifference<Object> value = entry.getValue();
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> temp = new HashMap<>();
    Object tempValue = null;
    for(int index = keys.length; index > 0 ; index--){
        new HashMap<>().put(keys[index], value);

    }
    return result;
}).collect(Collectors.toList());
*/