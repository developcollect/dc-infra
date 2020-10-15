package com.developcollect.dcinfra.annotation;


import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author zak
 */
@FunctionalInterface
public interface SerializableSupplier<T> extends Serializable, Supplier<T> {

}
