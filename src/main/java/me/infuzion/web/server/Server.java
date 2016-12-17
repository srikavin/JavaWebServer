package me.infuzion.web.server;

import me.infuzion.web.server.event.PageLoadEvent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server implements Runnable {
    private final Map<UUID, Map<String, String>> session;
    private final double version = 0.1;
    private boolean running;
    private ServerSocket serverSocket;
    private EventManager eventManager;
    private long lastRequestTime = -1;

    public Server(ServerSocket socket) {
        this.serverSocket = socket;
        running = true;
        eventManager = new EventManager();
        session = new HashMap<>();
    }

    public Map<UUID, Map<String, String>> getSession() {
        return session;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket client = serverSocket.accept();
                lastRequestTime = System.currentTimeMillis();
                new Thread(() -> {
                    try {
                        onRequest(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onRequest(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter writer = new PrintWriter(client.getOutputStream());

        int contentLength = -1;
        String page = "";
        String hostName = "";
        UUID sessionuuid = null;
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            final String contentLengthStr = "Content-Length: ";
            final String hostStr = "Host: ";
            final String cookieStr = "Cookie: ";
            if (line.startsWith(contentLengthStr)) {
                contentLength = Integer.parseInt(line.substring(contentLengthStr.length()));
                continue;
            }
            if (line.startsWith(hostStr)) {
                hostName = line.substring(hostStr.length());
                continue;
            }

            if (line.startsWith(cookieStr)) {
                String temp = line.substring(cookieStr.length());
                String[] cookies = temp.split(";");
                for (String e : cookies) {
                    System.out.println(e);
                    String[] cookie = e.split("=", 2);
                    if (cookie[0].trim().equals("session")) {
                        try {
                            UUID tempuuid = UUID.fromString(cookie[1]);
                            if (session.containsKey(tempuuid)) {
                                sessionuuid = tempuuid;
                            }
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }

            if (line.startsWith("GET") || line.startsWith("POST")) {
                page = line.split(" ")[1];
                continue;
            }

            if (line.length() == 0) {
                break;
            }
        }
        String contentString = null;
        if (contentLength != -1 && contentLength >= 0) {
            final char[] content = new char[contentLength];
            if (reader.read(content) == -1) {
                return;
            }
            contentString = new String(content);
        }

        PageLoadEvent event = new PageLoadEvent(page, contentString, hostName);
        eventManager.callEvent(event);
        System.out.println("Request recieved from: " + client.getInetAddress() + ":" + client.getPort() + " - " +
                event.getStatusCode() + " " + (System.currentTimeMillis() - lastRequestTime) + "ms " + event.getPage());
        generateResponse(writer, event.getStatusCode(), event.getFileEncoding(), event.getResponseData(), sessionuuid,
                event.getAdditionalHeaders());
    }

    private void generateResponse(PrintWriter writer, int status, String contentType, String responseData, UUID sessionuuid,
                                  Map<String, String> headers)
            throws UnsupportedEncodingException {
        writer.println("HTTP/1.1 " + status + "\r");
        writer.println("Content-Type: " + contentType + "; charset=UTF-8\r");
        writer.println("Content-Length: " + responseData.getBytes("UTF-8").length + "\r");
        writer.println("X-Powered-By: Java Web Server v" + version + "\r");
        for(Map.Entry e : headers.entrySet()){
            writer.println(e.getKey() + ": " + e.getValue());
        }

        if (sessionuuid == null) {
            UUID random = UUID.randomUUID();
            session.put(random, new HashMap<>());
            writer.println("Set-Cookie:session=" + random + "\r");
        }

        writer.println("X-Request-Time: " + (System.currentTimeMillis() - lastRequestTime) + "\r\n");

        writer.println(responseData);
        writer.flush();
    }
}
