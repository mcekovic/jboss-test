package test.jboss.ejb;

public interface RemoteHello {

	String hello(String name);
	String hello2(String name);
	void sendHello(String name);
	String helloMessage(String name);
}
