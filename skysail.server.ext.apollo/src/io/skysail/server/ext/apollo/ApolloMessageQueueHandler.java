package io.skysail.server.ext.apollo;

import javax.jms.*;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.services.MessageQueueHandler;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class ApolloMessageQueueHandler implements MessageQueueHandler {

    private String user = "admin";
    private String password = "password";
    private String host = "localhost";
    private int port = 61613;
    private Runnable runnable;

    @Activate
    public void activate() {
        runnable = new Runnable() {
            @Override
            public void run() {
                //listen();
            }

            private void listen() {
               
                String destination = "/topic/event";

                StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
                factory.setBrokerURI("tcp://" + host + ":" + port);

                Connection connection;
                try {
                    connection = factory.createConnection(user, password);
                    connection.start();
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Destination dest = new StompJmsDestination(destination);

                    MessageConsumer consumer = session.createConsumer(dest);
                    long start = System.currentTimeMillis();
                    long count = 1;
                    System.out.println("Waiting for messages...");
                    while(true) {
                        Message msg = consumer.receive();
                        if( msg instanceof  TextMessage ) {
                            String body = ((TextMessage) msg).getText();
                            System.out.println(body);
                            if( "SHUTDOWN".equals(body)) {
                                long diff = System.currentTimeMillis() - start;
                                System.out.println(String.format("Received %d in %.2f seconds", count, (1.0*diff/1000.0)));
                                break;
                            } else {
                                if( count != msg.getIntProperty("id") ) {
                                    System.out.println("mismatch: "+count+"!="+msg.getIntProperty("id"));
                                }
                                count = msg.getIntProperty("id");

                                if( count == 0 ) {
                                    start = System.currentTimeMillis();
                                }
                                if( count % 1000 == 0 ) {
                                    System.out.println(String.format("Received %d messages.", count));
                                }
                                count ++;
                            }

                        } else {
                            System.out.println("Unexpected message type: "+msg.getClass());
                        }
                    }
                    connection.close();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        runnable.run();
    }

    @Deactivate
    public void deactivate() {
       // runnable.
    }

    @Override
    public void send(String topic, String message) {
        String destination = topic;

        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setBrokerURI("tcp://" + host + ":" + port);

        Connection connection;
        try {
            connection = factory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = new StompJmsDestination(destination);
            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            TextMessage msg = session.createTextMessage(message);
            msg.setIntProperty("id", 1);
            producer.send(msg);

            producer.send(session.createTextMessage("SHUTDOWN"));
            connection.close();
        } catch (JMSException e) {
            log.warn(e.getMessage(), e);
        }

        // try {
        // ByteArrayInputStream fileIn = new
        // ByteArrayInputStream(message.getBytes());
        // ObjectInputStream in = new ObjectInputStream(fileIn);
        // Object readObject = in.readObject();
        // in.close();
        // fileIn.close();
        // System.out.println(readObject);
        // } catch (IOException i) {
        // i.printStackTrace();
        // return;
        // } catch (ClassNotFoundException c) {
        // System.out.println("Employee class not found");
        // c.printStackTrace();
        // return;
        // }

    }

}
