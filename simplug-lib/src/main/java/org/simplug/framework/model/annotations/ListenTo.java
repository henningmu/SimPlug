package org.simplug.framework.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.simplug.framework.model.events.Event;

/**
 * This annotation is used to annotate classes which extend
 * @see Plugin and want to listen to specific events. It is possible that
 * one class listens to multiple events. This annotation can be used instead
 * providing a configuration file inside the plugins.
 * */
@Documented
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface ListenTo {
	Class<? extends Event>[] value();
}
