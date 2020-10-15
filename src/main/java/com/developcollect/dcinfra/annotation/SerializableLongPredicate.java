package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.LongPredicate;


@FunctionalInterface
public interface SerializableLongPredicate extends Serializable, LongPredicate {


}
