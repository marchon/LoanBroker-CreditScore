package services;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * RabbitMQ service.
 * 
 * @author Group 6
 */
public class RabbitMQService implements IRabbitMQService {
    
    @Override
    public Connection getRabbitMQConnection(String host, String user, String pass, int port) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setHost(host);
        factory.setPort(port);
        return factory.newConnection();
    }
    
    @Override
    public void postToChannel(String message, String exchangeName, String routingKey, Connection conn) throws IOException {
        byte[] messageBytes = message.getBytes();
        Channel channel = getChannel(conn, exchangeName, routingKey);
        channel.basicPublish(exchangeName, routingKey, null, messageBytes);
    }
    
    @Override
    public String listenToChannel(String exhangeName, String routingKey, Connection conn) throws IOException {
        Channel channel = getChannel(conn, exhangeName, routingKey);
    }
    
    /**
     * Method that retrieve a channel, if the channel does not exist it is created.
     * 
     * @param conn
     * @param exchangeName
     * @param routingKey
     * @return
     * @throws IOException 
     */
    private Channel getChannel(Connection conn, String exchangeName, String routingKey) throws IOException {
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(exchangeName, "direct", true);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, routingKey);
        return channel;
    }

    @Override
    public void closeConnection(Connection conn) throws IOException, TimeoutException {
        conn.close();
    }

    @Override
    public void closeChannel(Channel channel) throws IOException, TimeoutException {
        channel.close();
    }
}
