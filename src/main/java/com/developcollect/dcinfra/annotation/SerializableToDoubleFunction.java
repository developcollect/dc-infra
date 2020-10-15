package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface SerializableToDoubleFunction<T> extends Serializable, ToDoubleFunction<T> {


}
