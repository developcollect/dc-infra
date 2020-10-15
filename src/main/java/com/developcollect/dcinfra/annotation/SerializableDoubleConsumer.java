
package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.DoubleConsumer;


@FunctionalInterface
public interface SerializableDoubleConsumer extends Serializable, DoubleConsumer {


}
