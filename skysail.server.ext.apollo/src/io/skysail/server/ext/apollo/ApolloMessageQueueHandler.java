package io.skysail.server.ext.apollo;

import javax.jms.*;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionImpl;
import org.apache.qpid.amqp_1_0.jms.impl.QueueImpl;
import org.apache.qpid.amqp_1_0.jms.impl.TopicImpl;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import io.skysail.server.services.MessageQueueHandler;
import io.skysail.server.services.SkysailMessageListener;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = "apolloMQ")
@Designate(ocd = ApolloConfig.class)
@Slf4j
public class ApolloMessageQueueHandler implements MessageQueueHandler {
    
    private String user;
    private String password;
    private String host;
    private int port;
    private Thread t;
    private int cnt = 1;
    private ConnectionFactoryImpl factory;
    private ConnectionImpl connection;

    @Activate
    public void activate(ApolloConfig apolloConfig) {
        user = apolloConfig.getUser();
        password = apolloConfig.getPassword();
        host = apolloConfig.getHost();
        port = apolloConfig.getPort();
        
        factory = new ConnectionFactoryImpl(host, port, user, password);
        try {
            connection = factory.createConnection(user, password);
        } catch (JMSException e) {
            log.error(e.getMessage(),e);
        }
        
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
        
        Destination dest = null;
        //String destination = "topic://event";
        if (topic.startsWith("topic://")) {
            dest = new TopicImpl(topic);
        } else {
            dest = new QueueImpl(topic);
        }
        try {
            
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            //producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage msg = session.createTextMessage(message);
            msg.setIntProperty("id", cnt++);
            producer.send(msg);
            System.out.println(String.format("Sent %d messages", cnt));
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void listen() {

        String destination = "topic://entity.io_skysail_server_app_notes_Note.post";
        Destination dest = null;
        if (destination.startsWith("topic://")) {
            dest = new TopicImpl(destination);
        } else {
            dest = new QueueImpl(destination);
        }
        
        try {
            connection = factory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            System.out.println("Waiting for messages...");
            while (true) {
                Message msg = consumer.receive();
                if (msg instanceof TextMessage) {
                    String body = ((TextMessage) msg).getText();
                    System.out.println("received: " + body);
                   
                } else {
                    System.out.println("Unexpected message type: " + msg.getClass());
                }
            }
        } catch (JMSException e) {
            log.warn(e.getMessage(), e);
        }

    }

    @Override
    public void addMessageListener(String topic, SkysailMessageListener listener) {
        String destination = topic;
        Destination dest = null;
        if (destination.startsWith("topic://")) {
            dest = new TopicImpl(destination);
        } else {
            dest = new QueueImpl(destination);
        }
        
        try {
            connection = factory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        try {
                            listener.processBody(((TextMessage)message).getText());
                        } catch (JMSException e) {
                            log.error(e.getMessage(),e);
                        }
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
