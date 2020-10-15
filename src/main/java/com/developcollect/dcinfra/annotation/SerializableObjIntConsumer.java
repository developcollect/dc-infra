package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.ObjIntConsumer;

@FunctionalInterface
public interface SerializableObjIntConsumer<T> extends Serializable, ObjIntConsumer<T> {

}
