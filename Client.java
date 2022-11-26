import dto.Country;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private static final String separator = "#";

    public Client() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:3009");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueOut = session.createQueue("fromClient");
            Destination queueIn = session.createQueue("toClient");

            producer = session.createProducer(queueOut);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueIn);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String handleMessage(String query, int timeout) throws JMSException {
        TextMessage message = session.createTextMessage(query);
        producer.send(message);
        Message mes = consumer.receive(timeout);
        if (mes == null) {
            return null;
        }

        if (mes instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) mes;
            return textMessage.getText();
        }

        return "";
    }

    public Country countryFindById(Long id) {
        String query = "countryFindById" + separator + id.toString();
        try {
            String response = handleMessage(query, 15000);
            return new Country(id, response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Country capitalFindByName(String name) {
        String query = "countyFindByName" + separator + name;
        try {
            String response = handleMessage(query, 15000);
            Long responseId = Long.parseLong(response);
            return new Country(responseId, name);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean CountryUpdate(Country country) {
        String query = "CountryUpdate" + separator + country.getId() +
                separator + country.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean countryInsert(Country country) {
        String query = "CountryInsert" +
                separator + country.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean coutryDelete(Country country) {
        String query = "CountryDelete" + separator + country.getId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Country> countryAll() {
        String query = "CountryAll";
        List<Country> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                list.add(new Country(id, name));
            }
            return list;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cleanMessages() {
        try {
            Message message = consumer.receiveNoWait();
            while (message!=null) {
                message = consumer.receiveNoWait();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}