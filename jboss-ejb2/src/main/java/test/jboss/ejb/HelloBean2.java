package test.jboss.ejb;

import javax.ejb.*;

@Stateless @Remote(RemoteHello2.class)
public class HelloBean2 implements RemoteHello2 {

	@Override public String hello(String name) {
		return "Hello " + name + " 2";
	}
}
