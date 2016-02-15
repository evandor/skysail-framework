package io.skysail.server.ext.apollo;

import javax.jms.*;

import org.apache.qpid.amqp_1_0.jms.impl.*;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import io.skysail.server.services.MessageQueueHandler;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = "apolloMQ")
@Designate(ocd = ApolloConfig.class)
@Slf4j
public class ApolloMessageQueueHandler2 implements MessageQueueHandler {

    private String user = "admin";
    private String password = "password";
    private String host = "localhost";
    private int port = 61613;
    private Thread t;
    private int cnt = 1;

    @Activate
    public void activate(ApolloConfig apolloConfig) {
        user = apolloConfig.getUser();
        password = apolloConfig.getPassword();
        host = apolloConfig.getHost();
        port = apolloConfig.getPort();

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    listen();
                }
            }
        });
        t.start();
    }

    @Deactivate
    public void deactivate() {
        t.interrupt();
    }

    @Override
    public void send(String topic, String message) {
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host, port, user, password);
        Destination dest = null;
        String destination = "topic://event";
        if (destination.startsWith("topic://")) {
            dest = new TopicImpl(destination);
        } else {
            dest = new QueueImpl(destination);
        }
        Connection connection;
        try {
            connection = factory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage msg = session.createTextMessage("#:" + cnt);
            msg.setIntProperty("id", cnt++);
            producer.send(msg);
            System.out.println(String.format("Sent %d messages", cnt));
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void listen() {

        String destination = "topic://event";

        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host, port, user, password);
        Destination dest = null;
        if (destination.startsWith("topic://")) {
            dest = new TopicImpl(destination);
        } else {
            dest = new QueueImpl(destination);
        }

        Connection connection;
        try {
            connection = factory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            long start = System.currentTimeMillis();
            long count = 1;
            System.out.println("Waiting for messages...");
            while (true) {
                Message msg = consumer.receive();
                if (msg instanceof TextMessage) {
                    String body = ((TextMessage) msg).getText();
                    if ("SHUTDOWN".equals(body)) {
                        long diff = System.currentTimeMillis() - start;
                        System.out.println(String.format("Received %d in %.2f seconds", count, (1.0 * diff / 1000.0)));
                        connection.close();
                        System.exit(1);
                    } else {
                        try {
                            if (count != msg.getIntProperty("id")) {
                                System.out.println("mismatch: " + count + "!=" + msg.getIntProperty("id"));
                            }
                        } catch (NumberFormatException ignore) {
                        }
                        if (count == 1) {
                            start = System.currentTimeMillis();
                        }
                        System.out.println(String.format("Received %d messages.", count));
                        count++;
                    }

                } else {
                    System.out.println("Unexpected message type: " + msg.getClass());
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
