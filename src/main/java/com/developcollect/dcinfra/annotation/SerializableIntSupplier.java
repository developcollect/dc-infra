package com.developcollect.dcinfra.annotation;

import java.io.Serializable;
import java.util.function.IntSupplier;


@FunctionalInterface
public interface SerializableIntSupplier extends Serializable, IntSupplier {

}
