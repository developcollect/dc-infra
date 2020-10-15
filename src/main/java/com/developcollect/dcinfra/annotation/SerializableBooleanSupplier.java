
package com.developcollect.dcinfra.annotation;


import java.io.Serializable;
import java.util.function.BooleanSupplier;


@FunctionalInterface
public interface SerializableBooleanSupplier extends Serializable, BooleanSupplier {

}
