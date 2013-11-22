package test.jboss.ejb;

import javax.ejb.*;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.*;
import org.jboss.shrinkwrap.api.*;
import org.jboss.shrinkwrap.api.asset.*;
import org.jboss.shrinkwrap.api.spec.*;
import org.junit.*;
import org.junit.runner.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class HelloBean2IT {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
			.addClasses(RemoteHello2.class, HelloBean2.class)
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB private RemoteHello2 hello;

	private static final String NAME = "World";

	@Test
	public void hello() {
		String helloMsg = hello.hello(NAME);
		assertThat(helloMsg, containsString(NAME));
	}
}
