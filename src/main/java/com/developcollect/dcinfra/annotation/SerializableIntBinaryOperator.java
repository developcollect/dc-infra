package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.IntBinaryOperator;


@FunctionalInterface
public interface SerializableIntBinaryOperator extends Serializable, IntBinaryOperator {


}
