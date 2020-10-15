package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.DoubleToIntFunction;


@FunctionalInterface
public interface SerializableDoubleToIntFunction extends Serializable, DoubleToIntFunction {


}
