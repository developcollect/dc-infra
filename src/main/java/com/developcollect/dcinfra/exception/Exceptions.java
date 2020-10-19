package com.developcollect.dcinfra.exception;

import java.io.Serializable;
import java.util.Map;


/**
 * 异常通用类
 * 这个类里放的是全局的异常枚举和异常构造方法
 *
 * @author zak
 * @version 1.0
 * @date 2020/10/19 10:04
 */
public interface Exceptions {

    // region 异常枚举

    GlobalException VIDEO_DECODE = new GlobalException(5006, "视频解码异常");

    GlobalRuntimeException ID_NOT_EXIST = new GlobalRuntimeException(5001, "id不存在");

    // endregion


    // region 自定义异常构造

    static GlobalRuntimeException ID_NOT_EXIST(Serializable id) {
        return new GlobalRuntimeException(5001, "id[{}]不存在", id);
    }

    // endregion


    // region 通用异常构造

    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException()}
     */
    static GlobalRuntimeException re() {
        return new GlobalRuntimeException();
    }

    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(int, String)}
     */
    static GlobalRuntimeException re(int code, String msg) {
        return new GlobalRuntimeException(code, msg);
    }

    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(int)}
     */
    static GlobalRuntimeException re(int code) {
        return new GlobalRuntimeException(code);
    }


    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(String)}
     */
    static GlobalRuntimeException re(String msg) {
        return new GlobalRuntimeException(msg);
    }


    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(int, String, Throwable)}
     */
    static GlobalRuntimeException re(int code, String msg, Throwable throwable) {
        return new GlobalRuntimeException(code, msg, throwable);
    }

    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(String, Throwable)}
     */
    static GlobalRuntimeException re(String msg, Throwable throwable) {
        return new GlobalRuntimeException(msg, throwable);
    }


    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(int, Throwable)}
     */
    static GlobalRuntimeException re(int code, Throwable throwable) {
        return new GlobalRuntimeException(code, throwable);
    }


    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(int, String, Object...)}
     */
    static GlobalRuntimeException re(int code, String format, Object... params) {
        return new GlobalRuntimeException(code, format, params);

    }


    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(String, Object...)}
     */
    static GlobalRuntimeException re(String format, Object... params) {
        return new GlobalRuntimeException(format, params);
    }


    /**
     * 运行时异常构造
     * <p>
     * 详细查看：{@link GlobalRuntimeException#GlobalRuntimeException(Throwable)}
     */
    static GlobalRuntimeException re(Throwable throwable) {
        return new GlobalRuntimeException(throwable);
    }


    // ------------- 受检异常构造 ---------------


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException()}
     */
    static GlobalException e() {
        return new GlobalException();
    }

    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(int, String)}
     */
    static GlobalException e(int code, String msg) {
        return new GlobalException(code, msg);
    }

    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(int)}
     */
    static GlobalException e(int code) {
        return new GlobalException(code);
    }


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(String)}
     */
    static GlobalException e(String msg) {
        return new GlobalException(msg);
    }


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(int, String, Throwable)}
     */
    static GlobalException e(int code, String msg, Throwable throwable) {
        return new GlobalException(code, msg, throwable);
    }

    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(String, Throwable)}
     */
    static GlobalException e(String msg, Throwable throwable) {
        return new GlobalException(msg, throwable);
    }


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(int, Throwable)}
     */
    static GlobalException e(int code, Throwable throwable) {
        return new GlobalException(code, throwable);
    }


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(int, String, Object...)}
     */
    static GlobalException e(int code, String format, Object... params) {
        return new GlobalException(code, format, params);

    }


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(String, Object...)}
     */
    static GlobalException e(String format, Object... params) {
        return new GlobalException(format, params);
    }


    /**
     * 受检异常构造
     * <p>
     * 详细查看：{@link GlobalException#GlobalException(Throwable)}
     */
    static GlobalException e(Throwable throwable) {
        return new GlobalException(throwable);
    }

    // endregion


    // region 扩展方法

    /**
     * 获取当前异常枚举中所有的
     * 状态码名称 ==> 状态码
     * <p>
     * 这里的异常状态码名称是自动生成的，生成规则
     * 异常字段名称 + _CODE
     * <p>
     * 例如
     * 有一个异常字段如下
     * GlobalException VIDEO_DECODE = new GlobalException(5006, "视频解码异常");
     * 那么该异常状态码名称就是 VIDEO_DECODE_CODE
     *
     * @return 状态码名称和状态码映射的map
     * @author zak
     * @date 2020/10/19 11:43
     */
    static Map<String, Integer> getCodeMap() {
        return ExceptionHelper.getCodeMap();
    }

    /**
     * 通过异常状态码名称获取异常状态码
     *
     * @param codeKey
     * @return int
     * @author zak
     * @date 2020/10/19 12:01
     */
    static int getCode(String codeKey) {
        return ExceptionHelper.getCode(codeKey);
    }

    /**
     * 通过状态码获取状态码对应的异常消息
     *
     * @param code 异常状态码
     * @return java.lang.String
     * @author zak
     * @date 2020/10/19 11:44
     */
    static String getMessage(int code) {
        return ExceptionHelper.getMessage(code);
    }

    // endregion
}
