package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.BiConsumer;


@FunctionalInterface
public interface SerializableBiConsumer<T, U> extends Serializable, BiConsumer<T, U> {


}
