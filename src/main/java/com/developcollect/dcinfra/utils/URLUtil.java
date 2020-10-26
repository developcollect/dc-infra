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

    /**
     * 从url中获取文件名，也就是获取url中最后一个/后面的不带参数的值
     *
     * @param uriStr
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2020/10/26 10:23
     */
    public static String getFilename(String uriStr) {
        String path = getPath(uriStr);
        return path.substring(path.lastIndexOf("/"));
    }

}
