package test.jboss.ejb;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import javax.ejb.*;
import javax.jms.*;

import org.slf4j.*;

@MessageDriven(name = "HelloListener", activationConfig = {
	@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/hello"),
	@ActivationConfigProperty(propertyName = "useJndi", propertyValue = "true")
})
// ActiveMQ Physical Name
//@ActivationConfigProperty(propertyName = "destination", propertyValue = "hello"),
//@ActivationConfigProperty(propertyName = "useJndi", propertyValue = "false")
// ActiveMQ JNDI Name
//@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/hello"),
//@ActivationConfigProperty(propertyName = "useJndi", propertyValue = "true")
public class HelloListener implements MessageListener {

	@EJB private RemoteHello hello;

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloListener.class);

	private static final AtomicInteger COUNTER = new AtomicInteger();
	private static final CountDownLatch LATCH = new CountDownLatch(InitHello.COUNT);
	private static final AtomicLong T0 = new AtomicLong();

	@Override public void onMessage(Message message) {
		try {
			int count = COUNTER.incrementAndGet();
			if (count == 1)
				T0.set(System.currentTimeMillis());
			LATCH.countDown();
			if (count == 1 || count % InitHello.STEP == 0) {
				String msgText = hello.helloMessage(((TextMessage)message).getText());
				long dt = System.currentTimeMillis() - T0.get();
				LOGGER.info("Hello message {} received in {} ms ({} msg/s): " + msgText, count, dt, 1000.0*count/dt);
			}
		}
		catch (JMSException ex) {
			ex.printStackTrace();
		}
	}

	public static void waitForReceivingFinished() {
		try {
			LATCH.await();
		}
		catch (InterruptedException ignored) {}
	}
}
