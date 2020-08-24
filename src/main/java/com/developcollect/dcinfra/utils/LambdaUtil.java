package com.developcollect.dcinfra.utils;


/**
 * lambda工具类
 *
 * @author zak
 * @since 1.0.0
 */
public class LambdaUtil {

    /**
     * 什么都不做
     * @param objs
     * @return R
     * @author zak
     * @date 2020/8/24 10:55
     */
    public static <R> R doNothing(Object... objs) {
        return null;
    }

    /**
     * 同{@link #doNothing(Object...)}
     */
    public static <R> R nop(Object... objs) {
        // nop
        return null;
    }

    /**
     * 编写一个泛型方法对异常进行包装
     * 从而绕过受检异常在编译时编译不通过的错误
     *
     * @param e   异常
     * @param <R> 返回值类型
     * @param <E> 异常类型
     * @throws E 异常
     * @author zak
     * @since 1.0.0
     */
    public static <R, E extends Throwable> R doThrow(Throwable e) throws E {
        throw (E) e;
    }

    /**
     * 同{@link #doThrow(Throwable)}
     */
    public static <R, E extends Throwable> R raise(Throwable e) throws E {
        throw (E) e;
    }

    /**
     * 通过函数式接口包装原有含受检异常的代码， 将受检异常通过泛型绕过编译错误
     *
     * @param doThrowWrapper 异常包装
     * @param <T>            返回值类型
     * @return 返回值
     * @author zak
     * @since 1.0.0
     */
    public static <T> T doThrow(DoThrowWrapper<T> doThrowWrapper) {
        try {
            return doThrowWrapper.get();
        } catch (Throwable throwable) {
            return doThrow(throwable);
        }
    }

    /**
     * 同{@link #doThrow(DoThrowWrapper)}
     */
    public static <T> T raise(DoThrowWrapper<T> doThrowWrapper) {
        return doThrow(doThrowWrapper);
    }


    /**
     * 异常代码包装
     *
     * @author zak
     * @since 1.0.0
     */
    @FunctionalInterface
    public interface DoThrowWrapper<T> {
        T get() throws Throwable;
    }
}



