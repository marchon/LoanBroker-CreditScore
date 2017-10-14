package workers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.LoanRequest;
import org.json.JSONObject;
import services.CreditScoreService;
import services.RabbitMQService;

/**
 *
 * @author Group 6
 */
public class Consumer implements Runnable {

    @Override
    public void run() {
        RabbitMQService service = new RabbitMQService();
        GetResponse response = null;

        try (Connection conn = service.getRabbitMQConnection("localhost")) {
            Channel channel = conn.createChannel();
            service.createQueue("g6_queue_credit", true, false, false, null, channel);
            service.createExchange("g6_queue_credit", "direct", true, channel);
            service.bindExchangeQueue("g6_queue_credit", "g6_queue_credit", "", channel);
            response = service.getResponseFromQueue("g6_queue_credit", "", true, channel);
            channel.close();

            byte[] body = response.getBody();
            JSONObject json = convertToJSON(body);
            String ssn = (String) json.get("ssn");

            int creditScore = getCreditScore(ssn);
            LoanRequest lr = createLoanRequest(json, creditScore);
            broadcast(lr);
            
            service.sendAcknowledgement(channel, response);
            
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Converts a byte[] into a JSONobject.
     * 
     * @param body
     * @return JSONObject
     */
    private JSONObject convertToJSON(byte[] body) {
        String jsonString = new String(body);
        return new JSONObject(jsonString);
    }

    /**
     * Takes a ssn and retrieves a credit score from the SOAP service.
     * 
     * @param ssn
     * @return int
     */
    private int getCreditScore(String ssn) {
        CreditScoreService cs = new CreditScoreService();
        return cs.getCreditScore(ssn);
    }

    /**
     * Generates a LoanRequest object from the json retrieved fromm the client and
     * with the credit score retrieved from the SOAP service.
     * 
     * @param json
     * @param creditScore
     * @return LoanRequest
     */
    private LoanRequest createLoanRequest(JSONObject json, int creditScore) {
        json.append("creditScore", creditScore);
        LoanRequest lr = new LoanRequest(
                (String) json.get("ssn"),
                creditScore,
                (Double) json.get("amount"),
                (int) json.get("days")
        );
        return lr;
    }
    
    /**
     * Broadcasts a LoanRequest to the system via the Publisher class.
     * 
     * @param lr 
     */
    private void broadcast(LoanRequest lr) {
        Publisher p = new Publisher(lr);
    }
}
