package main;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.CreditScoreService;


public class main {
    public static void main(String[] args) {
        CreditScoreService cs = new CreditScoreService();
        System.out.println("Credit score is " + cs.getCreditScore("010180-5100"));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> "Hello World";
        Future<String> future = executor.submit(callable);
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        // future.get() returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();
    }
}
