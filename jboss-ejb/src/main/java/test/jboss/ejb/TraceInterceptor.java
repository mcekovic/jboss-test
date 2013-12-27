package test.jboss.ejb;


import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import javax.interceptor.*;

import org.slf4j.*;

@Interceptor @Traceable
public class TraceInterceptor extends AnnotationDrivenInterceptor<Traceable, TraceInterceptor.TraceableInfo> {

	@AroundInvoke
	public Object logTraceAndPerformance(final InvocationContext invocationContext) throws Exception {
		Method method = invocationContext.getMethod();
		TraceableInfo trcInfo = getAnnotationInfo(method, Traceable.class);
		if (trcInfo.trace) {
			Logger logger = LoggerFactory.getLogger(invocationContext.getTarget().getClass());
			if (logger.isTraceEnabled()) {
				logger.trace(beforeMessage(method, invocationContext.getParameters(), trcInfo.skipParams, trcInfo.maskParams));
				long t0 = trcInfo.trackTime ? System.currentTimeMillis() : 0L;
				Object retVal = invocationContext.proceed();
				long dt = trcInfo.trackTime ? System.currentTimeMillis() - t0 : -1L;
				logger.trace(afterMessage(method, retVal, dt));
				return retVal;
			}
		}
		return invocationContext.proceed();
	}

	@Override protected TraceableInfo getAnnotationInfo(Traceable trcAnn, TraceableInfo trcInfo) {
		if (trcInfo == null)
			trcInfo = new TraceableInfo();
		else if (trcAnn != null)
			trcInfo.trace = true;
		if (trcAnn != null) {
			boolean trackTime = trcAnn.trackTime();
			if (trackTime)
				trcInfo.trackTime = true;
		}
		return trcInfo;
	}

	@Override protected void updateAnnotationInfo(TraceableInfo annInfo, Annotation[][] paramsAnns) {
		for (int i = 0; i < paramsAnns.length; i++) {
			for (Annotation paramAnn : paramsAnns[i]) {
				if (paramAnn instanceof Skip)
					annInfo.skip(i);
				if (paramAnn instanceof Mask)
					annInfo.mask(i);
			}
		}
	}

	public static final class TraceableInfo {

		private boolean trace = false;
		private boolean trackTime = false;
		private List<Integer> skipParams;
		private List<Integer> maskParams;

		void skip(int i) {
			if (skipParams == null)
				skipParams = new ArrayList<>();
			skipParams.add(i);
		}

		void mask(int i) {
			if (maskParams == null)
				maskParams = new ArrayList<>();
			maskParams.add(i);
		}
	}

	public static String beforeMessage(Method method, Object[] paramValues, List<Integer> skipParams, List<Integer> maskParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getName()).append('(');
		boolean first = true;
		for (int i = 0, len = paramValues.length; i < len ; i++) {
			if (skipParams != null && skipParams.contains(i))
				continue;
			if (first)
				first = false;
			else
				sb.append(", ");
			String paramValue = toString(paramValues[i]);
			if (maskParams != null && maskParams.contains(i))
				sb.append(copy('*', paramValue.length()));
			else
				sb.append(paramValue);
		}
		return sb.append(')').toString();
	}

	public static String afterMessage(Method method, Object returnValue, long dt) {
		StringBuilder sb = new StringBuilder();
		sb.append('~').append(method.getName());
		if (!method.getReturnType().equals(void.class))
			sb.append('=').append(returnValue);
		if (dt != -1L)
			sb.append(" [").append(dt).append("ms]");
		return sb.toString();
	}

	public static String toString(Object obj) {
		if (obj != null) {
			Class cls = obj.getClass();
			if (cls.isArray()) {
				if (obj instanceof Object[])
					return Arrays.toString((Object[])obj);
				else if (obj instanceof byte[])
					return Arrays.toString((byte[])obj);
				else if (obj instanceof short[])
					return Arrays.toString((short[])obj);
				else if (obj instanceof int[])
					return Arrays.toString((int[])obj);
				else if (obj instanceof long[])
					return Arrays.toString((long[])obj);
				else if (obj instanceof float[])
					return Arrays.toString((float[])obj);
				else if (obj instanceof double[])
					return Arrays.toString((double[])obj);
				else if (obj instanceof char[])
					return Arrays.toString((char[])obj);
				else if (obj instanceof boolean[])
					return Arrays.toString((boolean[])obj);
			}
		}
		return String.valueOf(obj);
	}

	public static String copy(char c, int count) {
		char[] cs = new char[count];
		Arrays.fill(cs, c);
		return new String(cs);
	}

}
