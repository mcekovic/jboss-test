package test.jboss.ejb;

import javax.annotation.*;
import javax.ejb.*;
import javax.jms.*;

import org.slf4j.*;

@Stateless @Remote(RemoteHello.class)
public class HelloBean implements RemoteHello {

	@EJB private RemoteHello2 hello2;

	@Resource(lookup = "java:/ConnectionFactory") private ConnectionFactory connectionFactory;
	@Resource(lookup = "java:/queue/hello") private Queue queue;

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);

	@PostConstruct
	public void init() {
		LOGGER.info("HelloBean initialized.");
	}

	@PreDestroy
	public void destroy() {
		LOGGER.info("HelloBean destroyed.");
	}

	@Override public String hello(String name) {
		LOGGER.info("HelloBean helloed.");
		return "Hello " + name;
	}

	@Override public String hello2(String name) {
		LOGGER.info("HelloBean helloed 2.");
		return hello2.hello(name);
	}

	@Override public void sendHello(String name) {
		try {
			Connection connection = connectionFactory.createConnection();
			try {
				Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
				try {
					MessageProducer producer = session.createProducer(queue);
					try {
						producer.send(session.createTextMessage(name));
					}
					finally {
						producer.close();
					}
				}
				finally {
					session.close();
				}
			}
			finally {
				connection.close();
			}
		}
		catch (JMSException ex) {
			throw new EJBException("Error sending hello message.", ex);
		}
	}

	@Override public String helloMessage(String name) {
		LOGGER.info("HelloBean hello message.");
		return "Hello " + name + " from Message";
	}
}
