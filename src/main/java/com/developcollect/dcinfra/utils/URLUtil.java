package com.developcollect.dcinfra.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Map;

/**
 * @author zak
 * @since 1.0.0
 */
public class URLUtil extends cn.hutool.core.util.URLUtil {

    private URLUtil() {
    }


    public static <P> String splice(String baseUrl, P params) {
        if (StrUtil.isBlank(baseUrl)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            sb.append("http://").append(baseUrl);
        } else {
            sb.append(baseUrl);
        }
        if (params == null) {
            return sb.toString();
        }
        Map<? extends Object, ? extends Object> paramMap;
        if (params instanceof Map) {
            paramMap = (Map) params;
        } else {
            paramMap = BeanUtil.beanToMap(params);
        }
        sb.append("?");
        for (Map.Entry<?, ?> entry : paramMap.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

}
