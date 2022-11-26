import dto.Country;
import dto.City;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class Server {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;

    private static final String SEPARATOR = "#";

    public void start() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:3009");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueTo = session.createQueue("toClient");
            Destination queueFrom = session.createQueue("fromClient");

            producer = session.createProducer(queueTo);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueFrom);

            while (processQuery()) {

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private boolean processQuery() {
        String response = "";
        String query = "";
        try {
            Message request = consumer.receive(500);
            if (request == null) {
                return true;
            }

            if (request instanceof TextMessage) {
                TextMessage message = (TextMessage) request;
                query = message.getText();
            } else {
                return true;
            }

            String[] fields = query.split(SEPARATOR);
            if (fields.length == 0) {
                return true;
            } else {
                String action = fields[0];
                Country country;
                City city;

                switch (action) {
                    case "CountryFindById":
                        Long id = Long.parseLong(fields[1]);
                        country = Country.findById(id);
                        response = country.getName();
                        TextMessage message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityFindByCountryId":
                        id = Long.parseLong(fields[1]);
                        List<City> list = City.findByCountryId(id);
                        StringBuilder str = new StringBuilder();
                        assert list != null;
                        citiesToString(str, list);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityFindByName":
                        String name = fields[1];
                        city = City.findByName(name);
                        assert city != null;
                        response = city.getCityId() + SEPARATOR + city.getName() + SEPARATOR + city.getPopulation() + SEPARATOR + city.getCountryId();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryFindByName":
                        name = fields[1];
                        country = Country.findByName(name);
                        assert country != null;
                        response = country.getId() + "";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        Integer year = Integer.parseInt(fields[3]);
                        Long countryId = Long.parseLong(fields[4]);
                        city = new City(id, name, year, countryId);
                        response = City.update(city) ? "true" : "false";
                        System.out.println(response);
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        country = new Country(id, name);
                        response = Country.update(country) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityInsert":
                        name = fields[1];
                        year = Integer.parseInt(fields[2]);
                        countryId = Long.parseLong(fields[3]);
                        city = new City((long) 0, name, year, countryId);
                        response = City.insert(city) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "countryInsert":
                        name = fields[1];
                        country = new Country();
                        country.setName(name);
                        response = Country.insert(country) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "cityDelete":
                        id = Long.parseLong(fields[1]);
                        city = new City();
                        city.setCityId(id);
                        response = City.delete(city) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "countryDelete":
                        id = Long.parseLong(fields[1]);
                        country = new Country();
                        country.setId(id);
                        response = Country.delete(country) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityAll":
                        List<City> citiesList = City.findAll();
                        str = new StringBuilder();
                        assert citiesList != null;
                        citiesToString(str, citiesList);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "countryAll":
                        List<Country> countryList = Country.findAll();
                        str = new StringBuilder();
                        assert countryList != null;
                        for (Country count : countryList) {
                            str.append(count.getId());
                            str.append(SEPARATOR);
                            str.append(count.getName());
                            str.append(SEPARATOR);
                        }
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                }
            }
            return true;
        } catch (JMSException ex) {
            return false;
        }
    }

    private void citiesToString(StringBuilder str, List<City> list) {
        for (City city : list) {
            str.append(city.getCityId());
            str.append(SEPARATOR);
            str.append(city.getName());
            str.append(SEPARATOR);
            str.append(city.getPopulation());
            str.append(SEPARATOR);
            str.append(city.getCountryId());
            str.append(SEPARATOR);
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

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}