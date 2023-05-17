package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try {
            ConnectionThread connectionThread = new ConnectionThread("localhost", 5000);
            connectionThread.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Wpisz login:");
            String clientName = reader.readLine();
            connectionThread.login(clientName);

            while (true) {
                String message = reader.readLine();
                if (message.startsWith("/")){
                    String[] result = message.split(" ", 2);
                    switch (result[0]){
                        case "/w":
                            connectionThread.whisper(result[1]);
                            break;
                        default:
                            System.out.println("Nierozponane polecenie");
                    }
                }else {connectionThread.sendMessage(message);}
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}