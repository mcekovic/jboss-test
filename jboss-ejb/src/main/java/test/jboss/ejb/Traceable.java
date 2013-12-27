package test.jboss.ejb;

import java.lang.annotation.*;
import javax.enterprise.util.*;
import javax.interceptor.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface Traceable {
	@Nonbinding boolean trackTime() default false;
}
