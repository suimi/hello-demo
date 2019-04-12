package com.suimi.hello.hash;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author suimi
 * @date 2019/4/3
 */
public class HashSizeMark {

    Map<String, String> map = new HashMap<>();

    public void put(String key) {
        buildHash(key);
        if (map.containsKey(key)) {
            return;
        }
        map.put(key, key);
    }

    private int buildHash(String key) {
        int hashCode = key.hashCode();
        int index = hashCode & (Integer.MAX_VALUE);
        System.out.println(key + " index = " + index + " hashcode: " + hashCode);
        return hashCode;
    }

    public static void main(String[] args) {
        HashSizeMark mark = new HashSizeMark();
        mark.put("ab");
        mark.put("b");
        mark.put("ab");
        mark.put("c");
        mark.put("d");
        mark.put("e");
        mark.put("f");
        mark.put("ab");
        mark.put("c");
        mark.put("b");
    }
}
