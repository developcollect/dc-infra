package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.IntFunction;


@FunctionalInterface
public interface SerializableIntFunction<R> extends Serializable, IntFunction<R> {


}
