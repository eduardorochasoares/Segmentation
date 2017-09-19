package activemq;

import Formats.Formats;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;

import bavi.recommendation.Video;
import dbpedia.DBPedia;
import java.io.FileInputStream;
import java.util.Properties;
import blazegraph.*;
import java.util.ArrayList;
import org.json.JSONObject;

public class ActiveMQ implements ExceptionListener {

    private static String AMQ_SERVER;
    private static String AMQ_USERNAME;
    private static String AMQ_PASSWORD;
    private static String QUEUE_OUT;
    private static Blazegraph bz;

    
    public ActiveMQ() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream("../properties/config.properties");
        props.load(file);

        this.AMQ_USERNAME = props.getProperty("AMQ_USERNAME");
        this.AMQ_PASSWORD = props.getProperty("AMQ_PASSWORD");
        this.AMQ_SERVER = props.getProperty("AMQ_SERVER");
        this.QUEUE_OUT = props.getProperty("queue.fromRecommendation.toRdfStore");
    
        String blazeURL = props.getProperty("Blazegraph.url");
        String blazeDBName = props.getProperty("Blazegraph.dbname");
        bz = new Blazegraph(blazeURL, blazeDBName);
    }
    
    public String getQueueOut(){
        return this.QUEUE_OUT;
    }
    
    public static void registerConsumer(String queue) throws FileNotFoundException, IOException {

        MessageConsumer consumer;
        Session session;
        Connection connection;
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AMQ_USERNAME, AMQ_PASSWORD, AMQ_SERVER);

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queue);

            // Create a MessageConsumer from the Session to the Topic or Queue
            consumer = session.createConsumer(destination);

            // Create a Listener
            MessageListener listner = new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            String text = textMessage.getText();
                            // TEXT LIDO DO ACTIVEMQ
                            System.out.println("text from activeMQ :" + text);
                            
                            JSONObject jsonObj = new JSONObject(text);
                            ArrayList<String> msg = new ArrayList<String>();
                            msg = Formats.returnArrayFromJSON(jsonObj,"references");

                            Video v = new Video((String) jsonObj.get("ID"));
                            v.setReferences(msg);
                            
                            DBPedia db = new DBPedia();
                            db.getResourcesRelatedAndRelatedTo(v);

                        }
                    } catch (Exception e) {
                        System.out.println("Caught:" + e);
                        e.printStackTrace();
                    }
                }

            };
            consumer.setMessageListener(listner);

        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public static void registerProducer(String queue, String message) throws FileNotFoundException, IOException {

        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AMQ_USERNAME, AMQ_PASSWORD, AMQ_SERVER);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queue);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            TextMessage text_message = session.createTextMessage(message);

            // Tell the producer to send the message
            //System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
            System.out.println("Json message: " + message);
            producer.send(text_message);

            // Clean up
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

}
