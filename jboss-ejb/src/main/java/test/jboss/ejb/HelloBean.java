package test.jboss.ejb;

import javax.ejb.*;

@Stateless @Remote(RemoteHello.class)
public class HelloBean implements RemoteHello {

	@Override public String hello(String name) {
		return "Hello " + name;
	}
}
