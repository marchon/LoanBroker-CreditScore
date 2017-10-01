package main;

import services.CreditScoreService;

public class main {

    public static void main(String[] args) {
        CreditScoreService cs = new CreditScoreService();
        System.out.println("Credit for user is: " + cs.getCreditScore("010180-5100"));
    }
}
