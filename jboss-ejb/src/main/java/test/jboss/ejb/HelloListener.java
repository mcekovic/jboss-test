package test.jboss.ejb;

import javax.ejb.*;
import javax.jms.*;

@MessageDriven(name = "HelloListener", activationConfig = {
	@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/hello"),
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

	@Override public void onMessage(Message message) {
		try {
			String msgText = hello.helloMessage(((TextMessage)message).getText());
			System.out.println(msgText);
		}
		catch (JMSException ex) {
			ex.printStackTrace();
		}
	}
}
