package services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Interface for the RabbitMQ Service
 * 
 * @author Group 6
 */
public interface IRabbitMQService {
    /**
     * Creates a connection to a RabbitMQ message queue.
     * 
     * @param host
     * @param user
     * @param pass
     * @param port
     * @return
     * @throws IOException
     * @throws TimeoutException 
     */
    public Connection getRabbitMQConnection(String host, String user, String pass, int port) throws IOException, TimeoutException;
    /**
     * Post a message to a specific exchange and route. Will be created if they does not exist.
     * 
     * @param message
     * @param exchangeName
     * @param routingKey
     * @param conn
     * @throws IOException
     */
    public void postToChannel(String message, String exchangeName, String routingKey, Connection conn) throws IOException;
    /**
     * Listen to a channel.
     * 
     * @param exchangeName
     * @param routingKey
     * @param conn
     * @return
     * @throws IOException 
     */
    public String listenToChannel(String exchangeName, String routingKey, Connection conn) throws IOException;
    /**
     * Close the RabbitMQ connection.
     * 
     * @param conn
     * @throws IOException
     * @throws TimeoutException 
     */
    public void closeConnection(Connection conn) throws IOException, TimeoutException;
    /**
     * Close the channel.
     * 
     * @param channel
     * @throws IOException
     * @throws TimeoutException 
     */
    public void closeChannel(Channel channel) throws IOException, TimeoutException;
}
