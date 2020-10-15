package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface SerializableDoublePredicate extends Serializable, DoublePredicate {


}
