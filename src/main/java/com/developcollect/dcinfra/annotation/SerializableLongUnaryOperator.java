package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.LongUnaryOperator;


@FunctionalInterface
public interface SerializableLongUnaryOperator extends Serializable, LongUnaryOperator {


}
