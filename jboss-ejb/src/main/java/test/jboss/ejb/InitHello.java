package test.jboss.ejb;

import javax.annotation.*;
import javax.ejb.*;

import org.slf4j.*;

@Singleton @Startup @DependsOn("HelloListener")
public class InitHello {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitHello.class);

	@EJB private RemoteHello hello;

	@PostConstruct
	public void init() {
		System.out.println("InitHello initialized.");
		LOGGER.info("InitHello initialized.");

		hello.sendHello("Message");
		System.out.println("Hello messgage sent.");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("InitHello destroyed.");
		LOGGER.info("InitHello destroyed.");
	}
}
