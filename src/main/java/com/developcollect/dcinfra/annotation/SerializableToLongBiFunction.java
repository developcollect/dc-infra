package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.ToLongBiFunction;


@FunctionalInterface
public interface SerializableToLongBiFunction<T, U> extends Serializable, ToLongBiFunction<T, U> {


}
