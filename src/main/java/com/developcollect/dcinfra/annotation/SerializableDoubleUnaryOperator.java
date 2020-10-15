package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.DoubleUnaryOperator;


@FunctionalInterface
public interface SerializableDoubleUnaryOperator extends Serializable, DoubleUnaryOperator {


}
