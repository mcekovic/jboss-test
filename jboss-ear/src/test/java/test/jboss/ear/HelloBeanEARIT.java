package test.jboss.ear;

import javax.ejb.*;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.*;
import org.jboss.shrinkwrap.api.*;
import org.jboss.shrinkwrap.api.asset.*;
import org.jboss.shrinkwrap.api.spec.*;
import org.jboss.shrinkwrap.descriptor.api.*;
import org.jboss.shrinkwrap.descriptor.api.spec.ee.application.*;
import org.junit.*;
import org.junit.runner.*;

import test.jboss.ejb.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class HelloBeanEARIT {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(EnterpriseArchive.class, "hello.ear")
			.addAsModule(ShrinkWrap.create(JavaArchive.class, "hello2.jar")
				.addClasses(RemoteHello2.class, HelloBean2.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
			);
//			.addAsModule(ShrinkWrap.create(JavaArchive.class, "hello.jar")
//				.addClasses(RemoteHello.class, HelloBean.class)
//				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
//			);
//			.addAsManifestResource(
//				new StringAsset(Descriptors.create(ApplicationDescriptor.class).version("6")
//					.ejbModule("hello.jar").ejbModule("hello2.jar").exportAsString()), "application.xml");
	}

//	@EJB private RemoteHello hello;

	private static final String NAME = "World";

	@Test
	public void hello() {
//		String helloMsg = hello.hello(NAME);
//		assertThat(helloMsg, containsString(NAME));
	}
}
