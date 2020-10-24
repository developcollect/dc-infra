package com.developcollect.dcinfra.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/10/22 15:49
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {


    public static Map<String, String> beanToStrMap(Object obj) {
        Map<String, Object> map = beanToMap(obj);
        if (map == null) {
            return null;
        }
        Map<String, String> strMap = new HashMap<>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            strMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return strMap;
    }

}
