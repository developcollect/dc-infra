package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.LongConsumer;


@FunctionalInterface
public interface SerializableLongConsumer extends Serializable, LongConsumer {


}
