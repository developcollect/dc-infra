package com.developcollect.dcinfra.utils.spring;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

/**
 * @author zak
 * @since 1.0.0
 */
@Slf4j
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class SpringMvcUtil {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;



    @Value("${spring.application.name}")
    private String applicationName;

    private static HttpServletResponse RESPONSE;
    private static HttpServletRequest REQUEST;
    private static String APPLICATION_NAME;


    static ThreadLocal<String> mappingThreadLocal = new ThreadLocal<>();


    @PostConstruct
    private void init() {
        REQUEST = request;
        RESPONSE = response;
        APPLICATION_NAME = applicationName.toUpperCase();
    }





//    public static String getClientId(HttpServletRequest request) {
//        return getClientId(getToken(request));
//    }

//    public static String getClientId(String token) {
//        Map<String, Claim> claims = JwtUtil.getClaims(token);
//        String clientId = Optional.ofNullable(claims)
//                .map(c -> c.get("client_id"))
//                .map(Claim::asString)
//                .orElse(null);
//        return clientId;
//    }

    public static String getMethod(HttpServletRequest request) {
        return request.getMethod().toUpperCase();
    }

    public static void addCookie(String key, String value) {
        addCookie(key, value, "/");
    }

    public static void addCookie(String key, String value, String path) {
        addCookie(key, value, path, null);
    }

    public static void addCookie(String key, String value, String path, Integer maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        addCookie(cookie);
    }

    public static void addCookie(String key, String value, Integer maxAge) {
        addCookie(key, value, "/", maxAge);
    }

    public static void addCookie(String key, String value, Duration duration) {
        Integer maxAge = duration == null ? null : (int) (duration.toMillis() / 1000);
        addCookie(key, value, "/", maxAge);
    }

    public static void addCookie(Cookie cookie) {
        if (StrUtil.isBlank(cookie.getPath())) {
            cookie.setPath("/");
        }
        getResponse().addCookie(cookie);
    }

    public static String getCookie(String key) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        Cookie[] cookies = getRequest().getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void delCookie(String key) {
        if (StrUtil.isBlank(key)) {
            return;
        }
        Cookie[] cookies = getRequest().getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                addCookie(cookie);
                return;
            }
        }
        return;
    }




    /**
     * 获取当前的request对象
     * 要注意的是在调用该方法的线程如果和controller执行线程不是同一个线程的话
     * 获取的request是无效的
     *
     * @param
     * @return javax.servlet.http.HttpServletRequest
     * @author zak
     * @date 2019/10/28 11:37
     */
    public static HttpServletRequest getRequest() {
        return REQUEST;
    }


    /**
     * 获取当前的response对象
     * 要注意的是在调用该方法的线程如果和controller执行线程不是同一个线程的话
     * 获取的response是无效的
     *
     * @param
     * @return javax.servlet.http.HttpServletRequest
     * @author zak
     * @date 2019/10/28 11:37
     */
    public static HttpServletResponse getResponse() {
        return RESPONSE;
    }

    /**
     * 获取当前接口的ContextPath
     *
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:21
     */
    public static String currContextPath() {
        return getRequest().getContextPath();
    }

    /**
     * 获取当前接口的映射路径
     *
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:22
     */
    public static String currMappingPath() {
        if (mappingThreadLocal.get() == null) {
            String mappingPattern = null;
            Map<String, DispatcherServlet> dispatcherServletMap = SpringUtil.getBeansOfType(DispatcherServlet.class);
            for (DispatcherServlet dispatcherServlet : dispatcherServletMap.values()) {
                List<HandlerMapping> handlerMappings = dispatcherServlet.getHandlerMappings();
                mappingPattern = fetchMappingPattern(handlerMappings, getRequest());
                if (mappingPattern != null) {
                    break;
                }
            }
            if (mappingPattern == null) {
                mappingPattern = currServletPath();
            }
            mappingThreadLocal.set(mappingPattern);
        }
        return mappingThreadLocal.get();
    }

    public static String getModuleMappingPath(HttpServletRequest request) {
        String mappingPattern = null;
        Map<String, DispatcherServlet> dispatcherServletMap = SpringUtil.getBeansOfType(DispatcherServlet.class);
        for (DispatcherServlet dispatcherServlet : dispatcherServletMap.values()) {
            List<HandlerMapping> handlerMappings = dispatcherServlet.getHandlerMappings();
            mappingPattern = fetchMappingPattern(handlerMappings, request);
            if (mappingPattern != null) {
                break;
            }
        }
        if (mappingPattern == null) {
            mappingPattern = currServletPath();
        }
        return currModulePrefix() + mappingPattern;
    }

    /**
     * 获取当前接口含模块名的映射路径
     *
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:22
     */
    public static String currModuleMappingPath() {
        return currModulePrefix() + currMappingPath();
    }


    /**
     * 获取匹配的路径
     * (模拟dispatcherServlet)
     *
     * @param handlerMappings
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 14:16
     */
    private static String fetchMappingPattern(List<HandlerMapping> handlerMappings, HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                if (handlerMapping instanceof AbstractHandlerMethodMapping) {
                    AbstractHandlerMethodMapping ahm = (AbstractHandlerMethodMapping) handlerMapping;
                    String lookupPath = ahm.getUrlPathHelper().getLookupPathForRequest(request);
                    // AbstractHandlerMethodMapping.MappingRegistry
                    Object mappingRegistry = ReflectUtil.invoke(ahm, "getMappingRegistry");
                    List<RequestMappingInfo> mappingInfos = ReflectUtil.invoke(mappingRegistry, "getMappingsByUrl", lookupPath);
                    if (mappingInfos == null) {
                        Map<RequestMappingInfo, HandlerMethod> mappings = ReflectUtil.invoke(mappingRegistry, "getMappings");
                        Set<RequestMappingInfo> requestMappingInfos = mappings.keySet();
                        Iterator<RequestMappingInfo> iterator = requestMappingInfos.iterator();
                        while (iterator.hasNext()) {
                            RequestMappingInfo mappingInfo = iterator.next().getMatchingCondition(request);
                            String pattern = fetchPatternFromRequestMappingInfo(mappingInfo);
                            if (pattern != null) {
                                return pattern;
                            }
                        }
                    }
                    if (mappingInfos != null) {
                        for (RequestMappingInfo mappingInfo : mappingInfos) {
                            String pattern = fetchPatternFromRequestMappingInfo(mappingInfo);
                            if (pattern != null) {
                                return pattern;
                            }
                        }
                    }

                } else if (handlerMapping instanceof AbstractUrlHandlerMapping) {
                    AbstractUrlHandlerMapping urlHandlerMapping = (AbstractUrlHandlerMapping) handlerMapping;
                    String lookupPath = urlHandlerMapping.getUrlPathHelper().getLookupPathForRequest(request);
                    if (urlHandlerMapping.getHandlerMap().containsKey(lookupPath)) {
                        return lookupPath;
                    }
                    for (String registeredPattern : urlHandlerMapping.getHandlerMap().keySet()) {
                        if (urlHandlerMapping.getPathMatcher().match(registeredPattern, lookupPath)) {
                            return registeredPattern;
                        } else if (urlHandlerMapping.useTrailingSlashMatch()) {
                            if (!registeredPattern.endsWith("/") && urlHandlerMapping.getPathMatcher().match(registeredPattern + "/", lookupPath)) {
                                return registeredPattern;
                            }
                        }
                    }
                }
            } catch (Throwable ignore) {
            }
        }

        return null;
    }


    private static String fetchPatternFromRequestMappingInfo(RequestMappingInfo requestMappingInfo) {
        Set<String> patterns = Optional
                .ofNullable(requestMappingInfo)
                .map(RequestMappingInfo::getPatternsCondition)
                .map(PatternsRequestCondition::getPatterns)
                .orElse(Collections.emptySet());
        Iterator<String> iterator = patterns.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * 获取当前访问的接口
     *
     * @param
     * @return java.lang.String
     * @author zak
     * @date 2019/11/26 15:16
     */
    public static String currServletPath() {
        String servletPath = getRequest().getServletPath();
        return servletPath;
    }

    /**
     * 获取当前访问的接口
     * 因为前端调用的接口要经过网关转发, 所以实际访问的接口前面有一个当前服务的名称
     * @param
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:23
     */
    public static String currModulePath() {
        return currModulePrefix() + currServletPath();
    }

    /**
     * 获取当前服务的前缀
     *
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:23
     */
    public static String currModulePrefix() {
        return "/" + APPLICATION_NAME;
    }

    /**
     * 获取当前接口的调用方法
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:23
     */
    public static String currMethod() {
        return getMethod(getRequest());
    }

//    /**
//     * 获取当前的token
//     *
//     * @return java.lang.String
//     * @author zak
//     * @date 2020/6/17 15:23
//     */
//    public static String currToken() {
//        return getToken(getRequest());
//    }

//    /**
//     * 获取当前的clientId
//     * 这是指的Oauth2中的clientId
//     * @return java.lang.String
//     * @author zak
//     * @date 2020/6/17 15:23
//     */
//    public static String currClientId() {
//        return getClientId(getRequest());
//    }


//    /**
//     * 从请求中获取token
//     * @param request
//     * @return java.lang.String
//     * @author zak
//     * @date 2020/6/17 15:24
//     */
//    public static String getToken(HttpServletRequest request) {
//        String authorization = request.getHeader("Authorization");
//        String authToken = null;
//
//        try {
//            if (StrUtil.isNotBlank(authorization)) {
//                authToken = TokenUtil.getTokenFromAuthorization(authorization);
//            }
//            if (StrUtil.isBlank(authToken)) {
//                String access_token = request.getParameter("access_token");
//                if (StrUtil.isNotBlank(access_token)) {
//                    authToken = access_token;
//                }
//            }
//        } catch (Exception e) {
//            log.debug("从请求中取token失败", e);
//        }
//
//        return authToken;
//    }


    /**
     * 获取当前请求的RequestBody
     * @return java.lang.String
     * @author zak
     * @date 2020/6/17 15:24
     */
    public static String currRequestBody() {
        return fetchRequestBody(getRequest());
    }

    public static String fetchRequestBody(HttpServletRequest request) {
        String body = null;
        try {
//            String str = httpServletRequest.getQueryString();
            BufferedReader bufferedReader = request.getReader();
            body = IoUtil.read(bufferedReader);
        } catch (Exception e) {
        }
        return body;
    }

//    public static void print() {
//        try {
//            HttpServletRequest httpServletRequest = getRequest();
//
//            String str = httpServletRequest.getQueryString();
//            BufferedReader bufferedReader = httpServletRequest.getReader();
//            String bodyStr = IoUtil.read(bufferedReader);
//            System.out.println("bodyStr = " + bodyStr);
//        } catch (Exception e) {
//            log.info("请求参数不合法");
//            e.printStackTrace();
//        }
//    }

    public static String getClientIP() {
        return getClientIP(getRequest());
    }

    public static String getClientIP(HttpServletRequest request) {
        return ServletUtil.getClientIP(request);
    }

    // region ------------------------------- Header -------------------------------

    /**
     * 获取请求所有的头（header）信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return header值
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        return ServletUtil.getHeaderMap(request);
    }


    /**
     * 忽略大小写获得请求header中的信息
     *
     * @param request        请求对象{@link HttpServletRequest}
     * @param nameIgnoreCase 忽略大小写头信息的KEY
     * @return header值
     */
    public static String getHeaderIgnoreCase(HttpServletRequest request, String nameIgnoreCase) {
        return ServletUtil.getHeaderIgnoreCase(request, nameIgnoreCase);
    }

    /**
     * 获得请求header中的信息
     *
     * @param request     请求对象{@link HttpServletRequest}
     * @param name        头信息的KEY
     * @param charsetName 字符集
     * @return header值
     */
    public static String getHeader(HttpServletRequest request, String name, String charsetName) {
        return getHeader(request, name, CharsetUtil.charset(charsetName));
    }

    /**
     * 获得请求header中的信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @param name    头信息的KEY
     * @param charset 字符集
     * @return header值
     */
    public static String getHeader(HttpServletRequest request, String name, Charset charset) {
        return ServletUtil.getHeader(request, name, charset);
    }

    public static String getHeader(HttpServletRequest request, String name) {
        return ServletUtil.getHeader(request, name, StandardCharsets.UTF_8);
    }

    public static String getHeader(String name) {
        return ServletUtil.getHeader(getRequest(), name, StandardCharsets.UTF_8);
    }

    public static String getHeaderIgnoreCase(String nameIgnoreCase) {
        return ServletUtil.getHeaderIgnoreCase(getRequest(), nameIgnoreCase);
    }

    /**
     * 获取请求所有的头（header）信息
     *
     * @return header值
     */
    public static Map<String, String> getHeaderMap() {
        return ServletUtil.getHeaderMap(getRequest());
    }

    /**
     * 获得请求header中的信息
     *
     * @param name        头信息的KEY
     * @param charsetName 字符集
     * @return header值
     */
    public static String getHeader(String name, String charsetName) {
        return getHeader(getRequest(), name, CharsetUtil.charset(charsetName));
    }

    /**
     * 获得请求header中的信息
     *
     * @param name    头信息的KEY
     * @param charset 字符集
     * @return header值
     */
    public static String getHeader(String name, Charset charset) {
        return ServletUtil.getHeader(getRequest(), name, charset);
    }

    // endregion ------------------------------- Header -------------------------------

    /**
     * 客户浏览器是否为IE
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 客户浏览器是否为IE
     */
    public static boolean isIE(HttpServletRequest request) {
        return ServletUtil.isIE(request);
    }

    /**
     * 是否为GET请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为GET请求
     */
    public static boolean isGetMethod(HttpServletRequest request) {
        return ServletUtil.isGetMethod(request);
    }

    /**
     * 是否为POST请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为POST请求
     */
    public static boolean isPostMethod(HttpServletRequest request) {
        return ServletUtil.isPostMethod(request);
    }

    /**
     * 是否为Multipart类型表单，此类型表单用于文件上传
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为Multipart类型表单，此类型表单用于文件上传
     */
    public static boolean isMultipart(HttpServletRequest request) {
        return ServletUtil.isMultipart(request);
    }

    /**
     * 客户浏览器是否为IE
     *
     * @return 客户浏览器是否为IE
     */
    public static boolean isIE() {
        return ServletUtil.isIE(getRequest());
    }

    /**
     * 是否为GET请求
     *
     * @return 是否为GET请求
     */
    public static boolean isGetMethod() {
        return ServletUtil.isGetMethod(getRequest());
    }

    /**
     * 是否为POST请求
     *
     * @return 是否为POST请求
     */
    public static boolean isPostMethod() {
        return ServletUtil.isPostMethod(getRequest());
    }

    /**
     * 是否为Multipart类型表单，此类型表单用于文件上传
     *
     * @return 是否为Multipart类型表单，此类型表单用于文件上传
     */
    public static boolean isMultipart() {
        return ServletUtil.isMultipart(getRequest());
    }
}
