package com.route.fr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Dest ClassName Annotation
 * @author hansj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ClassName {
    String value();
}
