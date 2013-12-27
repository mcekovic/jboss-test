package test.jboss.ejb;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public abstract class AnnotationDrivenInterceptor<A extends Annotation, AI> {

	private final Map<String, AI> annInfoCache = new ConcurrentHashMap<>();

	public AI getAnnotationInfo(Method method, Class<A> annClass) {
		String infoKey = method.toGenericString();
		AI annInfo = annInfoCache.get(infoKey);
		if (annInfo == null) {
			annInfo = getAnnotationInfo(method.getDeclaringClass().getAnnotation(annClass), null);
			annInfo = getAnnotationInfo(method.getAnnotation(annClass), annInfo);
			updateAnnotationInfo(annInfo, method.getParameterAnnotations());
			annInfoCache.put(infoKey, annInfo);
		}
		return annInfo;
	}

	protected abstract AI getAnnotationInfo(A ann, AI annInfo);

	protected void updateAnnotationInfo(AI annInfo, Annotation[][] paramAnns) {}
}
