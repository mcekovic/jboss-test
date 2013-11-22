package test.jboss.ejb;

import java.util.*;
import javax.naming.*;

public class HelloAppClient {

	public static void main(String[] args) throws NamingException {
		InitialContext ctx = getInitialContext();
		RemoteHello hello = (RemoteHello)ctx.lookup("ejb:jboss-ear/jboss-ejb/HelloBean!test.jboss.ejb.RemoteHello");
		System.out.println(hello.hello("World"));

		InitialContext ctx2 = getInitialContext2();
		RemoteHello hello2 = (RemoteHello)ctx2.lookup("java:jboss-ear/jboss-ejb/HelloBean!test.jboss.ejb.RemoteHello");
		System.out.println(hello2.hello("World 2"));
	}

	private static InitialContext getInitialContext() throws NamingException {
		Properties props = new Properties();
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		return new InitialContext(props);
	}

	private static InitialContext getInitialContext2() throws NamingException {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(Context.PROVIDER_URL,"remote://localhost:4447");
		return new InitialContext(props);
	}
}
