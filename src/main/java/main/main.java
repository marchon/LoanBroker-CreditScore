package main;

import com.rabbitmq.client.GetResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import models.LoanRequest;
import org.json.JSONObject;
import services.CreditScoreService;
import workers.Consumer;
import workers.Publisher;

public class main {
    
    private static int numberOfWorkers;

    public static void main(String[] args) {
        
        System.out.println("CreditScore application running...");
        
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores);
        
        System.out.println("Cores available: " + cores);
        System.out.println("Initializing workers...");
        
        for (int i = 0; i < cores; i++) {
            pool.submit(new Consumer());
            System.out.println("Worker number " + (i+1) + " started.");
        }
        
        System.out.println("All workers are running.");
    }
}
