package ru.dorofeev.storage.server;

public class ServerStart {

    public static void main(String[] args) throws Exception {

        Config config = new Config("C:\\server-storage");
        System.out.println(config);

        new Server(config).run();
    }
}
