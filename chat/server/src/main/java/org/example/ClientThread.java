package org.example;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    PrintWriter writer;
    private Server server;

    private String clientName;

    public ClientThread(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public void run() {
        try {
            InputStream input  = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);

            System.out.println("New client!");
            String message;
            while ((message = reader.readLine()) != null) {
                String[] result = message.split(":", 2);
                switch (result[0]) {
                    case "message" -> server.broadcast(result[1], this);
                    case "login" -> login(result[1]);
                    case "whisper" -> server.whisper(result[1], this);
                }
            }
            System.out.println("client disconnected");
        } catch (IOException e) {
            server.removeClient(this);
            throw new RuntimeException(e);
        }

    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void login(String clientName) {
        this.clientName = clientName;
        this.sendMessage("Welcome, " + clientName);
        server.broadcast(" joined the chat", this);
    }
}