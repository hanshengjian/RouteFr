package com.route.fr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intent extras key annotation
 * @author hansj
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface Key {
    String value();
}
