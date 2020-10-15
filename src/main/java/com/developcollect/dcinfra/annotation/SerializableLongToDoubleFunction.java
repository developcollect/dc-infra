package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.LongToDoubleFunction;


@FunctionalInterface
public interface SerializableLongToDoubleFunction extends Serializable, LongToDoubleFunction {


}
