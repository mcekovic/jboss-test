package test.jboss.ejb;

import javax.ejb.*;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.*;
import org.jboss.shrinkwrap.api.*;
import org.jboss.shrinkwrap.api.spec.*;
import org.junit.*;
import org.junit.runner.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class HelloBeanIT {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
			.addPackage(HelloBean.class.getPackage())
			.addClasses(RemoteHello2.class, HelloBean2.class)
			.addAsResource("META-INF/beans.xml");
	}

	@EJB private RemoteHello hello;

	private static final String NAME = "World";

	@Test
	public void hello() {
		String helloMsg = hello.hello(NAME);
		assertThat(helloMsg, containsString(NAME));
	}
}
