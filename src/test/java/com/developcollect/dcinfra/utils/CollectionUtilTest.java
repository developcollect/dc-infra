package com.developcollect.dcinfra.utils;

import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.developcollect.dcinfra.utils.CollectionUtil.*;
import static org.junit.Assert.*;

public class CollectionUtilTest {


    @Test
    public void testShadowSort2() {
        List<Map<String, String>> list1 = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("i", Integer.toString(i));
            map.put("dc", "wi");
            list1.add(map);
        }

        List<Map<String, String>> list2 = new ArrayList<>();
        for (int i = 3; i < 15; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("i", Integer.toString(i));
            map.put("dc", "wi");
            list2.add(map);
        }
        shuffle(list2);


        List<Map<String, String>> maps = shadowSort2(list1, map -> map.get("i"), list2, map -> map.get("i"));

        assertEquals(list2.get(0).get("i"), "3");
        assertEquals(list2.get(1).get("i"), "4");
        assertEquals(list2.get(2).get("i"), "5");
        assertEquals(list2.get(3).get("i"), "6");
        assertEquals(list2.get(4).get("i"), "7");
        assertEquals(list2.get(5).get("i"), "8");
        assertEquals(list2.get(6).get("i"), "9");
        assertEquals(list2.get(7).get("i"), "10");


        System.out.println(1);
    }

}