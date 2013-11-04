package test.jboss.ejb;

import java.util.*;
import javax.naming.*;

public class HelloAppClient {

	public static void main(String[] args) throws NamingException {
		Properties props = new Properties();
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		InitialContext ctx = new InitialContext(props);
		RemoteHello hello = (RemoteHello)ctx.lookup("ejb:jboss-ear/jboss-ejb//HelloBean!test.jboss.ejb.RemoteHello");
		hello.hello("World");
	}
}
