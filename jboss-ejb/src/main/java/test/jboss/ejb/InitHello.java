package test.jboss.ejb;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import javax.annotation.*;
import javax.ejb.*;

import org.slf4j.*;

@Singleton @Startup @DependsOn("HelloListener")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class InitHello {

	public static final int COUNT = 50000;
	public static final int STEP = 100;

	private static final Logger LOGGER = LoggerFactory.getLogger(InitHello.class);

	@EJB private RemoteHello hello;
	@Resource private SessionContext sessionContext;

	private static final AtomicLong T0 = new AtomicLong();

	@PostConstruct
	public void init() {
		sessionContext.getBusinessObject(InitHello.class).sendHelloMessages();
		System.out.println("InitHello initialized.");
		LOGGER.info("InitHello initialized.");
	}

	@Asynchronous
	public void sendHelloMessages() {
		for (int count = 1; count <= COUNT; count++) {
			try {
				Thread.sleep(10L);
			}
			catch (InterruptedException ignored) {}
			hello.sendHello("Message");
			if (count == 1)
				T0.set(System.currentTimeMillis());
			if (count == 1 || count % STEP == 0) {
				long dt = System.currentTimeMillis() - T0.get();
				LOGGER.info("Hello message {} sent in {} ms ({} msg/s).", count, dt, 1000.0*count/dt);
			}
		}
	}

	public void waitForReceivingFinished() {
		HelloListener.waitForReceivingFinished();
	}

	@PreDestroy
	public void destroy() {
		System.out.println("InitHello destroyed.");
		LOGGER.info("InitHello destroyed.");
	}
}
